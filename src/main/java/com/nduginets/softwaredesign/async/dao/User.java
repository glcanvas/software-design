package com.nduginets.softwaredesign.async.dao;

import org.bson.Document;

import java.util.Objects;

public class User {
    private final int id;
    private final String name;
    private final String login;
    private final String currency;


    public User(Document doc) {
        this(doc.getInteger("id"), doc.getString("name"), doc.getString("login"),
                doc.getString("currency"));
    }

    public User(int id, String name, String login, String currency) {
        this.id = id;
        this.name = name;
        this.login = login;
        this.currency = currency;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getLogin() {
        return login;
    }

    public String getCurrency() {
        return currency;
    }

    public Document toDocument() {
        return new Document()
                .append("id", id)
                .append("name", name)
                .append("login", login)
                .append("currency", currency);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return id == user.id &&
                Objects.equals(name, user.name) &&
                Objects.equals(login, user.login) &&
                Objects.equals(currency, user.currency);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, login, currency);
    }

    @Override
    public String toString() {
        return "{" +
                "\"id\":\"" + id +"\"" +
                ", \"name\":\"'" + name + '\"' +
                ", \"login\":\"" + login + '\"' +
                ", \"currency\":\"" + currency + '\"' +
                '}';
    }
}
