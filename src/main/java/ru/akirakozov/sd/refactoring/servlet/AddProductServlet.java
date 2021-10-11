package ru.akirakozov.sd.refactoring.servlet;

import ru.akirakozov.sd.refactoring.db.ProductTable;
import ru.akirakozov.sd.refactoring.domain.Product;
import ru.akirakozov.sd.refactoring.pages.HtmlFormatter;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author akirakozov
 */
public class AddProductServlet extends HttpServlet {
    private final ProductTable table;

    public AddProductServlet(ProductTable table) {
        this.table = table;
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String name = request.getParameter("name");
        long price = Long.parseLong(request.getParameter("price"));

        table.insert(new Product(name, price));

        HtmlFormatter formatter = new HtmlFormatter();
        formatter.printlnToBody("OK");
        formatter.writeToResponse(response);
        response.setStatus(HttpServletResponse.SC_OK);
    }
}
