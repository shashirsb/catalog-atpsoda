/*
 * Copyright (c) 2020 Oracle and/or its affiliates.
 *
 * Licensed under the Universal Permissive License v 1.0 as shown at
 * http://oss.oracle.com/licenses/upl.
 */

package io.helidon.examples.sockshop.catalog;

import java.util.Collection;

import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;

import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.parameters.Parameter;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponses;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON;

/**
 * REST API for {@code /catalog} service.
 */
public interface CatalogApi {
    @GET
    @Produces(APPLICATION_JSON)
    @Operation(summary = "Return the socks that match the specified query parameters")
    Collection<? extends Sock> getSocks(@Parameter(description = "tag identifiers")
           @QueryParam("tags") String tags,
           @Parameter(name = "order", description = "order identifier")
           @QueryParam("order") @DefaultValue("price") String order,
           @Parameter(description = "page number")
           @QueryParam("page") @DefaultValue("1") int pageNum,
           @Parameter(description = "page size")
           @QueryParam("size") @DefaultValue("10") int pageSize);

    @GET
    @Path("size")
    @Produces(APPLICATION_JSON)
    @Operation(summary = "Return sock count for the specified tag identifiers")
    CatalogResource.Count getSockCount(@Parameter(description = "tag identifiers")
                                       @QueryParam("tags") String tags);

    @GET
    @Path("{id}")
    @Produces(APPLICATION_JSON)
    @Operation(summary = "Return socks for the specified sock identifier")
    @APIResponses({
          @APIResponse(responseCode = "200", description = "if socks are found"),
          @APIResponse(responseCode = "404", description = "if socks do not exist")
    })
    Response getSock(@Parameter(description = "sock identifier")
                                @PathParam("id") String sockId);

    @GET
    @Path("images/{image}")
    @Produces("image/jpeg")
    @Operation(summary = "Return the sock images for the specified image identifer")
    @APIResponses({
          @APIResponse(responseCode = "200", description = "if image is found"),
          @APIResponse(responseCode = "404", description = "if image does not exist")
    })
    Response getImage(@Parameter(description = "image identifier")
                      @PathParam("image") String image);
}
