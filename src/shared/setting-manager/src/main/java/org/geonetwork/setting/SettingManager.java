/*
 * (c) 2003 Open Source Geospatial Foundation - all rights reserved
 * This code is licensed under the GPL 2.0 license,
 * available at the root application directory.
 */
package org.geonetwork.setting;

import java.util.Optional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.geonetwork.constants.Geonet;
import org.geonetwork.domain.Setting;
import org.geonetwork.domain.repository.SettingRepository;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@AllArgsConstructor
public class SettingManager {
    private final SettingRepository settingRepository;

    /**
     * Return a setting by its key
     *
     * @param path eg. system/site/name
     */
    public String getValue(String path) {
        return getValue(path, false);
    }

    public String getValue(Settings.GNSetting setting) {
        return getValue(setting.getName(), setting.isNullable());
    }

    public String getValue(String path, boolean nullable) {

        Optional<Setting> se = settingRepository.findById(path);
        if (!se.isPresent()) {
            // TODO : When a settings is not available in the settings table
            // we end here. It could be relevant to add a list of default
            // settings and populate the settings table when the settings is
            // missing (due to bad migration for example).
            return null;
        }

        String value = se.get().getValue();

        // This case occurs during the application startup, before the encryptor is initialized:
        // value is null and storedValue has the correct value in this case.
        // Affects OpenApiConfig and SettingManager PostConstruct that retrieve settings during the startup,
        // before the encryptor is initialized.
        // TODO: Improve EncryptorInitializer. For now it depends on GeonetworkDataDirectory
        //  that requires to be initialised in GeoNetwork start method.
        /*if (!encryptor.isInitialized()) {
            if (!se.get().isEncrypted()) {
                value = se.get().getStoredValue();
            } else {
                throw new IllegalStateException("Encrypted settings can't be accessed before encryptor is initialized");
            }
        }*/

        if (value == null && !nullable) {
            log.warn(String.format(
                    "  Requested setting with name: %s but null value found. Check the settings table.", path));
        }

        return value;
    }

    /**
     * Get value of a setting as integer
     *
     * @param key The setting key
     * @return The integer value of the setting or null
     */
    public Integer getValueAsInt(String key) {
        String value = getValue(key);
        if (value == null || value.trim().length() == 0) return null;
        return Integer.valueOf(value);
    }

    public Integer getValueAsInt(String key, Integer defaultValue) {
        String value = getValue(key);
        if (value == null || value.trim().length() == 0) return defaultValue;
        return Integer.valueOf(value);
    }

    public String getServerURL() {
        String protocol = getValue(Settings.SYSTEM_SERVER_PROTOCOL);
        String host = getValue(Settings.SYSTEM_SERVER_HOST);
        Integer port = getServerPort();

        StringBuilder sb = new StringBuilder(protocol + "://");

        sb.append(host);

        if (isPortRequired(protocol, port + "")) {
            sb.append(":");
            sb.append(port);
        }

        return sb.toString();
    }

    public Integer getServerPort() {
        String protocol = getValue(Settings.SYSTEM_SERVER_PROTOCOL);

        // some conditional logic to handle the case where there's no port in the settings
        Integer sitePort;

        Integer configuredPort = getValueAsInt(Settings.SYSTEM_SERVER_PORT, -1);
        if (configuredPort != -1) {
            sitePort = configuredPort;
        } else if (protocol != null && protocol.equalsIgnoreCase(Geonet.HttpProtocol.HTTPS)) {
            sitePort = Geonet.DefaultHttpPort.HTTPS;
        } else {
            sitePort = Geonet.DefaultHttpPort.HTTP;
        }

        return sitePort;
    }

    public static boolean isPortRequired(String protocol, String port) {
        if (Geonet.HttpProtocol.HTTP.equals(protocol)
                && String.valueOf(Geonet.DefaultHttpPort.HTTP).equals(port)) {
            return false;
        } else if (Geonet.HttpProtocol.HTTPS.equals(protocol)
                && String.valueOf(Geonet.DefaultHttpPort.HTTPS).equals(port)) {
            return false;
        } else {
            return true;
        }
    }
}
