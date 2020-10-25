/*
 * Copyright (c) 2020 Oracle and/or its affiliates.
 *
 * Licensed under the Universal Permissive License v 1.0 as shown at
 * http://oss.oracle.com/licenses/upl.
 */

package io.helidon.examples.sockshop.catalog.atpsoda;

import io.helidon.examples.sockshop.catalog.CatalogRepository;
import io.helidon.examples.sockshop.catalog.CatalogRepositoryTest;

import io.helidon.examples.sockshop.catalog.atpsoda.AtpSodaProducers;

import static io.helidon.examples.sockshop.catalog.atpsoda.AtpSodaProducers.*;

import org.junit.jupiter.api.Test;

/**
 * Integration tests for {@link io.helidon.examples.sockshop.catalog.mongo.MongoCatalogRepository}.
 */
public class AtpSodaCatalogRepositoryIT {
    @Test
    void dbConnectTest() {
        AtpSodaProducers asp = new AtpSodaProducers();
        asp.dbConnect();
    }
}
