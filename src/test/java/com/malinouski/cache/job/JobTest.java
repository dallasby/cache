package com.malinouski.cache.job;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

public class JobTest {
    public static void main(String[] args) {
        Long sleepTime = TimeUnit.SECONDS.toMillis(10);
        Job job = new Job();

        CompletableFuture<String> jobAsync = job.someJob(sleepTime)
                .completeOnTimeout("Pause", 10, TimeUnit.SECONDS);

        System.out.println("Waiting for job to complete...");
        String jobResult = jobAsync.join();
        System.out.println("Job result: " + jobResult);
    }
}
