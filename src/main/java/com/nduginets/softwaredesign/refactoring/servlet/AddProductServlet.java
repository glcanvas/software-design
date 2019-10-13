package com.nduginets.softwaredesign.refactoring.servlet;

import com.nduginets.softwaredesign.refactoring.DatabaseRequest;
import com.nduginets.softwaredesign.refactoring.HttpResponse;

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
            HttpResponse.badRequestResponse(response, "Name & Price required");
            return;
        }
        try {
            price = Long.parseLong(priceString);
        } catch (NumberFormatException ignored) {
            HttpResponse.badRequestResponse(response, "Price must be long");
            return;
        }

        try {
            databaseRequest.addProduct(name, price);
        } catch (Exception ignored) {
            HttpResponse.errorResponse(response, "Can't create product");
            return;
        }
        HttpResponse.createdResponse(response, "OK");
    }
}