package ru.akirakozov.sd.refactoring;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import ru.akirakozov.sd.refactoring.db.ProductTable;
import ru.akirakozov.sd.refactoring.exceptions.ServerException;
import ru.akirakozov.sd.refactoring.servlet.AddProductServlet;
import ru.akirakozov.sd.refactoring.servlet.GetProductsServlet;
import ru.akirakozov.sd.refactoring.servlet.QueryServlet;

/**
 * @author akirakozov
 */
public class Main {
    public static final int PORT = 8081;

    public static void main(String[] args) throws InterruptedException {
        ProductTable table = new ProductTable("test.db");
        upServer(table).join();
    }

    // public for tests
    public static Server upServer(ProductTable table) {
        table.create();

        Server server = new Server(PORT);

        ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
        context.setContextPath("/");
        server.setHandler(context);

        context.addServlet(new ServletHolder(new AddProductServlet(table)), "/add-product");
        context.addServlet(new ServletHolder(new GetProductsServlet(table)),"/get-products");
        context.addServlet(new ServletHolder(new QueryServlet(table)),"/query");

        try {
            server.start();
        } catch (Exception e) {
            throw new ServerException("Error while starting server", e);
        }
        return server;
    }
}
