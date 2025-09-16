package com.projects.util;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.InputStream;

public class TestDataLoader {
    private static JsonNode rootNode;

    static {
        try (InputStream is = TestDataLoader.class.getResourceAsStream("/testdata/testdata.json")) {
            if (is == null) {
                throw new IllegalStateException("❌ testdata/testdata.json not found in resources");
            }
            ObjectMapper mapper = new ObjectMapper();
            rootNode = mapper.readTree(is);
        } catch (Exception e) {
            throw new RuntimeException("❌ Failed to load testdata.json", e);
        }
    }

    public static User getUser(String key) {
        JsonNode node = rootNode.path("users").path(key);
        return new User(node.get("username").asText(), node.get("password").asText());
    }

    public static String getProduct(String key) {
        return rootNode.path("products").path(key).asText();
    }

    public static CheckoutInfo getCheckoutInfo(String type) {
        JsonNode node = rootNode.path("checkout").path(type);
        return new CheckoutInfo(
                node.get("firstName").asText(),
                node.get("lastName").asText(),
                node.get("postalCode").asText()
        );
    }

    public static String getErrorMessage(String key) {
        return rootNode.path("errors").path(key).asText();
    }
}
