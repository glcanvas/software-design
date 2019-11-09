package com.nduginets.softwaredesign.notes.application;

import java.util.concurrent.CompletableFuture;

public interface Application extends AutoCloseable {

    CompletableFuture<?> start();

    CompletableFuture<?> stop();
}
