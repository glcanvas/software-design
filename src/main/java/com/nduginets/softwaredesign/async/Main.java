package com.nduginets.softwaredesign.async;

import com.mongodb.async.client.Observable;
import com.mongodb.rx.client.MongoClient;
import com.mongodb.rx.client.MongoClients;
import com.mongodb.rx.client.MongoCollection;
import com.mongodb.rx.client.Success;
import com.nduginets.softwaredesign.async.dao.AsyncDataBase;
import com.nduginets.softwaredesign.async.dao.User;
import org.bson.Document;
import rx.Observer;


public class Main {


    public static void main(String[] args) throws InterruptedException {
        AsyncDataBase db = AsyncDataBase.createInstance();


    }
}
