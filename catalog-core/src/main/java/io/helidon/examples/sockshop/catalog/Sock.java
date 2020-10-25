/*
 * Copyright (c) 2020 Oracle and/or its affiliates.
 *
 * Licensed under the Universal Permissive License v 1.0 as shown at
 * http://oss.oracle.com/licenses/upl.
 */

package io.helidon.examples.sockshop.catalog;

import java.io.Serializable;
import java.util.List;
import java.util.Set;

import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.Id;

import lombok.Data;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

@Data
@Entity
public class Sock implements Serializable {
    /**
     * Product identifier.
     */
    @Id
    @Schema(description = "Product identifier")
    public String id;

    /**
     * Product name.
     */
    @Schema(description = "Product name")
    public String name;

    /**
     * Product description.
     */
    @Schema(description = "Product description")
    public String description;

    /**
     * A list of product image URLs.
     */
    @ElementCollection
    @Schema(description = "A list of product image URLs")
    public List<String> imageUrl;

    /**
     * Product price.
     */
    @Schema(description = "Product price")
    public float price;

    /**
     * Product count.
     */
    @Schema(description = "Product count")
    public int count;

    /**
     * Product tags.
     */
    @ElementCollection
    @Schema(description = "Product tags")
    private Set<String> tag;
}
