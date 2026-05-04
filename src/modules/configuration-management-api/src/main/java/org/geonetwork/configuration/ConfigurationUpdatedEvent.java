/*
 * SPDX-FileCopyrightText: 2001 FAO-UN and others <geonetwork@osgeo.org>
 * SPDX-License-Identifier: GPL-2.0-or-later
 */
package org.geonetwork.configuration;

/** Event published when a configuration parameter is updated in the database. */
public record ConfigurationUpdatedEvent(AppConfigId id, String newValue) {}
