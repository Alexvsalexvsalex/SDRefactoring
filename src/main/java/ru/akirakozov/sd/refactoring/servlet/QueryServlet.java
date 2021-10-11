package ru.akirakozov.sd.refactoring.servlet;

import ru.akirakozov.sd.refactoring.db.Databases;
import ru.akirakozov.sd.refactoring.pages.HtmlFormatter;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author akirakozov
 */
public class QueryServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String command = request.getParameter("command");

        HtmlFormatter formatter = new HtmlFormatter();
        switch (command) {
            case "max": {
                formatter.printlnToBody("<h1>Product with max price: </h1>");
                formatter.printlnToBody(Databases.PRODUCT_DATABASE.getMaxPriceProduct());
                break;
            }
            case "min": {
                formatter.printlnToBody("<h1>Product with min price: </h1>");
                formatter.printlnToBody(Databases.PRODUCT_DATABASE.getMinPriceProduct());
                break;
            }
            case "sum": {
                formatter.printlnToBody("Summary price: ");
                formatter.printlnToBody(Databases.PRODUCT_DATABASE.getSumPrice());
                break;
            }
            case "count": {
                formatter.printlnToBody("Number of products: ");
                formatter.printlnToBody(Databases.PRODUCT_DATABASE.countAll());
                break;
            }
            default:
                formatter.printlnToBody("Unknown command: " + command);
        }
        formatter.writeToResponse(response);
        response.setStatus(HttpServletResponse.SC_OK);
    }
}
