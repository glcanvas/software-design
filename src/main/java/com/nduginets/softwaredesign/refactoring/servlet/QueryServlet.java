package com.nduginets.softwaredesign.refactoring.servlet;

import com.nduginets.softwaredesign.refactoring.DatabaseRequest;
import com.nduginets.softwaredesign.refactoring.HttpResponse;
import com.nduginets.softwaredesign.refactoring.Product;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.*;
import java.util.List;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * @author akirakozov
 */
public class QueryServlet extends AbstractDatabaseServlet {

    public QueryServlet(DatabaseRequest request) {
        super(request);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String command = request.getParameter("command");

        if ("max".equals(command)) {
            executeCommand(command, "<h1>Product with max price: </h1>", databaseRequest::maxProduct, LIST_PROCESSED, response);
        } else if ("min".equals(command)) {
            executeCommand(command, "<h1>Product with min price: </h1>", databaseRequest::minProduct, LIST_PROCESSED, response);
        } else if ("sum".equals(command)) {
            executeCommand(command, "Summary price: ", databaseRequest::sumProduct, SINGLE_PROCESSED, response);
        } else if ("count".equals(command)) {
            executeCommand(command, "Product count: ", databaseRequest::countProduct, SINGLE_PROCESSED, response);
        } else {
            HttpResponse.badRequestResponse(response, "Command: " + command + " not recognized");
        }
    }

    private <T, V> void executeCommand(String cmd, String header, Supplier<T> dbRequest,
                                       Function<T, V> resultSetProcessed,
                                       HttpServletResponse response) throws IOException {
        StringBuilder sb = new StringBuilder();
        sb.append(header);
        try {
            sb.append(resultSetProcessed.apply(dbRequest.get()));
        } catch (RuntimeException e) {
            HttpResponse.errorResponse(response, "Can't execute: " + cmd);
        }
        String body = HttpResponse.CREATE_HTML_FROM_SB.apply(sb);
        HttpResponse.okResponse(response, body);
    }
}
