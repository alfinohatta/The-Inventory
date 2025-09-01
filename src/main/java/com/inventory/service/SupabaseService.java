package com.inventory.service;

import com.inventory.model.Item;
import com.inventory.model.Transaction;
import javax.json.Json;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class SupabaseService {

    private final HttpClient httpClient;
    private final String baseUrl;
    private final String apiKey;

    public SupabaseService(String baseUrl, String apiKey) {
        this.httpClient = HttpClient.newHttpClient();
        this.baseUrl = baseUrl;
        this.apiKey = apiKey;
    }

    public void createItem(Item item) {
        try {
            String endpoint = baseUrl + "/rest/v1/items";
            String requestBody = Json.createObjectBuilder()
                .add("item_code", item.getItemCode())
                .add("name", item.getName())
                .add("category", item.getCategory())
                .add("unit", item.getUnit())
                .add("min_stock", item.getMinStock())
                .add("is_palindrome", item.isPalindrome())
                .build()
                .toString();

            HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(endpoint))
                .header("Content-Type", "application/json")
                .header("apikey", apiKey)
                .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                .build();

            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() != 201) {
                throw new RuntimeException("Failed to create item: " + response.body());
            }
        } catch (Exception e) {
            throw new RuntimeException("Error creating item", e);
        }
    }

    public void updateItem(Item item) {
        try {
            String endpoint = baseUrl + "/rest/v1/items?id=eq." + item.getId();
            String requestBody = Json.createObjectBuilder()
                .add("name", item.getName())
                .add("category", item.getCategory())
                .add("unit", item.getUnit())
                .add("min_stock", item.getMinStock())
                .add("is_palindrome", item.isPalindrome())
                .build()
                .toString();

            HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(endpoint))
                .header("Content-Type", "application/json")
                .header("apikey", apiKey)
                .method("PATCH", HttpRequest.BodyPublishers.ofString(requestBody))
                .build();

            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() != 204) {
                throw new RuntimeException("Failed to update item: " + response.body());
            }
        } catch (Exception e) {
            throw new RuntimeException("Error updating item", e);
        }
    }

    public void deleteItem(UUID itemId) {
        // Implementation for deleting an item in Supabase
    }

    public int getCurrentStock(UUID itemId) {
        // Implementation for fetching current stock from Supabase
        return 0;
    }

    public List<Item> getAllItems() throws Exception {
        // Updated to return List<Item> instead of List<String>
        return new ArrayList<>();
    }

    public void createTransaction(Transaction transaction) {
        // Implementation for creating a transaction in Supabase
    }

    public List<TransactionReportRow> getTransactionReport(LocalDate startDate, LocalDate endDate) {
        // Implementation for fetching transaction reports from Supabase
        return new ArrayList<>();
    }

    public Item getItemById(UUID itemId) {
        // Implementation for fetching an item by ID from Supabase
        return null;
    }

    public static class TransactionReportRow {
        // Define fields and methods for the TransactionReportRow class
    }
}