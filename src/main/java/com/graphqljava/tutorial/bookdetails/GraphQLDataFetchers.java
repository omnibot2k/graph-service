package com.graphqljava.tutorial.bookdetails;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import graphql.schema.DataFetcher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

@Component
public class GraphQLDataFetchers {

    @Autowired
    ObjectMapper mapper;

    private String readThingsAsString() {
        try {
            return new String(Files.readAllBytes(Paths.get("./src/main/resources/things-resp.json")));
        } catch (IOException e) {
            e.printStackTrace();
        }

        return "bad";
    }

    private String readSimpleAsString() {
        try {
            return new String(Files.readAllBytes(Paths.get("./src/main/resources/simple.json")));
        } catch (IOException e) {
            e.printStackTrace();
        }

        return "bad";
    }

    private Object loagThings() {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        String json = readThingsAsString();

        try {
            Map<String, Object> jsonMap = mapper.readValue(json,
                    new TypeReference<Map<String, Object>>() {
                    });
            return jsonMap;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "bad";
    }

    private Object loagSimple() {
        String json = readSimpleAsString();

        try {
            Map<String, Object> jsonMap = mapper.readValue(json,
                    new TypeReference<Map<String, Object>>() {
                    });
            return jsonMap;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "bad";
    }

    public DataFetcher getResultsByQueryDataFetcher() {
        //sync
        /*
        return dataFetchingEnvironment -> {
            String query = dataFetchingEnvironment.getArgument("query");
            return loagThings();
        };*/

        //async much faster!!!
        return environment -> CompletableFuture.supplyAsync(
                () -> loagThings());
    }
    public DataFetcher getSimpleQueryDataFetcher() {
        return dataFetchingEnvironment -> {
            String query = dataFetchingEnvironment.getArgument("query");
            return loagSimple();
        };
    }

    public DataFetcher getResponseTestFetcher() {
        return dataFetchingEnvironment -> {
            String query = dataFetchingEnvironment.getArgument("query");
            return "WOW IT WORKED";
        };
    }
}
