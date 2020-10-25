/*
 * Copyright (c) 2020 Oracle and/or its affiliates.
 *
 * Licensed under the Universal Permissive License v 1.0 as shown at
 * http://oss.oracle.com/licenses/upl.
 */

package io.helidon.examples.sockshop.catalog;

/**
 * Tests for default in memory repository implementation.
 */
public class DefaultCatalogRepositoryTest extends CatalogRepositoryTest {
    @Override
    protected CatalogRepository getCatalogRepository() {
        return new DefaultCatalogRepository().loadData();
    }
}
