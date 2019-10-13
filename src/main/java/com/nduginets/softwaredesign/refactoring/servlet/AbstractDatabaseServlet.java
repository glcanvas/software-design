package com.nduginets.softwaredesign.refactoring.servlet;

import com.nduginets.softwaredesign.refactoring.DatabaseRequest;
import com.nduginets.softwaredesign.refactoring.Product;

import javax.servlet.http.HttpServlet;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;

abstract class AbstractDatabaseServlet extends HttpServlet {

    static final Function<Long, Long> SINGLE_PROCESSED = Function.identity();
    static final Function<List<Product>, String> LIST_PROCESSED = productList -> {
        StringBuilder sb = new StringBuilder();
        for (Product p : productList) {
            String name = p.getName();
            long price = p.getPrice();
            sb.append(name).append("\t").append(price).append("</br>");
        }
        return sb.toString();
    };

    final DatabaseRequest databaseRequest;

    AbstractDatabaseServlet(DatabaseRequest databaseRequest) {
        this.databaseRequest = Objects.requireNonNull(databaseRequest, "databaseRequest");
    }
}
