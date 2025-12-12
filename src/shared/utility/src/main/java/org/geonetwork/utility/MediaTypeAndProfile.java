/*
 * SPDX-FileCopyrightText: 2001 FAO-UN and others <geonetwork@osgeo.org>
 * SPDX-License-Identifier: GPL-2.0-or-later
 */
package org.geonetwork.utility;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.http.MediaType;

/**
 * requests look like "?f=html".
 *
 * <p>This would return { mediaType=MediaType.TEXT_HTML, name = "html" }
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
// NOTE: see the spotbugs excludes for this class (EI_EXPOSE_REP2 and EI_EXPOSE_REP)
public class MediaTypeAndProfile {
    private MediaType mediaType;
    private List<String> profile;
}
