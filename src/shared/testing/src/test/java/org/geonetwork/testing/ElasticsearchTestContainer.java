/*
 * SPDX-FileCopyrightText: 2001 FAO-UN and others <geonetwork@osgeo.org>
 * SPDX-License-Identifier: GPL-2.0-or-later
 */
package org.geonetwork.testing;

import org.testcontainers.elasticsearch.ElasticsearchContainer;

public class ElasticsearchTestContainer extends ElasticsearchContainer {
    private static final String DOCKER_ELASTIC = "docker.elastic.co/elasticsearch/elasticsearch:8.14.3";

    private static final String CLUSTER_NAME = "gn5-cluster";

    private static final String ELASTIC_SEARCH = "gn5-elasticsearch";

    public ElasticsearchTestContainer() {
        super(DOCKER_ELASTIC);
        this.addFixedExposedPort(9211, 9200);
        this.addFixedExposedPort(9311, 9300);
        this.addEnv(CLUSTER_NAME, ELASTIC_SEARCH);
    }
}
