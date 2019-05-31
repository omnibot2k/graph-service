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

    private Object loagThings() {
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

    public DataFetcher getResultsByQueryDataFetcher() {
        return dataFetchingEnvironment -> {
            String query = dataFetchingEnvironment.getArgument("query");
            return loagThings();
        };
    }
}
