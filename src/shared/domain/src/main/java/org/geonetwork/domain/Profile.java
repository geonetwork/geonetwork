/*
 * (c) 2003 Open Source Geospatial Foundation - all rights reserved
 * This code is licensed under the GPL 2.0 license,
 * available at the root application directory.
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

    @SuppressWarnings("ImmutableEnumChecker")
    private final Set<Profile> parents;

    Profile(Profile... parents) {
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

    public Set<String> getAllNames() {
        return getAll().stream().map(Profile::name).collect(Collectors.toSet());
    }
}
