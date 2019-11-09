package com.nduginets.softwaredesign.notes.application;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.atomic.AtomicReference;

public abstract class AbstractApplication implements Application {
    private static final int TIMEOUT_SECONDS = 10;

    private final AtomicReference<Thread> executionRef = new AtomicReference<>();
    private final CompletableFuture<?> startFuture = new CompletableFuture<>();
    private final CompletableFuture<?> stopFuture = new CompletableFuture<>();
    private final CompletableFuture<?> terminateFuture = new CompletableFuture<>();

    @Override
    public CompletableFuture<?> start() {
        if (executionRef.get() == null) {
            Thread t = new Thread(this::run);
            if (executionRef.compareAndSet(null, t)) {
                t.start();
            }
        }
        return startFuture;
    }

    @Override
    public CompletableFuture<?> stop() {
        stopFuture.complete(null);
        return stopFuture;
    }

    @Override
    public void close() {
        stop();
        Thread t = executionRef.get();
        try {
            terminateFuture.get(TIMEOUT_SECONDS, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        } catch (ExecutionException e) {
            stopFuture.completeExceptionally(e);
        } catch (TimeoutException e) {
            t.interrupt();
        }
        System.err.println("exit successfully");
    }

    public abstract void onStart() throws Exception;

    public abstract void onClose() throws Exception;

    private void run() {
        try {
            onStart();
            startFuture.complete(null);
        } catch (Throwable e) {
            e.printStackTrace();
            startFuture.completeExceptionally(e);
            stopFuture.complete(null);
        }

        try {
            stopFuture.get();

        } catch (Throwable e) {
            e.printStackTrace();
            stopFuture.completeExceptionally(e);
        }

        try {
            onClose();
        } catch (Throwable e) {
            terminateFuture.completeExceptionally(e);
            e.printStackTrace();
            return;
        }
        terminateFuture.complete(null);
    }
}
