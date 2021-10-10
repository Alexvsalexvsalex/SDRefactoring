package ru.akirakozov.sd.refactoring.servlet;

import ru.akirakozov.sd.refactoring.db.Database;
import ru.akirakozov.sd.refactoring.exceptions.DatabaseException;
import ru.akirakozov.sd.refactoring.pages.HtmlFormatter;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.*;

/**
 * @author akirakozov
 */
public class GetProductsServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        HtmlFormatter formatter = new HtmlFormatter();
        Database.executeQuery("SELECT * FROM PRODUCT ORDER BY PRICE DESC", resultSet -> {
            try {
                while (resultSet.next()) {
                    String  name = resultSet.getString("name");
                    int price  = resultSet.getInt("price");
                    formatter.printlnToBody(name + "\t" + price + "</br>");
                }
            } catch (SQLException e) {
                throw new DatabaseException("Error while reading from table", e);
            }
            return 0;
        });
        formatter.writeToResponse(response);
        response.setStatus(HttpServletResponse.SC_OK);
    }
}
