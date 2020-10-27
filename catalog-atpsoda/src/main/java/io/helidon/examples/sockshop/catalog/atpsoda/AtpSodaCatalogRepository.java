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

    public static OracleDatabase db = null;

    @Inject
    AtpSodaCatalogRepository() {
        try {
            String catalogResponse = createData();
            System.out.println(catalogResponse);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    public Collection < ? extends AtpSodaSock > getSocks(String tags, String order, int pageNum, int pageSize) {
        ArrayList < AtpSodaSock > results = new ArrayList < > ();

        org.json.simple.JSONObject _jsonObject = new JSONObject();
        org.json.simple.parser.JSONParser _parser = new JSONParser();


        try {

            // Get a collection with the name "socks".
            // This creates a database table, also named "socks", to store the collection.
            OracleCollection col = this.db.admin().createCollection("socks");

            // Find all documents in the collection.
            OracleCursor c = null;
            String jsonFormattedString = null;
            try {

                // Find a documents in the collection.
            // OracleDocument filterSpec =
            // this.db.createDocumentFromString("{ \"id\" : \"" + sockId + "\"}");
            //    c = col.find().filter(filterSpec).getCursor();
            tagsFilter(tags);
                c = col.find().getCursor();
                OracleDocument resultDoc;


                while (c.hasNext()) {
                    AtpSodaSock atpSodaSock = new AtpSodaSock();
                    List < String > imageUrlList = new ArrayList < > ();
                    Set < String > tag_Set = new HashSet < String > ();

                    resultDoc = c.next();

                    JSONParser parser = new JSONParser();

                    Object obj = parser.parse(resultDoc.getContentAsString());

                    JSONObject jsonObject = (JSONObject) obj;

                    atpSodaSock.id = jsonObject.get("id").toString();
                    atpSodaSock.name = jsonObject.get("name").toString();
                    atpSodaSock.description = jsonObject.get("description").toString();
                    atpSodaSock.price = Float.parseFloat(jsonObject.get("price").toString());
                    atpSodaSock.count = Integer.parseInt(jsonObject.get("count").toString());

                    JSONArray _jsonArrayimageUrl = (JSONArray) jsonObject.get("imageUrl");

                    for (int i = 0; i < _jsonArrayimageUrl.size(); i++) {
                        imageUrlList.add(_jsonArrayimageUrl.get(i).toString());
                    }

                    JSONArray _jsonArraytag = (JSONArray) jsonObject.get("tag");

                    for (int i = 0; i < _jsonArraytag.size(); i++) {
                        tag_Set.add(_jsonArraytag.get(i).toString());
                    }


                    atpSodaSock.imageUrl = imageUrlList;
                    atpSodaSock.tag = tag_Set;




                    results.add(atpSodaSock);
                }



            } finally {
                // IMPORTANT: YOU MUST CLOSE THE CURSOR TO RELEASE RESOURCES.
                if (c != null) c.close();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        System.out.println("/catalogue.. GET Request 200OK");
        return results;
    }

    @Override
    public AtpSodaSock getSock(String sockId) {
        ArrayList < AtpSodaSock > results = new ArrayList < > ();
        AtpSodaSock atpSodaSock = new AtpSodaSock();

        org.json.simple.JSONObject _jsonObject = new JSONObject();
        org.json.simple.parser.JSONParser _parser = new JSONParser();


        try {


            // Get a collection with the name "socks".
            // This creates a database table, also named "socks", to store the collection.
            OracleCollection col = this.db.admin().createCollection("socks");

            // Find a documents in the collection.
            OracleDocument filterSpec =
                this.db.createDocumentFromString("{ \"id\" : \"" + sockId + "\"}");
            OracleCursor c = col.find().filter(filterSpec).getCursor();
            String jsonFormattedString = null;
            try {
                OracleDocument resultDoc;


                while (c.hasNext()) {

                    List < String > imageUrlList = new ArrayList < > ();
                    Set < String > tag_Set = new HashSet < String > ();

                    resultDoc = c.next();

                    JSONParser parser = new JSONParser();

                    Object obj = parser.parse(resultDoc.getContentAsString());

                    JSONObject jsonObject = (JSONObject) obj;

                    atpSodaSock.id = jsonObject.get("id").toString();
                    atpSodaSock.name = jsonObject.get("name").toString();
                    atpSodaSock.description = jsonObject.get("description").toString();
                    atpSodaSock.price = Float.parseFloat(jsonObject.get("price").toString());
                    atpSodaSock.count = Integer.parseInt(jsonObject.get("count").toString());

                    JSONArray _jsonArrayimageUrl = (JSONArray) jsonObject.get("imageUrl");

                    for (int i = 0; i < _jsonArrayimageUrl.size(); i++) {
                        imageUrlList.add(_jsonArrayimageUrl.get(i).toString());
                    }

                    JSONArray _jsonArraytag = (JSONArray) jsonObject.get("tag");

                    for (int i = 0; i < _jsonArraytag.size(); i++) {
                        tag_Set.add(_jsonArraytag.get(i).toString());
                    }

                    atpSodaSock.imageUrl = imageUrlList;
                    atpSodaSock.tag = tag_Set;


                    results.add(atpSodaSock);
                }



            } finally {
                // IMPORTANT: YOU MUST CLOSE THE CURSOR TO RELEASE RESOURCES.
                if (c != null) c.close();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        System.out.println("/catalogue/" + sockId + ".. GET Request 200OK");
        return atpSodaSock;

    }

    @Override
    public long getSockCount(String tags) {
        long numDocs = 0;
        try {

            // Get a collection with the name "socks".
            // This creates a database table, also named "socks", to store the collection.
            OracleCollection col = this.db.admin().createCollection("socks");
            numDocs = col.find().count();

        } catch (OracleException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("/catalogue/size.. GET Request 200OK");
        return numDocs;
    }

    @Override
    public Set < String > getTags() {
        Set < String > tags = new HashSet < > ();

        org.json.simple.JSONObject _jsonObject = new JSONObject();
        org.json.simple.parser.JSONParser _parser = new JSONParser();


        try {

            // Get a collection with the name "socks".
            // This creates a database table, also named "socks", to store the collection.
            OracleCollection col = this.db.admin().createCollection("socks");

            // Find all documents in the collection.
            OracleCursor c = null;
            String jsonFormattedString = null;
            try {
                c = col.find().getCursor();
                OracleDocument resultDoc;


                while (c.hasNext()) {

                    resultDoc = c.next();

                    JSONParser parser = new JSONParser();

                    Object obj = parser.parse(resultDoc.getContentAsString());

                    JSONObject jsonObject = (JSONObject) obj;


                    JSONArray _jsonArraytag = (JSONArray) jsonObject.get("tag");

                    for (int i = 0; i < _jsonArraytag.size(); i++) {
                        tags.add(_jsonArraytag.get(i).toString());
                    }
                }



            } finally {
                // IMPORTANT: YOU MUST CLOSE THE CURSOR TO RELEASE RESOURCES.
                if (c != null) c.close();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        System.out.println("/tags.. GET Request 200OK");
        return tags;
    }


/**
     * Helper method to create tags filter.
     *
     * @param tags a comma-separated list of tags; can be {@code null}
     *
     * @return a MongoDB filter for the specified tags
     */
    private void tagsFilter(String tags) {
        System.out.println("9.........................." + tags);
        if (tags != null && !"".equals(tags)) {
            System.out.println(Arrays.stream(tags.split(",")));
            List<Bson> filters = Arrays.stream(tags.split(","))
                    .map(tag -> eq("tag", tag))
                    .collect(Collectors.toList());


                   // {"tag" : {"$in" : ["tea", "soda"]}}

            System.out.println("1..........................");
            System.out.println(or(filters).toString());
            System.out.println("2..........................");
        }
        System.out.println("1..........................");
        System.out.println(new BsonDocument().toString());
        System.out.println("2..........................");
    }



public String createData() {
    // Create a collection with the name "MyJSONCollection".
    // This creates a database table, also named "MyJSONCollection", to store the collection.

    String stringToParse = "[{\"count\":115,\"description\":\"For all those leg lovers out there. A perfect example of a swivel chair trained calf. Meticulously trained on a diet of sitting and Pina Coladas. Phwarr...\",\"id\":\"a0a4f044-b040-410d-8ead-4de0446aec7e\",\"imageUrl\":[\"/catalogue/images/bit_of_leg_1.jpeg\",\"/catalogue/images/bit_of_leg_2.jpeg\"],\"name\":\"Nerd leg\",\"price\":7.99,\"tag\":[\"blue\",\"skin\"]},{\"count\":801,\"description\":\"We were not paid to sell this sock. It's just a bit geeky.\",\"id\":\"d3588630-ad8e-49df-bbd7-3167f7efb246\",\"imageUrl\":[\"/catalogue/images/youtube_1.jpeg\",\"/catalogue/images/youtube_2.jpeg\"],\"name\":\"YouTube.sock\",\"price\":10.99,\"tag\":[\"formal\",\"geek\"]},{\"count\":127,\"description\":\"Keep it simple.\",\"id\":\"zzz4f044-b040-410d-8ead-4de0446aec7e\",\"imageUrl\":[\"/catalogue/images/classic.jpg\",\"/catalogue/images/classic2.jpg\"],\"name\":\"Classic\",\"price\":12,\"tag\":[\"green\",\"brown\"]},{\"count\":808,\"description\":\"enim officia aliqua excepteur esse deserunt quis aliquip nostrud anim\",\"id\":\"819e1fbf-8b7e-4f6d-811f-693534916a8b\",\"imageUrl\":[\"/catalogue/images/WAT.jpg\",\"/catalogue/images/WAT2.jpg\"],\"name\":\"Figueroa\",\"price\":14,\"tag\":[\"formal\",\"green\",\"blue\"]},{\"count\":820,\"description\":\"Ready for action. Engineers: be ready to smash that next bug! Be ready, with these super-action-sport-masterpieces. This particular engineer was chased away from the office with a stick.\",\"id\":\"510a0d7e-8e83-4193-b483-e27e09ddc34d\",\"imageUrl\":[\"/catalogue/images/puma_1.jpeg\",\"/catalogue/images/puma_2.jpeg\"],\"name\":\"SuperSport XL\",\"price\":15,\"tag\":[\"formal\",\"black\",\"sport\"]},{\"count\":175,\"description\":\"consequat amet cupidatat minim laborum tempor elit ex consequat in\",\"id\":\"837ab141-399e-4c1f-9abc-bace40296bac\",\"imageUrl\":[\"/catalogue/images/catsocks.jpg\",\"/catalogue/images/catsocks2.jpg\"],\"name\":\"Cat socks\",\"price\":15,\"tag\":[\"formal\",\"green\",\"brown\"]},{\"count\":738,\"description\":\"A mature sock, crossed, with an air of nonchalance.\",\"id\":\"808a2de1-1aaa-4c25-a9b9-6612e8f29a38\",\"imageUrl\":[\"/catalogue/images/cross_1.jpeg\",\"/catalogue/images/cross_2.jpeg\"],\"name\":\"Crossed\",\"price\":17.32,\"tag\":[\"red\",\"formal\",\"blue\",\"action\"]},{\"count\":438,\"description\":\"proident occaecat irure et excepteur labore minim nisi amet irure\",\"id\":\"3395a43e-2d88-40de-b95f-e00e1502085b\",\"imageUrl\":[\"/catalogue/images/colourful_socks.jpg\",\"/catalogue/images/colourful_socks.jpg\"],\"name\":\"Colourful\",\"price\":18,\"tag\":[\"blue\",\"brown\"]},{\"count\":1,\"description\":\"Socks fit for a Messiah. You too can experience walking in water with these special edition beauties. Each hole is lovingly proggled to leave smooth edges. The only sock approved by a higher power.\",\"id\":\"03fef6ac-1896-4ce8-bd69-b798f85c6e0b\",\"imageUrl\":[\"/catalogue/images/holy_1.jpeg\",\"/catalogue/images/holy_2.jpeg\"],\"name\":\"Holy\",\"price\":99.99,\"tag\":[\"magic\",\"action\"]}]";
    try {
        // pass the path to the file as a parameter 
        // String stringToParse = "";
        // stringToParse = new String(Files.readAllBytes(Paths.get("./../"+fileName)));

        JSONParser parser = new JSONParser();
        JSONObject jsonObjects = new JSONObject();
        JSONArray jsonArray = (JSONArray) parser.parse(stringToParse.replace("\\", ""));


        AtpSodaProducers asp = new AtpSodaProducers();
        this.db = asp.dbConnect();

        // Create a collection with the name "MyJSONCollection".
        // This creates a database table, also named "MyJSONCollection", to store the collection.\

        OracleCollection col = this.db.admin().createCollection("socks");

        col.admin().truncate();

        for (int i = 0; i < jsonArray.size(); i++) {

            // Create a JSON document.
            OracleDocument doc =
                this.db.createDocumentFromString(jsonArray.get(i).toString());

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