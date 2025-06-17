package org.geonetwork.ogcapi.service.configuration;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ProfileDefault {
    public String mimetype;
    public String defaultProfile;
}
