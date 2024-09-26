/*
 * (c) 2003 Open Source Geospatial Foundation - all rights reserved
 * This code is licensed under the GPL 2.0 license,
 * available at the root application directory.
 */
package org.geonetwork.schemas.plugin;

import java.util.Set;
import org.jdom.Element;

/** Created by francois on 8/20/14. */
public interface AssociatedResourcesSchemaPlugin {

  /** Analyse a metadata record and extract associated resources. */
  public abstract Set<AssociatedResource> getAssociatedResourcesUUIDs(Element metadata);

  /** Analyze a metadata record and extract associated parents */
  public abstract Set<String> getAssociatedParentUUIDs(Element metadata);

  public abstract Set<String> getAssociatedDatasetUUIDs(Element metadata);

  public abstract Set<String> getAssociatedFeatureCatalogueUUIDs(Element metadata);

  public abstract Set<String> getAssociatedSourceUUIDs(Element metadata);

  public abstract Set<AssociatedResource> getAssociatedParents(Element metadata);

  public abstract Set<AssociatedResource> getAssociatedDatasets(Element metadata);

  public abstract Set<AssociatedResource> getAssociatedFeatureCatalogues(Element metadata);

  public abstract Set<AssociatedResource> getAssociatedSources(Element metadata);
}
