package com.nduginets.softwaredesign.refactoring;

import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import com.nduginets.softwaredesign.refactoring.servlet.AddProductServlet;
import com.nduginets.softwaredesign.refactoring.servlet.GetProductsServlet;
import com.nduginets.softwaredesign.refactoring.servlet.QueryServlet;

/**
 * @author akirakozov
 */
public class Server {

    public static void main(String[] args) throws Exception {
        if (args == null || args.length != 1) {
            System.err.println("required: int port");
            System.exit(1);
        }
        int port = Integer.parseInt(args[0]);
        Server server = new Server(new DatabaseRequest(), port);
        server.start();
        server.join();
    }

    private final DatabaseRequest databaseRequest;
    private final org.eclipse.jetty.server.Server server;
    private final ServletContextHandler context;

    public Server(DatabaseRequest databaseRequest, int port) {
        this.databaseRequest = databaseRequest;
        this.server = new org.eclipse.jetty.server.Server(port);
        this.context = new ServletContextHandler(ServletContextHandler.SESSIONS);
    }

    /**
     * Initialize all resources, block until service doesn't start
     *
     * @throws Exception if any error occurred
     */
    public void start() throws Exception {
        databaseRequest.createDatabase();
        context.setContextPath("/");
        server.setHandler(context);
        context.addServlet(new ServletHolder(new AddProductServlet(databaseRequest)), "/add-product");
        context.addServlet(new ServletHolder(new GetProductsServlet(databaseRequest)), "/get-products");
        context.addServlet(new ServletHolder(new QueryServlet(databaseRequest)), "/query");
        server.start();
    }


    public void join() throws Exception {
        server.join();
    }

    public void stop() throws Exception {
        server.stop();
    }
}