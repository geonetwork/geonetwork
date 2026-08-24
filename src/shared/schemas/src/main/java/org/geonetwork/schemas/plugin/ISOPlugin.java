/*
 * (c) 2003 Open Source Geospatial Foundation - all rights reserved
 * This code is licensed under the GPL 2.0 license,
 * available at the root application directory.
 */
package org.geonetwork.schemas.plugin;

import java.util.List;
import java.util.Map;
import org.jdom.Element;

/** Created by francois on 31/01/15. */
public interface ISOPlugin {
    /** Return the name (with namespace prefix) for the basic default type. */
    String getBasicTypeCharacterStringName();

    /** Return an element to be use as default when creating new elements. */
    Element createBasicTypeCharacterString();

    /**
     * Add operatesOn and coupledResource element to a service metadata record.
     *
     * @return the updated record
     */
    Element addOperatesOn(Element serviceRecord, Map<String, String> layers, String serviceType, String baseUrl);

    class Extent {
        public double xmin;
        public double xmax;
        public double ymin;
        public double ymax;

        public Extent(Double xmin, Double xmax, Double ymin, Double ymax) {
            this.xmin = xmin;
            this.xmax = xmax;
            this.ymin = ymin;
            this.ymax = ymax;
        }
    }

    List<Extent> getExtents(Element record);
}
