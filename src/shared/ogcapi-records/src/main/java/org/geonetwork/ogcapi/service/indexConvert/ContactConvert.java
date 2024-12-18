/*
 * (c) 2003 Open Source Geospatial Foundation - all rights reserved
 * This code is licensed under the GPL 2.0 license,
 * available at the root application directory.
 */
package org.geonetwork.ogcapi.service.indexConvert;

import static org.apache.commons.lang3.StringUtils.isBlank;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import org.geonetwork.index.model.record.Contact;
import org.geonetwork.ogcapi.records.generated.model.OgcApiRecordsAddressDto;
import org.geonetwork.ogcapi.records.generated.model.OgcApiRecordsContactDto;
import org.geonetwork.ogcapi.records.generated.model.OgcApiRecordsEmailDto;
import org.geonetwork.ogcapi.records.generated.model.OgcApiRecordsLinkDto;
import org.geonetwork.ogcapi.records.generated.model.OgcApiRecordsPhoneDto;

public class ContactConvert {

    public static List<OgcApiRecordsContactDto> convert(Map<String, List<Contact>> contacts, String lang3iso) {
        var result = contacts.entrySet().stream()
                .map(x -> convert(x.getKey(), x.getValue(), lang3iso))
                .flatMap(List::stream)
                .toList();
        return result.stream().filter(x -> !isBlank(x.getName())).toList();
    }

    public static List<OgcApiRecordsContactDto> convert(String role, List<Contact> contacts, String lang3iso) {
        return contacts.stream().map(x -> convert(role, x, lang3iso)).toList();
    }

    public static OgcApiRecordsContactDto convert(String role, Contact contact, String lang3iso) {
        var result = new OgcApiRecordsContactDto();

        // probably not filled in
        if (contact.getIdentifier() != null && !contact.getIdentifier().isEmpty()) {
            var item = contact.getIdentifier().getFirst();

            if (!isBlank(item.getCode()) && !isBlank(item.getCodeSpace())) {
                result.setIdentifier(item.getCodeSpace() + item.getCode());
            } else if (!isBlank(item.getCode())) {
                result.setIdentifier(item.getCode());
            } else if (!isBlank(item.getCodeSpace())) {
                result.setIdentifier(item.getCodeSpace());
            } else if (!isBlank(item.getLink())) {
                result.setIdentifier(item.getLink());
            }
        }

        if (!isBlank(contact.getEmail())) {
            result.addEmailsItem(new OgcApiRecordsEmailDto(contact.getEmail()));
        }
        if (!isBlank(contact.getPhone())) {
            result.addPhonesItem(new OgcApiRecordsPhoneDto(contact.getPhone()));
        }

        if (!isBlank(contact.getIndividual())) {
            result.setName(contact.getIndividual());
        }

        if (!isBlank(contact.getAddress())) {
            var address = new OgcApiRecordsAddressDto();
            address.addDeliveryPointItem(contact.getAddress());
            result.addAddressesItem(address);
        }

        if (!isBlank(contact.getPosition())) {
            result.setPosition(contact.getPosition());
        }

        var organization = ElasticIndex2Catalog.getLangString(contact.getOrganisation(), lang3iso);
        if (!isBlank(organization)) {
            result.setOrganization(organization);
        }

        var roles = Arrays.asList(role, contact.getRole());
        roles = roles.stream().filter(Objects::nonNull).toList();

        if (!roles.isEmpty()) {
            result.setRoles(roles);
        }

        if (!isBlank(contact.getLogo())) {
            try {
                result.setLogo(new OgcApiRecordsLinkDto(new URI(contact.getLogo())));
            } catch (URISyntaxException e) {
                // no action
            }
        }

        if (!isBlank(contact.getWebsite())) {
            try {
                result.addLinksItem(new OgcApiRecordsLinkDto(new URI(contact.getWebsite())));
            } catch (URISyntaxException e) {
                // no action
            }
        }

        return result;
    }
}
