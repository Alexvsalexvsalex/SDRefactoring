package ru.akirakozov.sd.refactoring.servlet;

import ru.akirakozov.sd.refactoring.db.ProductTable;
import ru.akirakozov.sd.refactoring.domain.Product;
import ru.akirakozov.sd.refactoring.pages.HtmlFormatter;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;

/**
 * @author akirakozov
 */
public class QueryServlet extends HttpServlet {
    private final ProductTable table;

    public QueryServlet(ProductTable table) {
        this.table = table;
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String command = request.getParameter("command");

        HtmlFormatter formatter = new HtmlFormatter();
        switch (command) {
            case "max": {
                formatter.printlnToBody("<h1>Product with max price: </h1>");
                Optional<Product> product = table.getMaxPriceProduct();
                if (product.isPresent()) {
                    formatter.printlnToBody(product.get().getName());
                } else {
                    formatter.printlnToBody("None");
                }
                break;
            }
            case "min": {
                formatter.printlnToBody("<h1>Product with min price: </h1>");
                Optional<Product> product = table.getMinPriceProduct();
                if (product.isPresent()) {
                    formatter.printlnToBody(product.get().getName());
                } else {
                    formatter.printlnToBody("None");
                }
                break;
            }
            case "sum": {
                formatter.printlnToBody("Summary price: ");
                formatter.printlnToBody(table.getSumPrice());
                break;
            }
            case "count": {
                formatter.printlnToBody("Number of products: ");
                formatter.printlnToBody(table.countAll());
                break;
            }
            default:
                formatter.printlnToBody("Unknown command: " + command);
        }
        formatter.writeToResponse(response);
        response.setStatus(HttpServletResponse.SC_OK);
    }
}
