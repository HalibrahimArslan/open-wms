package com.hisarresearch.wms.service;

import org.typesense.api.Client;
import org.typesense.model.Field;
import org.typesense.api.*;
import org.typesense.model.*;
import org.typesense.resources.*;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Arrays;

public class TypeSenseExample {
    public static void main(String[] args) {
        try {
            ArrayList<Node> nodes = new ArrayList<>(
                Arrays.asList(
                    new Node("http", "localhost", "8108")
                )
            );

            Configuration configuration = new Configuration(nodes, Duration.ofSeconds(2), "xyz");

            Client client = new Client(configuration);

            CollectionSchema schema = new CollectionSchema()
                .name("companies")
                .addFieldsItem(new Field().name("company_name").type("string"))
                .addFieldsItem(new Field().name("num_employees").type("int32"))
                .addFieldsItem(new Field().name("country").type("string"));

            client.collections().create(schema);

            System.out.println("Collection created successfully!");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
