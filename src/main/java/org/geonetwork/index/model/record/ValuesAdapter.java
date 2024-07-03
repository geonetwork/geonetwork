/*
 * (c) 2003 Open Source Geospatial Foundation - all rights reserved
 * This code is licensed under the GPL 2.0 license,
 * available at the root application directory.
 */


package org.geonetwork.index.model.record;

import jakarta.xml.bind.annotation.adapters.XmlAdapter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ValuesAdapter
    extends XmlAdapter<ListOfValues, Map<String, List<String>>> {

    @Override
    public Map<String, List<String>> unmarshal(ListOfValues loe)
        throws Exception {
        Map<String, List<String>> map = new HashMap<>();
        for (Values values : loe.getValues()) {
            map.put(values.getKey(), values.getValues());
        }
        return map;
    }

    @Override
    public ListOfValues marshal(Map<String, List<String>> map)
        throws Exception {
        ListOfValues loe = new ListOfValues();
        for (Map.Entry<String, List<String>> mapValues : map.entrySet()) {
            Values values = new Values();
            values.setKey(mapValues.getKey());
            values.getValues().addAll(mapValues.getValue());
            loe.getValues().add(values);
        }
        return loe;
    }
}
