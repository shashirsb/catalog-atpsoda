/*
 * Copyright (c) 2020 Oracle and/or its affiliates.
 *
 * Licensed under the Universal Permissive License v 1.0 as shown at
 * http://oss.oracle.com/licenses/upl.
 */

package io.helidon.examples.sockshop.catalog;

import java.util.Set;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.Path;

/**
 * Implementation of the Catalog Service {@code /tags} API.
 */
@ApplicationScoped
@Path("/tags")
public class TagsResource implements TagApi {

    @Inject
    private CatalogRepository catalog;

    @Override
    public Tags getTags() {
        return new Tags(catalog.getTags());
    }

    public static class Tags {
        public Set<String> tags;
        public Object err;

        Tags(Set<String> tags) {
            this.tags = tags;
        }
    }
}
