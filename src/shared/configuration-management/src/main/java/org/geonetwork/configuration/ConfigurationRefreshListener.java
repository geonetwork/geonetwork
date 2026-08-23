/*
 * SPDX-FileCopyrightText: 2001 FAO-UN and others <geonetwork@osgeo.org>
 * SPDX-License-Identifier: GPL-2.0-or-later
 */
package org.geonetwork.configuration;

import java.util.Set;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.context.refresh.ContextRefresher;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

/** Listens for configuration updates and refreshes the Spring Context after the transaction commits. */
@Component
@RequiredArgsConstructor
@Slf4j
public class ConfigurationRefreshListener {

    private final ContextRefresher contextRefresher;

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void onConfigurationUpdated(ConfigurationUpdatedEvent event) {
        log.info("Configuration updated for {}. Refreshing Spring Context...", event.id());
        Set<String> refreshedKeys = contextRefresher.refresh();
        log.info("Context refreshed. Updated keys: {}", refreshedKeys);
    }
}
