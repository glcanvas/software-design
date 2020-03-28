package com.nduginets.softwaredesign.async.dao;

import org.bson.Document;

import java.util.Objects;

public class Item {

    private final int id;
    private final String type;
    private final String name;
    private final double dollarPrice;

    public Item(int id, String type, String name, double dollarPrice) {
        this.id = id;
        this.type = type;
        this.name = name;
        this.dollarPrice = dollarPrice;
    }

    public Item(Document doc) {
        this(doc.getInteger("id"), doc.getString("type"), doc.getString("name"), doc.getDouble("dollarPrice"));
    }

    public Document toDocument() {
        return new Document()
                .append("id", id)
                .append("type", type)
                .append("name", name)
                .append("dollarPrice", dollarPrice);
    }

    public int getId() {
        return id;
    }

    public String getType() {
        return type;
    }

    public String getName() {
        return name;
    }

    public double getDollarPrice() {
        return dollarPrice;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Item item = (Item) o;
        return id == item.id &&
                Objects.equals(type, item.type) &&
                Objects.equals(name, item.name) &&
                Objects.equals(dollarPrice, item.dollarPrice);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, type, name, dollarPrice);
    }

    @Override
    public String toString() {
        return "{" +
                "\"id\":\"" + id + "\"" +
                ", \"type\":\"" + type + '\"' +
                ", \"name\":\"" + name + '\"' +
                ", \"dollarPrice\":\"" + dollarPrice + "\""+
                '}';
    }
}
