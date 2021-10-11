package ru.akirakozov.sd.refactoring.servlet;

import ru.akirakozov.sd.refactoring.db.Database;
import ru.akirakozov.sd.refactoring.db.Databases;
import ru.akirakozov.sd.refactoring.db.ProductDatabase;
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

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String name = request.getParameter("name");
        long price = Long.parseLong(request.getParameter("price"));

        Databases.PRODUCT_DATABASE.insert(new Product(name, price));

        HtmlFormatter formatter = new HtmlFormatter();
        formatter.printlnToBody("OK");
        formatter.writeToResponse(response);
        response.setStatus(HttpServletResponse.SC_OK);
    }
}
