package com.nduginets.softwaredesign.refactoring.servlet;

import com.nduginets.softwaredesign.refactoring.DatabaseRequest;
import com.nduginets.softwaredesign.refactoring.HttpResponseHandler;

import java.io.IOException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author akirakozov
 */
public class GetProductsServlet extends AbstractDatabaseServlet {

    public GetProductsServlet(DatabaseRequest request) {
        super(request);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String result;
        try {
            result = LIST_PROCESSED.apply(databaseRequest.getProducts());
        } catch (Exception e) {
            HttpResponseHandler.errorResponse(response, "Can't get products");
            return;
        }
        HttpResponseHandler.okResponse(response, HttpResponseHandler.CREATE_HTML_FROM_S.apply(result));
    }
}