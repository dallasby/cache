package com.malinouski.cache.requests;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

public class CompletableFutureTest {
    public static void main(String[] args) throws ExecutionException, InterruptedException {
        CompletableFuture<String> request1 = CompletableFuture.supplyAsync(
                () -> {
                    try {
                        return fetchingData("request1");
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }).exceptionally(ex->{
            System.err.println("Request1 failed: " + ex.getMessage());
                    return "Request1 failed";
        });
        System.out.println(request1.get());

        CompletableFuture<String> request2 = CompletableFuture.supplyAsync(
                () -> {
                    try {
                        return fetchingData("request2");
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }).exceptionally(ex->{
            System.err.println("Request2 failed: " + ex.getMessage());
            return "Request2 failed";
        });
        System.out.println(request2.get());

        CompletableFuture<String> request3 = CompletableFuture.supplyAsync(
                () -> {
                    try {
                        return fetchingData("request3");
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }).exceptionally(ex->{
            System.err.println("Request3 failed: " + ex.getMessage());
            return "Request3 failed";
        });
        System.out.println(request3.get());

        CompletableFuture<List<String>> results = CompletableFuture.allOf(request1, request2, request3)
                .thenApply(v -> List.of(request1.join(), request2.join(), request3.join()));

        results.thenAccept(r -> r.forEach(System.out::println));
    }

    private static String fetchingData(String data) throws InterruptedException {
        Thread.sleep(3000);
        if ("request2".equals(data)) {
            throw new RuntimeException("Data is empty");
        }
        return "Getting data: " + data;
    }
}
