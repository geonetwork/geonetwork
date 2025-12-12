/*
 * SPDX-FileCopyrightText: 2001 FAO-UN and others <geonetwork@osgeo.org>
 * SPDX-License-Identifier: GPL-2.0-or-later
 */
package org.geonetwork.utility.legacy.resolver;

public class ProxyParams {
    String username, password, proxyHost;
    int proxyPort;
    boolean useProxy = false;
    boolean useProxyAuth = false;
}
