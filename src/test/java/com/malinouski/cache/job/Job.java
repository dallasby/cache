package com.malinouski.cache.job;

import java.util.concurrent.CompletableFuture;

public class Job {
    public CompletableFuture<String> someJob(Long sleepTime) {
        return CompletableFuture.supplyAsync(() -> {
                    System.out.println("Starting job...");
                    return timeForJobCompletion(sleepTime);
                }
        ).exceptionally(ex -> {
            System.err.println("Job exception: " + ex.getMessage());
            return "Job failed";
        });
    }

    private String timeForJobCompletion(Long sleepTime) {
        try {
            System.out.println("Waiting for job completion for " + sleepTime + " ms");
            Thread.sleep(sleepTime);
            return "Job completed";
        } catch (InterruptedException e) {
            throw new RuntimeException("Job interrupted", e);
        }
    }
}
