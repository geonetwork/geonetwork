/*
 * (c) 2003 Open Source Geospatial Foundation - all rights reserved
 * This code is licensed under the GPL 2.0 license,
 * available at the root application directory.
 */

package org.geonetwork.domain;

/**
 * The list of reserved groups. Ids and names are hardcoded and have special meaning in Geonetwork.
 *
 * @author Jesse
 */
public enum ReservedGroup {
  /** The "All" group. IE the group that represents all. */
  all(1),
  /**
   * The Intranet group. IE the group that represents all users within the same intranet as the
   * geonetwork server.
   */
  intranet(0),
  /** The "Guest" group. IE the group representing all users not signed in. */
  guest(-1);

  private final int _id;

  private ReservedGroup(int id) {
    _id = id;
  }

  public static boolean isReserved(int grpId) {
    for (ReservedGroup reservedGroup : values()) {
      if (reservedGroup.getId() == grpId) {
        return true;
      }
    }
    return false;
  }

  /**
   * Get the id of the reserved group.
   *
   * @return the id of the reserved group.
   */
  public int getId() {
    return _id;
  }

  /**
   * Create a detached Group that represents the reserved group.
   *
   * @return a detached Group that represents the reserved group.
   */
  public Group getGroupEntityTemplate() {
    return Group.builder().id(_id).name(name()).description(name()).build();
  }
}
