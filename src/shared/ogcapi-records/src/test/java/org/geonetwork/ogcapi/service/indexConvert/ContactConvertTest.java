/*
 * (c) 2003 Open Source Geospatial Foundation - all rights reserved
 * This code is licensed under the GPL 2.0 license,
 * available at the root application directory.
 */
package org.geonetwork.ogcapi.service.indexConvert;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.geonetwork.index.model.record.Contact;
import org.junit.jupiter.api.Test;

public class ContactConvertTest {

    @Test
    public void testConvert() throws URISyntaxException {
        var contactsByRole = createContactsByRole();
        var contacts = ContactConvert.convert(contactsByRole, null);

        assertEquals("Ms. Testcase", contacts.get(0).getName());
        assertEquals("test case position", contacts.get(0).getPosition());

        assertEquals(1, contacts.get(0).getLinks().size());
        assertEquals(
                new URI("http://geocat.net"), contacts.get(0).getLinks().get(0).getHref());

        assertEquals(1, contacts.get(0).getAddresses().size());
        assertEquals(1, contacts.get(0).getAddresses().get(0).getDeliveryPoint().size());
        assertEquals(
                "123 testcase lane",
                contacts.get(0).getAddresses().get(0).getDeliveryPoint().get(0));

        assertEquals(1, contacts.get(0).getEmails().size());
        assertEquals("test@test.com", contacts.get(0).getEmails().get(0).getValue());

        assertEquals(1, contacts.get(0).getPhones().size());
        assertEquals("555-555-5555", contacts.get(0).getPhones().get(0).getValue());

        assertEquals(2, contacts.get(0).getRoles().size());
        assertEquals("testcase", contacts.get(0).getRoles().get(0));
        assertEquals("testcase-role", contacts.get(0).getRoles().get(1));

        assertEquals("default org", contacts.get(0).getOrganization());
    }

    @Test
    public void testConvertOrgLang() {
        var contactsByRole = createContactsByRole();

        var contacts = ContactConvert.convert(contactsByRole, null);
        assertEquals("default org", contacts.get(0).getOrganization());

        contacts = ContactConvert.convert(contactsByRole, "eng");
        assertEquals("eng org", contacts.get(0).getOrganization());

        contacts = ContactConvert.convert(contactsByRole, "xyz");
        assertEquals("default org", contacts.get(0).getOrganization());
    }

    public Map<String, List<Contact>> createContactsByRole() {
        var contactByRole = new HashMap<String, List<Contact>>();
        var contacts = new ArrayList<Contact>();
        contactByRole.put("testcase", contacts);

        var contact = new Contact();
        contact.setRole("testcase-role");
        contact.setEmail("test@test.com");
        contact.setPhone("555-555-5555");
        contact.setIndividual("Ms. Testcase");
        contact.setAddress("123 testcase lane");
        contact.setPosition("test case position");
        contact.setWebsite("http://geocat.net");

        var org = new HashMap<String, String>();
        org.put("default", "default org");
        org.put("langeng", "eng org");
        contact.setOrganisation(org);

        contacts.add(contact);

        return contactByRole;
    }
}
