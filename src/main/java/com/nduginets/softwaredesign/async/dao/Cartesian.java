package com.nduginets.softwaredesign.async.dao;

import com.nduginets.softwaredesign.async.CurrencyMapper;

import java.util.Objects;

public class Cartesian {

    private final User user;
    private final Item item;

    private final double price;

    public Cartesian(User user, Item item) {
        this.user = user;
        this.item = item;
        this.price = CurrencyMapper.map(user.getCurrency(), item.getDollarPrice());
    }

    public User getUser() {
        return user;
    }

    public Item getItem() {
        return item;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Cartesian cartesian = (Cartesian) o;
        return Objects.equals(user, cartesian.user) &&
                Objects.equals(item, cartesian.item);
    }

    @Override
    public int hashCode() {
        return Objects.hash(user, item);
    }

    @Override
    public String toString() {
        return "{" +
                "\"user\":" + user +
                ", \"item\":" + item +
                ", \"price\":\"" + price + "\"" +
                '}';
    }
}
