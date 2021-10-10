package ru.akirakozov.sd.refactoring.servlet;

import ru.akirakozov.sd.refactoring.db.Database;
import ru.akirakozov.sd.refactoring.db.Decoders;
import ru.akirakozov.sd.refactoring.domain.Product;
import ru.akirakozov.sd.refactoring.pages.HtmlFormatter;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * @author akirakozov
 */
public class QueryServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String command = request.getParameter("command");

        HtmlFormatter formatter = new HtmlFormatter();
        if ("max".equals(command)) {
            formatter.printlnToBody("<h1>Product with max price: </h1>");
            List<Product> products = Database.executeQueryAndProcess(
                    "SELECT * FROM PRODUCT ORDER BY PRICE DESC LIMIT 1",
                    Decoders.PRODUCT_DECODER);
            products.forEach(formatter::printlnToBody);
        } else if ("min".equals(command)) {
            formatter.printlnToBody("<h1>Product with min price: </h1>");
            List<Product> products = Database.executeQueryAndProcess(
                    "SELECT * FROM PRODUCT ORDER BY PRICE LIMIT 1",
                    Decoders.PRODUCT_DECODER);
            products.forEach(formatter::printlnToBody);
        } else if ("sum".equals(command)) {
            formatter.printlnToBody("Summary price: ");
            List<Integer> price = Database.executeQueryAndProcess(
                    "SELECT SUM(price) FROM PRODUCT",
                    Decoders.INTEGER_DECODER);
            price.forEach(formatter::printlnToBody);
        } else if ("count".equals(command)) {
            formatter.printlnToBody("Number of products: ");
            List<Integer> price = Database.executeQueryAndProcess(
                    "SELECT COUNT(*) FROM PRODUCT",
                    Decoders.INTEGER_DECODER);
            price.forEach(formatter::printlnToBody);
        } else {
            formatter.printlnToBody("Unknown command: " + command);
        }
        formatter.writeToResponse(response);
        response.setStatus(HttpServletResponse.SC_OK);
    }
}
