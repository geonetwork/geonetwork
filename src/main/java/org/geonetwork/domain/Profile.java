/*
 * Copyright (C) 2001-2016 Food and Agriculture Organization of the
 * United Nations (FAO-UN), United Nations World Food Programme (WFP)
 * and United Nations Environment Programme (UNEP)
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or (at
 * your option) any later version.
 *
 * This program is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301, USA
 *
 * Contact: Jeroen Ticheler - FAO - Viale delle Terme di Caracalla 2,
 * Rome - Italy. email: geonetwork@osgeo.org
 */

package org.geonetwork.domain;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

/** The enumeration of profiles available */
public enum Profile {
  Administrator,
  UserAdmin(Administrator),
  Reviewer(UserAdmin),
  Editor(Reviewer),
  RegisteredUser(Editor),
  Guest(RegisteredUser),
  Monitor(Administrator);

  private final Set<Profile> parents;

  Profile(Profile... parents) {
    this.parents = new HashSet<>(Arrays.asList(parents));
  }

  /**
   * A case-sensitive search for profile
   *
   * @param profile the name of the profile to check.
   */
  public static boolean exists(String profile) {
    return Arrays.stream(values()).anyMatch(p -> p.name().equals(profile));
  }

  /**
   * Find the profile by name but ignore case errors.
   *
   * @param profileName The profile name.
   */
  public static Profile findProfileIgnoreCase(String profileName) {
    return Arrays.stream(values())
        .filter(profile -> profile.name().equalsIgnoreCase(profileName))
        .findFirst()
        .orElse(null);
  }

  public Set<Profile> getParents() {
    return Arrays.stream(values())
        .filter(profile -> profile.parents.contains(this))
        .collect(Collectors.toSet());
  }

  public Set<Profile> getAll() {
    Set<Profile> all = new HashSet<>(getParents());
    all.add(this);
    getParents().forEach(parent -> all.addAll(parent.getAll()));
    return all;
  }

  public Set<String> getAllNames() {
    return getAll().stream().map(Profile::name).collect(Collectors.toSet());
  }
}
