/*
 * SPDX-FileCopyrightText: 2001 FAO-UN and others <geonetwork@osgeo.org>
 * SPDX-License-Identifier: GPL-2.0-or-later
 */
package org.geonetwork.metadata;

import io.micrometer.common.util.StringUtils;
import java.util.Optional;
import java.util.StringTokenizer;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.geonetwork.constants.Geonet;
import org.geonetwork.domain.Setting;
import org.geonetwork.domain.SettingKey;
import org.geonetwork.domain.repository.SettingRepository;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.testcontainers.shaded.com.google.common.base.Ascii;
import org.testcontainers.shaded.com.google.common.base.Splitter;
import org.testcontainers.shaded.com.google.common.collect.Iterables;

/** basic support for determining if user is from the intranet. Methods moved here for simplicity or re-usability. */
@Component
@AllArgsConstructor
@Slf4j
public class IntranetHelper {

    private final SettingRepository settingRepository;

    /** Converts an ip x.x.x.x into a long. */
    private long getAddress(String ip) {
        if (ip.trim().equals("?")) {
            return 0;
        } else {
            StringTokenizer st = new StringTokenizer(ip, ".");
            if (!st.hasMoreElements()) {
                return 0;
            }
            long a1 = Integer.parseInt(st.nextToken());
            if (!st.hasMoreElements()) {
                return 0;
            }
            long a2 = Integer.parseInt(st.nextToken());
            if (!st.hasMoreElements()) {
                return 0;
            }
            long a3 = Integer.parseInt(st.nextToken());
            if (!st.hasMoreElements()) {
                return 0;
            }
            long a4 = Integer.parseInt(st.nextToken());
            return a1 << 24 | a2 << 16 | a3 << 8 | a4;
        }
    }

    public String currentRequestIpAddress() {
        var requestAttributes = RequestContextHolder.getRequestAttributes();
        if (requestAttributes instanceof ServletRequestAttributes servletRequestAttributes) {
            var request = servletRequestAttributes.getRequest();
            String ip = request.getRemoteAddr(); // TODO: proxy-ed request?
            return ip;
        }
        return "";
    }

    private boolean isValidIntranetSettings(String[] networkArray, String[] netmaskArray) {
        if (networkArray.length != netmaskArray.length) {
            log.error(
                    Geonet.ACCESS_MANAGER,
                    String.format(
                            "Invalid intranet configuration. Define as many network mask (currently %d) as network ip (currently %d). Check Settings > Intranet.",
                            netmaskArray.length, networkArray.length));
            return false;
        } else {
            return true;
        }
    }

    public boolean isLocalhost(String ip) {
        return ip.startsWith("0:0:0:0:0:0:0:1") || ip.equals("127.0.0.1");
    }

    public boolean isIntranet(String ip) {
        // --- consider IPv4 & IPv6 loopback
        // --- we use 'startsWith' because some addresses can be 0:0:0:0:0:0:0:1%0
        if (isLocalhost(ip)) return true;

        // IPv6 link-local
        String ipv6LinkLocalPrefix = "fe80:";
        if (Ascii.toLowerCase(ip).startsWith(ipv6LinkLocalPrefix)) {
            return true;
        }
        // other IPv6
        else if (ip.indexOf(':') >= 0) {
            return false;
        }

        // IPv4
        Optional<Setting> network = settingRepository.findById(SettingKey.SYSTEM_INTRANET_NETWORK);
        Optional<Setting> netmask = settingRepository.findById(SettingKey.SYSTEM_INTRANET_NETMASK);

        try {
            if (network.isPresent()
                    && netmask.isPresent()
                    && StringUtils.isNotEmpty(network.get().getValue())
                    && StringUtils.isNotEmpty(netmask.get().getValue())) {
                long lAddress = getAddress(Iterables.get(Splitter.on(',').split(ip), 0));
                ;
                String[] networkArray = network.get().getValue().split(SettingKey.SYSTEM_INTRANET_IP_SEPARATOR);
                String[] netmaskArray = netmask.get().getValue().split(SettingKey.SYSTEM_INTRANET_IP_SEPARATOR);

                if (isValidIntranetSettings(networkArray, netmaskArray)) {
                    for (int i = 0; i < networkArray.length; i++) {
                        long lIntranetNet = getAddress(networkArray[i]);
                        long lIntranetMask = getAddress(netmaskArray[i]);
                        if ((lAddress & lIntranetMask) == (lIntranetNet & lIntranetMask)) {
                            return true;
                        }
                    }
                }
                return false;
            }
        } catch (Exception nfe) {
            log.error(Geonet.ACCESS_MANAGER, "isIntranet error: " + nfe.getMessage(), nfe);
        }
        return false;
    }
}
