/*
 * SPDX-FileCopyrightText: 2001 FAO-UN and others <geonetwork@osgeo.org>
 * SPDX-License-Identifier: GPL-2.0-or-later
 */
package org.geonetwork.process.manager;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.util.List;
import org.springframework.batch.core.Job;

public interface IProcess {
    String getName();

    List<ProcessParameter> getParameters();

    /** Spring Batch */
    @JsonIgnore
    Job getJob();
}
