/*
 * (c) 2003 Open Source Geospatial Foundation - all rights reserved
 * This code is licensed under the GPL 2.0 license,
 * available at the root application directory.
 */
package org.geonetwork.ogcapi.service.indexConvert.dynamic;

import co.elastic.clients.elasticsearch.indices.IndexState;
import com.google.common.base.Splitter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.compress.utils.Lists;
import org.apache.commons.lang3.StringUtils;
import org.geonetwork.index.client.IndexClient;
import org.geonetwork.index.model.record.IndexRecord;
import org.geonetwork.ogcapi.records.generated.model.OgcApiRecordsRecordGeoJSONDto;
import org.geonetwork.ogcapi.service.configuration.OgcElasticFieldsMapperConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * this allows you to pull properties out of the elastic index (`IndexRecord`) and put them in the properties section of
 * the ogcapi-records json. This is similar to an XPath.
 *
 * <p>The main work is traversing the `IndexRecord` tree.
 *
 * <p>Create the path separated by ".". Here are the different types of path components
 *
 * <p>1. word (i.e. "scaleDenominator") --> look for a method called `getScaleDenominator()` 2. [#] (i.e. [0]) --> get
 * the index inside the list 3. ["word"] (i.e. ["scaleDenominator"]) --> get a map's value by key 4. [*] --> this means
 * it will return a list of items
 *
 * <p>example: codelists.["cl_maintenanceAndUpdateFrequency"].[*].properties.["key"]
 *
 * <p>1. on `IndexRecord` call the method `getCodeLists()` (this returns a Map) 2. call
 * `#.get("cl_maintenanceAndUpdateFrequency")` on the map (this will return a list) 3. [*] --> for each of the items in
 * the list, execute `properties.["key"]` and turn those items a. call `getProperties()` - returns a Map b. call
 * `#.get("key") on the map -- returns a String
 *
 * <p>SEE ExtraElasticPropertiesServiceTest
 */
@Component
@Slf4j
public class ExtraElasticPropertiesService {

    OgcElasticFieldsMapperConfig config; // user-configured fields
    String indexRecordName; // typically "gn-records"

    IndexState elasticIndexInfo;

    // for test cases
    public ExtraElasticPropertiesService(
            OgcElasticFieldsMapperConfig config,
            @Value("${geonetwork.index.indexRecordName:'gn-records'}") String indexRecordName,
            IndexState elasticIndexInfo) {
        this.config = config;
        this.indexRecordName = indexRecordName;
        this.elasticIndexInfo = elasticIndexInfo;
    }

    @Autowired
    public ExtraElasticPropertiesService(
            OgcElasticFieldsMapperConfig config,
            @Value("${geonetwork.index.indexRecordName:'gn-records'}") String indexRecordName,
            IndexClient client) {
        this.config = config;
        this.indexRecordName = indexRecordName;

        try {
            if (client != null) {
                this.elasticIndexInfo = client.getEsClient()
                        .indices()
                        .get(x -> x.index(indexRecordName))
                        .get(indexRecordName);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * given an elastic IndexRecord, inject the configured properties into the result.Properties.
     *
     * @param indexRecord from elastic (source info)
     * @param iso3lang incase there are language-specific properties
     * @param result ogcapi-records document with the extra properties injected
     */
    public void inject(IndexRecord indexRecord, String iso3lang, OgcApiRecordsRecordGeoJSONDto result) {
        for (var field : config.getFields()) {
            try {
                var indexPath = field.getIndexRecordProperty();
                var elasticValue = getFromElasticIndexRecord(indexPath, indexRecord);
                result.getProperties().putAdditionalProperty(field.getOgcProperty(), elasticValue);
            } catch (Exception e) {
                log.info("Error parsing field ogc='" + field.getOgcProperty() + "' elastic='"
                        + field.getElasticProperty() + "' indexrecord='" + field.getIndexRecordProperty() + "': "
                        + e.getMessage());
            }
        }
    }

    /**
     * Given a path (See definition, above) it will traverse through the indexRecord to ge a value.
     *
     * @param path traversal instructions (See definition, above)
     * @param indexRecord Elastic JSON object parsed to an object
     * @return value at the end of the traversal
     * @throws Exception bad path for the object
     */
    public Object getFromElasticIndexRecord(String path, IndexRecord indexRecord) throws Exception {
        if (path.length() - path.replace("*", "").length() > 1) {
            throw new Exception("IndexRecordProperty - contains more than one * - " + path);
        }
        var pathParts = Splitter.on('.').split(path);
        return getByPath(indexRecord, Lists.newArrayList(pathParts.iterator()));
    }

    /**
     * iterates through the path components for an object.
     *
     * @param object object to traverse (reflection)
     * @param path components to traverse
     * @return end value after traversal
     */
    public Object getByPath(Object object, List<String> path) {
        if (path.isEmpty()) {
            return object;
        }
        Object current = object;
        while (!path.isEmpty()) {
            var pathPart = path.removeFirst();
            if (pathPart.equals("[*]") || pathPart.equals("*") && (object instanceof List)) {
                return getByPathMulti((List) current, path);
            } else if (pathPart.startsWith("[") && pathPart.endsWith("]")) {
                current = getByIndex(current, pathPart);
            } else {
                current = getByProperty(current, pathPart);
            }
        }
        return current;
    }

    /**
     * When a "[*]" is found, we get all the values from that list and return them.
     *
     * @param list set of value that the path with be executed on
     * @param path traversal instructions
     * @return for each of the values in the list, the results of traversal
     */
    @SuppressWarnings("unchecked")
    private List getByPathMulti(List list, List<String> path) {
        return list.stream().map(x -> getByPath(x, new ArrayList<>(path))).toList();
    }

    /**
     * given a property name and object, get the value.
     *
     * @param object object to get the value from
     * @param propertyName name of the property
     * @return value of the property in the object
     */
    public Object getByProperty(Object object, String propertyName) {
        var clazz = object.getClass();

        // this is the usual case - i.e. `#getScale()`
        try {
            var pname = StringUtils.capitalize(propertyName); // caps first letter
            var method = clazz.getMethod("get" + pname);
            return method.invoke(object);
        } catch (Exception e) {
        }

        // might be a field with the same name
        try {
            var field = clazz.getField(propertyName);
            return field.get(object);
        } catch (Exception e) {
        }

        // we don't capitalize the first letter (unlikely to work)
        try {
            var method = clazz.getMethod("get" + propertyName);
            return method.invoke(object);
        } catch (Exception e) {
        }

        throw new RuntimeException("Could not find property in IndexRecord (or inside property): " + propertyName);
    }

    /**
     * Given an object (either a list or a map), extract the sub-value from it.
     *
     * @param object where to get info from
     * @param index [#] (list) or ["name"] (map)
     * @return sub-value of the object
     */
    public Object getByIndex(Object object, String index) {
        index = index.substring(1, index.length() - 1); // trim "[]"
        if (index.startsWith("\"") && index.endsWith("\"")) {
            index = index.substring(1, index.length() - 1); // trim ""
            var map = (Map) object;
            return map.get(index);
        } else {
            var idx = Integer.parseInt(index);
            var list = (List) object;
            return list.get(idx);
        }
    }
}
