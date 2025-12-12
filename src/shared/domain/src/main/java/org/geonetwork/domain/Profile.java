/*
 * SPDX-FileCopyrightText: 2001 FAO-UN and others <geonetwork@osgeo.org>
 * SPDX-License-Identifier: GPL-2.0-or-later
 */
package org.geonetwork.domain;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

/** The enumeration of profiles available */
public enum Profile {
    Administrator(0),
    UserAdmin(1, Administrator),
    Reviewer(2, UserAdmin),
    Editor(3, Reviewer),
    RegisteredUser(4, Editor),
    Guest(5, RegisteredUser),
    Monitor(6, Administrator);

    private final int id;

    @SuppressWarnings("ImmutableEnumChecker")
    private final Set<Profile> parents;

    Profile(int id, Profile... parents) {
        this.id = id;
        this.parents = Set.of(parents);
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

    public int getId() {
        return id;
    }

    public Set<String> getAllNames() {
        return getAll().stream().map(Profile::name).collect(Collectors.toSet());
    }
}
