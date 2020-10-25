/*
 * Copyright (c) 2020 Oracle and/or its affiliates.
 *
 * Licensed under the Universal Permissive License v 1.0 as shown at
 * http://oss.oracle.com/licenses/upl.
 */

package io.helidon.examples.sockshop.catalog.atpsoda;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Specializes;
import javax.inject.Inject;

import io.helidon.examples.sockshop.catalog.CatalogRepository;
import io.helidon.examples.sockshop.catalog.DefaultCatalogRepository;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Indexes;
import com.mongodb.client.model.Sorts;
import org.bson.BsonDocument;
import org.bson.conversions.Bson;
import org.eclipse.microprofile.opentracing.Traced;

import io.helidon.examples.sockshop.catalog.atpsoda.AtpSodaProducers;

import static com.mongodb.client.model.Filters.eq;
import static com.mongodb.client.model.Filters.or;

///////////////////////

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonArray;

import java.nio.file.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;


import io.helidon.config.Config;
import io.helidon.webserver.Routing;
import io.helidon.webserver.ServerRequest;
import io.helidon.webserver.ServerResponse;
import io.helidon.webserver.Service;

import java.io.*;
import java.util.Properties;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.HashMap;
import java.util.stream.Stream;



import oracle.soda.rdbms.OracleRDBMSClient;
import oracle.soda.OracleDatabase;
import oracle.soda.OracleCursor;
import oracle.soda.OracleCollection;
import oracle.soda.OracleDocument;
import oracle.soda.OracleException;

import org.json.simple.JSONObject;
import org.json.simple.JSONArray;
import org.json.simple.parser.ParseException;
import org.json.simple.parser.JSONParser;



/**
 * An implementation of {@link io.helidon.examples.sockshop.catalog.CatalogRepository}
 * that that uses MongoDB as a backend data store.
 */
@ApplicationScoped
@Specializes
@Traced
public class AtpSodaCatalogRepository extends DefaultCatalogRepository {

    // private MongoCollection<MongoSock> socks;

    // @Inject
    // MongoCatalogRepository(MongoCollection<MongoSock> socks) {
    //     this.socks = socks;
    // }

    // @PostConstruct
    // void init() {
    //     loadData();
    //     socks.createIndex(Indexes.hashed("id"));
    // }

    @PostConstruct
    void init() {
        try {
            String catalogResponse = createData("catalog-docs.json");
            System.out.println(catalogResponse);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public Collection < ? extends AtpSodaSock > getSocks(String tags, String order, int pageNum, int pageSize) {
        ArrayList <AtpSodaSock> results = new ArrayList <> ();
        AtpSodaSock atpSodaSock = new AtpSodaSock();
        List <String> imageUrlList = new ArrayList <> ();

        imageUrlList.add("/catalogue/images/bit_of_leg_1.jpeg");
        imageUrlList.add("/catalogue/images/bit_of_leg_2.jpeg");

        Set <String> tag_Set = new HashSet <String> ();

        tag_Set.add("blue");
        tag_Set.add("skin");

        atpSodaSock.id = "0a4f044-b040-410d-8ead-4de0446aec7e";
        atpSodaSock.name = "ssssssssssssssssssssssssss";
        atpSodaSock.description = "sssssssssssssssssssssssssssssss";
        atpSodaSock.imageUrl = imageUrlList;
        atpSodaSock.price = 7.99f;
        atpSodaSock.count = 115;
        atpSodaSock.tag = tag_Set;

        results.add(atpSodaSock);
        return results;
    }

    @Override
    public AtpSodaSock getSock(String sockId) { 
        ArrayList <AtpSodaSock> results = new ArrayList <> ();
        AtpSodaSock atpSodaSock = new AtpSodaSock();
        List <String> imageUrlList = new ArrayList <> ();

        imageUrlList.add("/catalogue/images/bit_of_leg_1.jpeg");
        imageUrlList.add("/catalogue/images/bit_of_leg_2.jpeg");

        Set <String> tag_Set = new HashSet <String> ();

        tag_Set.add("blue");
        tag_Set.add("skin");

        atpSodaSock.id = "0a4f044-b040-410d-8ead-4de0446aec7e";
        atpSodaSock.name = "ssssssssssssssssssssssssss";
        atpSodaSock.description = "sssssssssssssssssssssssssssssss";
        atpSodaSock.imageUrl = imageUrlList;
        atpSodaSock.price = 7.99f;
        atpSodaSock.count = 115;
        atpSodaSock.tag = tag_Set;

        results.add(atpSodaSock);
        return atpSodaSock;

    }

    @Override
    public long getSockCount(String tags) {
        long numDocs = 0;
        try {

            AtpSodaProducers asp = new AtpSodaProducers();
            OracleDatabase db = asp.dbConnect();
            // Get a collection with the name "socks".
            // This creates a database table, also named "socks", to store the collection.
            OracleCollection col = db.admin().createCollection("socks");
            numDocs = col.find().count();

        } catch (OracleException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return numDocs;
    }

    @Override
    public Set<String> getTags() {
        Set<String> tags = new HashSet<>();
        tags.add("blue");
        tags.add("skin");
        // socks.distinct("tag", String.class)
        //         .forEach((Consumer<? super String>) tags::add);
        return tags;
    }

    // @Override
    // public CatalogRepository loadData() {
    //     if (this.socks.countDocuments() == 0) {
    //         this.socks.insertMany(loadSocksFromJson(AtpSodaSock.class));
    //     }
    //     return this;
    // }

    // /**
    //  * Helper method to create tags filter.
    //  *
    //  * @param tags a comma-separated list of tags; can be {@code null}
    //  *
    //  * @return a MongoDB filter for the specified tags
    //  */
    // private Bson tagsFilter(String tags) {
    //     if (tags != null && !"".equals(tags)) {
    //         List<Bson> filters = Arrays.stream(tags.split(","))
    //                 .map(tag -> eq("tag", tag))
    //                 .collect(Collectors.toList());
    //         return or(filters);
    //     }
    //     return new BsonDocument();
    // }


    public String createData(String fileName) {
        // Create a collection with the name "MyJSONCollection".
        // This creates a database table, also named "MyJSONCollection", to store the collection.
        try {
            // pass the path to the file as a parameter 
            String stringToParse = "";
            stringToParse = new String(Files.readAllBytes(Paths.get(fileName)));

            JSONParser parser = new JSONParser();
            JSONObject jsonObjects = new JSONObject();
            JSONArray jsonArray = (JSONArray) parser.parse(stringToParse);


            AtpSodaProducers asp = new AtpSodaProducers();
            OracleDatabase db = asp.dbConnect();

            // Create a collection with the name "MyJSONCollection".
            // This creates a database table, also named "MyJSONCollection", to store the collection.
            OracleCollection col = db.admin().createCollection("socks");

            for (int i = 0; i < jsonArray.size(); i++) {

                // Create a JSON document.
                OracleDocument doc =
                    db.createDocumentFromString(jsonArray.get(i).toString());

                // Insert the document into a collection.
                col.insert(doc);

            }           

        } catch (OracleException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "successfully created socks collection !!!";
    }
}