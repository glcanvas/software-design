package com.nduginets.softwaredesign.async.dao;

import com.mongodb.client.model.IndexOptions;
import com.mongodb.rx.client.MongoClient;
import com.mongodb.rx.client.MongoClients;
import com.mongodb.rx.client.Success;
import rx.Observable;
import rx.Subscription;


import java.time.Instant;

import static com.mongodb.client.model.Filters.eq;
import static com.mongodb.client.model.Filters.regex;
import static com.mongodb.client.model.Indexes.ascending;

public final class AsyncDataBase {
    private static final String DB = "db_" + Instant.now().toString().replace(".", "_");
    private static final String USERS = "users";
    private static final String ITEMS = "items";


    private static AsyncDataBase instance = null;

    private final MongoClient client;

    private AsyncDataBase() {
        client = MongoClients.create("mongodb://localhost:27017");
        client.getDatabase(DB)
                .createCollection(USERS)
                .subscribe();
        client.getDatabase(DB)
                .createCollection(ITEMS)
                .subscribe();

        client.getDatabase(DB)
                .getCollection(USERS)
                .createIndex(ascending("id"), new IndexOptions().unique(true))
                .subscribe();

        client.getDatabase(DB)
                .getCollection(ITEMS)
                .createIndex(ascending("id"), new IndexOptions().unique(true))
                .subscribe();

    }

    public Observable<Success> createUser(User user) {
        return client.getDatabase(DB)
                .getCollection(USERS)
                .insertOne(user.toDocument())
                .map(s -> s);
    }

    public Observable<Success> createItem(Item item) {
        return client.getDatabase(DB)
                .getCollection(ITEMS)
                .insertOne(item.toDocument())
                ;
    }

    public Observable<User> getUsers() {
        return client.getDatabase(DB)
                .getCollection(USERS)
                .find()
                .toObservable()
                .map(User::new);

    }

    public Observable<User> getUserById(int id) {
        return client.getDatabase(DB)
                .getCollection(USERS)
                .find(eq("id", id))
                .toObservable()
                .map(User::new);
    }

    public Observable<Item> getItemsByType(String type) {
        return client.getDatabase(DB)
                .getCollection(ITEMS)
                .find(eq("type", type))
                .toObservable()
                .map(Item::new);
    }

    public Observable<Item> getItems() {
        return client.getDatabase(DB)
                .getCollection(ITEMS)
                .find()
                .toObservable()
                .map(Item::new);
    }

    public Observable<Item> getItemsByName(String name) {
        return client.getDatabase(DB)
                .getCollection(ITEMS)
                .find(eq("name", name))
                .toObservable()
                .map(Item::new);
    }

    public static synchronized AsyncDataBase createInstance() {
        if (instance == null) {
            instance = new AsyncDataBase();
        }
        return instance;
    }

}
