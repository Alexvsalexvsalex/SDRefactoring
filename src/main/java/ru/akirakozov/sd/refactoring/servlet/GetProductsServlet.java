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
public class GetProductsServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        HtmlFormatter formatter = new HtmlFormatter();

        List<Product> products = Database.executeQueryAndProcess(
                "SELECT * FROM PRODUCT ORDER BY PRICE DESC",
                Decoders.PRODUCT_DECODER);
        products.forEach(formatter::printlnToBody);

        formatter.writeToResponse(response);
        response.setStatus(HttpServletResponse.SC_OK);
    }
}
