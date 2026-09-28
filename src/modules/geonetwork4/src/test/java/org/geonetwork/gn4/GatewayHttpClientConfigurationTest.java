/*
 * SPDX-FileCopyrightText: 2001 FAO-UN and others <geonetwork@osgeo.org>
 * SPDX-License-Identifier: GPL-2.0-or-later
 */
package org.geonetwork.gn4;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.time.Duration;
import org.junit.jupiter.api.Test;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;

class GatewayHttpClientConfigurationTest {

    @Test
    void buildsAPooledRequestFactory() {
        var builder =
                new GatewayHttpClientConfiguration().clientHttpRequestFactoryBuilder(100, 50, Duration.ofSeconds(5));

        HttpComponentsClientHttpRequestFactory factory = builder.build();

        assertNotNull(factory);
    }
}
