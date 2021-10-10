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
public class QueryServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String command = request.getParameter("command");

        HtmlFormatter formatter = new HtmlFormatter();
        if ("max".equals(command)) {
            Database.executeQuery("SELECT * FROM PRODUCT ORDER BY PRICE DESC LIMIT 1", resultSet -> {
                formatter.printlnToBody("<h1>Product with max price: </h1>");
                try {
                    while (resultSet.next()) {
                        String name = resultSet.getString("name");
                        int price  = resultSet.getInt("price");
                        formatter.printlnToBody(name + "\t" + price + "</br>");
                    }
                } catch (SQLException e) {
                    throw new DatabaseException("Error while reading from table", e);
                }
                return 0;
            });
        } else if ("min".equals(command)) {
            Database.executeQuery("SELECT * FROM PRODUCT ORDER BY PRICE LIMIT 1", resultSet -> {
                formatter.printlnToBody("<h1>Product with min price: </h1>");
                try {
                    while (resultSet.next()) {
                        String name = resultSet.getString("name");
                        int price  = resultSet.getInt("price");
                        formatter.printlnToBody(name + "\t" + price + "</br>");
                    }
                } catch (SQLException e) {
                    throw new DatabaseException("Error while reading from table", e);
                }
                return 0;
            });
        } else if ("sum".equals(command)) {
            Database.executeQuery("SELECT SUM(price) FROM PRODUCT", resultSet -> {
                formatter.printlnToBody("Summary price: ");
                try {
                    if (resultSet.next()) {
                        formatter.printlnToBody(resultSet.getInt(1));
                    }
                } catch (SQLException e) {
                    throw new DatabaseException("Error while reading from table", e);
                }
                return 0;
            });
        } else if ("count".equals(command)) {
            Database.executeQuery("SELECT COUNT(*) FROM PRODUCT", resultSet -> {
                formatter.printlnToBody("Number of products: ");
                try {
                    if (resultSet.next()) {
                        formatter.printlnToBody(resultSet.getInt(1));
                    }
                } catch (SQLException e) {
                    throw new DatabaseException("Error while reading from table", e);
                }
                return 0;
            });
        } else {
            formatter.printlnToBody("Unknown command: " + command);
        }
        formatter.writeToResponse(response);
        response.setStatus(HttpServletResponse.SC_OK);
    }
}
