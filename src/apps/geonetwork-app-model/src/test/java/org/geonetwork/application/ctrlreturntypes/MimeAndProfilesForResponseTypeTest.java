/*
 * SPDX-FileCopyrightText: 2001 FAO-UN and others <geonetwork@osgeo.org>
 * SPDX-License-Identifier: GPL-2.0-or-later
 */
package org.geonetwork.application.ctrlreturntypes;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.lang.reflect.Type;
import java.util.List;
import org.geonetwork.application.formatters.MessageWriterUtil;
import org.geonetwork.application.profile.ProfileDefaultsConfiguration;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.HttpOutputMessage;
import org.springframework.http.MediaType;
import org.springframework.http.converter.AbstractGenericHttpMessageConverter;
import org.springframework.http.converter.HttpMessageConverter;

class MimeAndProfilesForResponseTypeTest {

    abstract static class ResponseA implements IControllerResponseObject {}

    abstract static class ResponseB implements IControllerResponseObject {}

    /** Generic writer (like the HTML one) that handles ResponseA only. */
    static class GenericWriter extends AbstractGenericHttpMessageConverter<Object> implements IMultiResponseTypeWriter {
        GenericWriter() {
            super(MediaType.TEXT_HTML, MediaType.APPLICATION_JSON);
        }

        @Override
        public List<Class<? extends IControllerResponseObject>> getResponseTypes() {
            return List.of(ResponseA.class);
        }

        @Override
        protected void writeInternal(Object o, Type type, HttpOutputMessage outputMessage) {}

        @Override
        protected Object readInternal(Class<?> clazz, HttpInputMessage inputMessage) {
            return null;
        }

        @Override
        public Object read(Type type, Class<?> contextClass, HttpInputMessage inputMessage) {
            return null;
        }
    }

    @Test
    @SuppressWarnings({"unchecked", "rawtypes"})
    void genericMultiTypeWriterIsDiscoveredAndSpecificFormatterWins() throws Exception {
        IControllerResultFormatter jsonFormatter = mock(IControllerResultFormatter.class);
        when(jsonFormatter.getInputType()).thenReturn(ResponseA.class);
        when(jsonFormatter.getMimeType()).thenReturn(MediaType.valueOf("application/json;charset=UTF-8"));

        var util = new MessageWriterUtil();
        util.setAllMessageConverters(List.<HttpMessageConverter<?>>of(jsonFormatter, new GenericWriter()));

        var lookup = new MimeAndProfilesForResponseType();
        lookup.messageWriterUtil = util;
        lookup.profileDefaultsConfiguration = mock(ProfileDefaultsConfiguration.class);

        var infosA = lookup.getResponseTypeInfos(ResponseA.class);
        assertEquals(
                List.of(MediaType.valueOf("application/json;charset=UTF-8"), MediaType.TEXT_HTML),
                infosA.stream()
                        .map(MimeAndProfilesForResponseType.ResponseTypeInfo::getMimeType)
                        .toList());
        assertEquals(List.of("GenericWriter"), infosA.get(1).getFormatProviders());
        assertEquals(List.of(), infosA.get(1).getProfiles());

        assertEquals(List.of(), lookup.getResponseTypeInfos(ResponseB.class));
    }
}
