package com.nduginets.softwaredesign.refactoring;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public class DatabaseRequest {
    private static final String DB_ERROR = "Can't invoke database";
    private static final Function<ResultSet, Long> GET_SINGLE_ELEMENT = rs -> {
        try {
            return (long) rs.getInt(1);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    };
    private static final Function<ResultSet, List<Product>> GET_LIST = rs -> {
        try {
            List<Product> products = new ArrayList<>();
            while (rs.next()) {
                String name = rs.getString("name");
                long price = (long) rs.getInt("price");
                products.add(new Product(name, price));
            }
            return products;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    };
    private static final String CREATE_DATABASE = "CREATE TABLE IF NOT EXISTS PRODUCT" +
            "(ID INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL," +
            " NAME           TEXT    NOT NULL, " +
            " PRICE          INT     NOT NULL)";

    private final String databaseName;

    public DatabaseRequest() {
        this.databaseName = "jdbc:sqlite:test.db";
    }

    public void dropProduct() {
        initJdbc();
        String sql = "DROP TABLE PRODUCT";
        executeUpdate(sql);
    }

    public void createDatabase() {
        initJdbc();
        executeUpdate(CREATE_DATABASE);
    }

    public void addProduct(String name, long price) {
        String sql = "INSERT INTO PRODUCT " +
                "(NAME, PRICE) VALUES (\"" + name + "\"," + price + ")";
        executeUpdate(sql);
    }

    public List<Product> getProducts() {
        return executeQuery("SELECT * FROM PRODUCT", GET_LIST);
    }

    public List<Product> maxProduct() {
        return executeQuery("SELECT * FROM PRODUCT ORDER BY PRICE DESC LIMIT 1", GET_LIST);
    }

    public List<Product> minProduct() {
        return executeQuery("SELECT * FROM PRODUCT ORDER BY PRICE LIMIT 1", GET_LIST);
    }

    public Long sumProduct() {
        return executeQuery("SELECT SUM(price) FROM PRODUCT", GET_SINGLE_ELEMENT);
    }

    public Long countProduct() {
        return executeQuery("SELECT COUNT(*) FROM PRODUCT", GET_SINGLE_ELEMENT);
    }

    private void executeUpdate(String sqlRequest) {
        try (Connection c = DriverManager.getConnection(databaseName);
             Statement statement = c.createStatement()) {
            statement.executeUpdate(sqlRequest);
        } catch (SQLException ignored) {
            throw new RuntimeException(DB_ERROR);
        }

    }

    private <T> T executeQuery(String sqlRequest, Function<ResultSet, T> toObject) {
        try (Connection c = DriverManager.getConnection(databaseName);
             Statement statement = c.createStatement();
             ResultSet resultSet = statement.executeQuery(sqlRequest)) {
            return toObject.apply(resultSet);
        } catch (SQLException e) {
            throw new RuntimeException(DB_ERROR);
        }
    }


    private static void initJdbc() {
        try {
            Class.forName("org.sqlite.JDBC");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }


}
