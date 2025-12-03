/*
 * (c) 2003 Open Source Geospatial Foundation - all rights reserved
 * This code is licensed under the GPL 2.0 license,
 * available at the root application directory.
 */
package org.geonetwork.ogcapi.service.formatter;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.List;
import org.geonetwork.application.profile.ProfileDefault;
import org.geonetwork.application.profile.ProfileDefaultsConfiguration;
import org.geonetwork.schemas.model.schemaident.Formatter;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;

public class ProfileSelectorTest {

    /** this tests the easiest case - just using the default. */
    @Test
    public void test_default() {
        ProfileDefaultsConfiguration profileDefaultsConfiguration = new ProfileDefaultsConfiguration();
        profileDefaultsConfiguration.setProfileDefaults(new ArrayList<>());
        var profileDefault = new ProfileDefault("application/testcase", "testcase.profile", null);
        profileDefaultsConfiguration.getProfileDefaults().add(profileDefault);

        ProfileSelector profileSelector = new ProfileSelector(profileDefaultsConfiguration);

        var selectedFormatter = profileSelector.chooseProfile(
                MediaType.valueOf("application/testcase"), new ArrayList<>(), List.of(getFormatter1()));

        assertEquals("testcase.profile", selectedFormatter);
    }

    /** choose default when there are multiple formatters. */
    @Test
    public void test_default2() {
        ProfileDefaultsConfiguration profileDefaultsConfiguration = new ProfileDefaultsConfiguration();
        profileDefaultsConfiguration.setProfileDefaults(new ArrayList<>());
        var profileDefault = new ProfileDefault("application/testcase", "testcase.profile", null);
        profileDefaultsConfiguration.getProfileDefaults().add(profileDefault);

        ProfileSelector profileSelector = new ProfileSelector(profileDefaultsConfiguration);

        var selectedFormatter = profileSelector.chooseProfile(
                MediaType.valueOf("application/testcase"),
                new ArrayList<>(),
                List.of(getFormatter1(), getFormatter2()));

        assertEquals("testcase.profile", selectedFormatter);
    }

    /** choose requested profile (1st one) */
    @Test
    public void test_requested1() {
        ProfileDefaultsConfiguration profileDefaultsConfiguration = new ProfileDefaultsConfiguration();
        profileDefaultsConfiguration.setProfileDefaults(new ArrayList<>());
        var profileDefault = new ProfileDefault("application/testcase", "testcase.profile", null);
        profileDefaultsConfiguration.getProfileDefaults().add(profileDefault);

        ProfileSelector profileSelector = new ProfileSelector(profileDefaultsConfiguration);

        var selectedFormatter = profileSelector.chooseProfile(
                MediaType.valueOf("application/testcase"),
                List.of("testcase.profile"),
                List.of(getFormatter1(), getFormatter2()));

        assertEquals("testcase.profile", selectedFormatter);
    }

    /** choose requested profile (second) */
    @Test
    public void test_requested2() {
        ProfileDefaultsConfiguration profileDefaultsConfiguration = new ProfileDefaultsConfiguration();
        profileDefaultsConfiguration.setProfileDefaults(new ArrayList<>());
        var profileDefault = new ProfileDefault("application/testcase", "testcase.profile", null);
        profileDefaultsConfiguration.getProfileDefaults().add(profileDefault);

        ProfileSelector profileSelector = new ProfileSelector(profileDefaultsConfiguration);

        var selectedFormatter = profileSelector.chooseProfile(
                MediaType.valueOf("application/testcase"),
                List.of("testcase.profile2"),
                List.of(getFormatter1(), getFormatter2()));

        assertEquals("testcase.profile2", selectedFormatter);
    }

    /** choose requested profile - 2 requests, first is "good" */
    @Test
    public void test_requested_multi() {
        ProfileDefaultsConfiguration profileDefaultsConfiguration = new ProfileDefaultsConfiguration();
        profileDefaultsConfiguration.setProfileDefaults(new ArrayList<>());
        var profileDefault = new ProfileDefault("application/testcase", "testcase.profile", null);
        profileDefaultsConfiguration.getProfileDefaults().add(profileDefault);

        ProfileSelector profileSelector = new ProfileSelector(profileDefaultsConfiguration);

        var selectedFormatter = profileSelector.chooseProfile(
                MediaType.valueOf("application/testcase"),
                List.of("testcase.profile2", "NONEXIST"),
                List.of(getFormatter1(), getFormatter2()));

        assertEquals("testcase.profile2", selectedFormatter);

        selectedFormatter = profileSelector.chooseProfile(
                MediaType.valueOf("application/testcase"),
                List.of("NONEXIST", "testcase.profile2"),
                List.of(getFormatter1(), getFormatter2()));

        assertEquals("testcase.profile2", selectedFormatter);
    }

    private Formatter getFormatter1() {
        var formatter = new Formatter();
        formatter.setName("testcase.profile informal");
        formatter.setTitle("testcase.profile title");
        formatter.setOfficialProfileName("testcase.profile");
        formatter.setContentType("application/testcase");
        return formatter;
    }

    private Formatter getFormatter2() {
        var formatter = new Formatter();
        formatter.setName("testcase.profile2 informal");
        formatter.setTitle("testcase.profile2 title");
        formatter.setOfficialProfileName("testcase.profile2");
        formatter.setContentType("application/testcase");
        return formatter;
    }
}
