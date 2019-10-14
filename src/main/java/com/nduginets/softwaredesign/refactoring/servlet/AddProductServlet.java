package com.nduginets.softwaredesign.refactoring.servlet;

import com.nduginets.softwaredesign.refactoring.DatabaseRequest;
import com.nduginets.softwaredesign.refactoring.HttpResponseHandler;

import java.io.IOException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author akirakozov
 */
public class AddProductServlet extends AbstractDatabaseServlet {

    public AddProductServlet(DatabaseRequest request) {
        super(request);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String name = request.getParameter("name");
        String priceString = request.getParameter("price");
        long price;
        if (name == null || priceString == null) {
            HttpResponseHandler.badRequestResponse(response, "Name & Price required");
            return;
        }
        try {
            price = Long.parseLong(priceString);
        } catch (NumberFormatException ignored) {
            HttpResponseHandler.badRequestResponse(response, "Price must be long");
            return;
        }

        try {
            databaseRequest.addProduct(name, price);
        } catch (Exception ignored) {
            HttpResponseHandler.errorResponse(response, "Can't create product");
            return;
        }
        HttpResponseHandler.createdResponse(response, "OK");
    }
}