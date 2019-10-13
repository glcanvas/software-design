package com.nduginets.softwaredesign.refactoring;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.function.Function;

public class HttpResponse {
    public static final Function<String, String> CREATE_HTML_FROM_S = values -> String.format("<html><body>\n%s\n</body></html>", values);

    public static final Function<StringBuilder, String> CREATE_HTML_FROM_SB = stringBuilder -> CREATE_HTML_FROM_S.apply(stringBuilder.toString());

    public static void okResponse(HttpServletResponse response, String responseBody) throws IOException {
        writeResponse(response, HttpServletResponse.SC_OK, responseBody);
    }

    public static void createdResponse(HttpServletResponse response, String responseBody) throws IOException {
        writeResponse(response, HttpServletResponse.SC_CREATED, responseBody);
    }

    public static void badRequestResponse(HttpServletResponse response, String responseBody) throws IOException {
        writeResponse(response, HttpServletResponse.SC_BAD_REQUEST, responseBody);
    }


    public static void errorResponse(HttpServletResponse response, String responseBody) throws IOException {
        writeResponse(response, 500, responseBody);
    }

    private static void writeResponse(HttpServletResponse response, int statusCode, String responseBody) throws IOException {
        setHttp(response);
        response.setStatus(statusCode);
        if (responseBody != null) {
            response.getWriter().println(responseBody);
        }
    }

    private static void setHttp(HttpServletResponse response) {
        response.setContentType("text/html");
    }
}
