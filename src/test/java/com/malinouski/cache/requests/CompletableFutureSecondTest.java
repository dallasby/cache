package com.malinouski.cache.requests;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.CompletableFuture;


public class CompletableFutureSecondTest {
    public static void main(String[] args) {
        System.out.println("Starting requests...");
        CompletableFuture<String> request1 = sendingAsyncRequest("request1");
        CompletableFuture<String> request2 = sendingAsyncRequest("request2");
        CompletableFuture<String> request3 = sendingAsyncRequest("request3");

        System.out.println("Completing requests...");

        CompletableFuture<List<String>> allRequests = CompletableFuture
                .allOf(request1, request2, request3)
                .thenApply(v -> {
                    System.err.println("Getting data:");
                    return List.of(request1.join(), request2.join(), request3.join());
                })
                .exceptionally(ex -> {
                    System.err.println("Error: " + ex.getMessage());
                    return Collections.singletonList("Request failed: " + ex.getMessage());
                });

        allRequests.thenAccept(r -> {
            System.out.println("Completed requests: " + r);
            r.forEach(System.out::println);
        }).join();
        System.out.println("All requests have been completed.");
    }

    private static CompletableFuture<String> sendingAsyncRequest(String request) {
        return CompletableFuture.supplyAsync(
                () -> {
                    try {
                        return fetchingData(request);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }).exceptionally(ex -> {
            System.err.println("Request failed: " + ex.getMessage());
            return "Request failed: " + request;
        });
    }

    private static String fetchingData(String data) throws InterruptedException {
        Thread.sleep(3000);
        if ("request3".equals(data)) {
            throw new RuntimeException("Data is empty");
        }
        return "Getting data: " + data;
    }
}
