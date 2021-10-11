package ru.akirakozov.sd.refactoring.servlet;

import ru.akirakozov.sd.refactoring.db.ProductTable;
import ru.akirakozov.sd.refactoring.pages.HtmlFormatter;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author akirakozov
 */
public class GetProductsServlet extends HttpServlet {
    private final ProductTable table;

    public GetProductsServlet(ProductTable table) {
        this.table = table;
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        HtmlFormatter formatter = new HtmlFormatter();

        table.selectAllOrderedByPriceDesc().forEach(formatter::printlnToBody);

        formatter.writeToResponse(response);
        response.setStatus(HttpServletResponse.SC_OK);
    }
}
