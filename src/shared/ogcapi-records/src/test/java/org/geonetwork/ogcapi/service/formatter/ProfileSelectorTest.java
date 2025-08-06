/*
 * (c) 2003 Open Source Geospatial Foundation - all rights reserved
 * This code is licensed under the GPL 2.0 license,
 * available at the root application directory.
 */
package org.geonetwork.ogcapi.service.formatter;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.List;
import org.geonetwork.ogcapi.service.configuration.ItemPageLinksConfiguration;
import org.geonetwork.ogcapi.service.configuration.ProfileDefault;
import org.geonetwork.schemas.SchemaPluginConfiguration;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;

public class ProfileSelectorTest {

    /** this tests the easiest case - just using the default. */
    @Test
    public void test_default() {
        ItemPageLinksConfiguration itemPageLinksConfiguration = new ItemPageLinksConfiguration();
        itemPageLinksConfiguration.setProfileDefaults(new ArrayList<>());
        var profileDefault = new ProfileDefault("application/testcase", "testcase.profile");
        itemPageLinksConfiguration.getProfileDefaults().add(profileDefault);

        ProfileSelector profileSelector = new ProfileSelector(itemPageLinksConfiguration);

        var selectedFormatter = profileSelector.chooseProfile(
                MediaType.valueOf("application/testcase"), new ArrayList<>(), List.of(getFormatter1()));

        assertEquals("testcase.profile", selectedFormatter);
    }

    /** choose default when there are multiple formatters. */
    @Test
    public void test_default2() {
        ItemPageLinksConfiguration itemPageLinksConfiguration = new ItemPageLinksConfiguration();
        itemPageLinksConfiguration.setProfileDefaults(new ArrayList<>());
        var profileDefault = new ProfileDefault("application/testcase", "testcase.profile");
        itemPageLinksConfiguration.getProfileDefaults().add(profileDefault);

        ProfileSelector profileSelector = new ProfileSelector(itemPageLinksConfiguration);

        var selectedFormatter = profileSelector.chooseProfile(
                MediaType.valueOf("application/testcase"),
                new ArrayList<>(),
                List.of(getFormatter1(), getFormatter2()));

        assertEquals("testcase.profile", selectedFormatter);
    }

    /** choose requested profile (1st one) */
    @Test
    public void test_requested1() {
        ItemPageLinksConfiguration itemPageLinksConfiguration = new ItemPageLinksConfiguration();
        itemPageLinksConfiguration.setProfileDefaults(new ArrayList<>());
        var profileDefault = new ProfileDefault("application/testcase", "testcase.profile");
        itemPageLinksConfiguration.getProfileDefaults().add(profileDefault);

        ProfileSelector profileSelector = new ProfileSelector(itemPageLinksConfiguration);

        var selectedFormatter = profileSelector.chooseProfile(
                MediaType.valueOf("application/testcase"),
                List.of("testcase.profile"),
                List.of(getFormatter1(), getFormatter2()));

        assertEquals("testcase.profile", selectedFormatter);
    }

    /** choose requested profile (second) */
    @Test
    public void test_requested2() {
        ItemPageLinksConfiguration itemPageLinksConfiguration = new ItemPageLinksConfiguration();
        itemPageLinksConfiguration.setProfileDefaults(new ArrayList<>());
        var profileDefault = new ProfileDefault("application/testcase", "testcase.profile");
        itemPageLinksConfiguration.getProfileDefaults().add(profileDefault);

        ProfileSelector profileSelector = new ProfileSelector(itemPageLinksConfiguration);

        var selectedFormatter = profileSelector.chooseProfile(
                MediaType.valueOf("application/testcase"),
                List.of("testcase.profile2"),
                List.of(getFormatter1(), getFormatter2()));

        assertEquals("testcase.profile2", selectedFormatter);
    }

    /** choose requested profile - 2 requests, first is "good" */
    @Test
    public void test_requested_multi() {
        ItemPageLinksConfiguration itemPageLinksConfiguration = new ItemPageLinksConfiguration();
        itemPageLinksConfiguration.setProfileDefaults(new ArrayList<>());
        var profileDefault = new ProfileDefault("application/testcase", "testcase.profile");
        itemPageLinksConfiguration.getProfileDefaults().add(profileDefault);

        ProfileSelector profileSelector = new ProfileSelector(itemPageLinksConfiguration);

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

    private SchemaPluginConfiguration.Formatter getFormatter1() {
        return SchemaPluginConfiguration.Formatter.builder()
                .name("testcase.profile informal")
                .title("testcase.profile title")
                .officialProfileName("testcase.profile")
                .contentType("application/testcase")
                .build();
    }

    private SchemaPluginConfiguration.Formatter getFormatter2() {
        return SchemaPluginConfiguration.Formatter.builder()
                .name("testcase.profile2 informal")
                .title("testcase.profile2 title")
                .officialProfileName("testcase.profile2")
                .contentType("application/testcase")
                .build();
    }
}
