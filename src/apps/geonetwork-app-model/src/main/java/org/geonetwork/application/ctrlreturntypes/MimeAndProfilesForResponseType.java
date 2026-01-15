/*
 * SPDX-FileCopyrightText: 2001 FAO-UN and others <geonetwork@osgeo.org>
 * SPDX-License-Identifier: GPL-2.0-or-later
 */
package org.geonetwork.application.ctrlreturntypes;

import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.geonetwork.application.formatters.MessageWriterUtil;
import org.geonetwork.application.profile.ProfileDefaultsConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;

@Configuration
public class MimeAndProfilesForResponseType {

    @Autowired
    MessageWriterUtil messageWriterUtil;

    @Autowired
    ProfileDefaultsConfiguration profileDefaultsConfiguration;

    /**
     * This only give SEPECIFIC output for the response type, and not generic (i.e. xml)
     *
     * @param responseType IControllerResponse type to search for specific formatters.
     * @return metadata about the response type
     * @throws Exception missconfig?
     */
    @SuppressWarnings("unchecked")
    public List<ResponseTypeInfo> getResponseTypeInfos(Class<?> responseType) throws Exception {
        if (!IControllerResponseObject.class.isAssignableFrom(responseType)) {
            // We have search by the ACTUAL controller response (i.e. auto-generated), not the IControllerResponseObject
            throw new Exception("bad type"); // TODO: handle this case
        }

        var writers = messageWriterUtil.getAllMessageConverters().stream()
                .filter(x -> x instanceof IControllerResultFormatter)
                .map(x -> ((IControllerResultFormatter) x))
                .filter(x -> x.getInputType().equals(responseType))
                .toList();

        var writersNormal = messageWriterUtil.getAllMessageConverters().stream()
                .filter(x -> getInputObject(x) == responseType)
                .filter(x -> !(x instanceof IControllerResultFormatter))
                .toList();

        var result = new ArrayList<ResponseTypeInfo>();

        for (var writer : writers) {
            var item = new ResponseTypeInfo();
            item.setMimeType(writer.getMimeType());
            item.setProfiles(writer.getProvidedProfileNames());
            item.setFormatProviders(List.of(writer.getClass().getSimpleName()));
            item.setDefaultProfile(profileDefaultsConfiguration.getDefaultProfile(writer.getMimeType(), responseType));
            result.add(item);
        }

        for (var writer : writersNormal) {
            for (var mediaType : writer.getSupportedMediaTypes()) {
                var item = new ResponseTypeInfo();
                item.setMimeType(mediaType);
                item.setFormatProviders(List.of(writer.getClass().getSimpleName()));
                result.add(item);
            }
        }

        return result;
    }

    @SuppressWarnings("unchecked")
    private Class<?> getInputObject(HttpMessageConverter<?> writer) {
        var methods = Arrays.stream(writer.getClass().getMethods());
        var writeMethod = methods.filter(x -> x.getName().equals("write")
                        && x.getParameterTypes().length > 0
                        && !x.getParameterTypes()[0].equals(Object.class)
                        && !x.getParameterTypes()[0].equals(IControllerResponseObject.class))
                .findFirst();

        if (writeMethod.isPresent()) {
            return writeMethod.get().getParameterTypes()[0];
        }

        // we assume that the first generic type is the right
        try {
            var clazz = ((Class<?>)
                    ((ParameterizedType) writer.getClass().getGenericSuperclass()).getActualTypeArguments()[0]);
            if (IControllerResponseObject.class.isAssignableFrom(clazz)) {
                return clazz;
            }
            return null;
        } catch (Exception e) {
            return null;
        }
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ResponseTypeInfo {
        MediaType mimeType;
        List<String> profiles;
        String defaultProfile;
        List<String> formatProviders;
    }
}
