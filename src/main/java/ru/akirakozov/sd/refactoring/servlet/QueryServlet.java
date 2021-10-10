package ru.akirakozov.sd.refactoring.servlet;

import ru.akirakozov.sd.refactoring.db.Database;
import ru.akirakozov.sd.refactoring.exceptions.DatabaseException;

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

        PrintWriter responseWriter = response.getWriter();
        if ("max".equals(command)) {
            Database.executeQuery("SELECT * FROM PRODUCT ORDER BY PRICE DESC LIMIT 1", resultSet -> {
                responseWriter.println("<html><body>");
                responseWriter.println("<h1>Product with max price: </h1>");

                try {
                    while (resultSet.next()) {
                        String name = resultSet.getString("name");
                        int price  = resultSet.getInt("price");
                        responseWriter.println(name + "\t" + price + "</br>");
                    }
                } catch (SQLException e) {
                    throw new DatabaseException("Error while reading from table", e);
                }
                responseWriter.println("</body></html>");
                return 0;
            });
        } else if ("min".equals(command)) {
            Database.executeQuery("SELECT * FROM PRODUCT ORDER BY PRICE LIMIT 1", resultSet -> {
                responseWriter.println("<html><body>");
                responseWriter.println("<h1>Product with min price: </h1>");

                try {
                    while (resultSet.next()) {
                        String name = resultSet.getString("name");
                        int price  = resultSet.getInt("price");
                        responseWriter.println(name + "\t" + price + "</br>");
                    }
                } catch (SQLException e) {
                    throw new DatabaseException("Error while reading from table", e);
                }
                responseWriter.println("</body></html>");
                return 0;
            });
        } else if ("sum".equals(command)) {
            Database.executeQuery("SELECT SUM(price) FROM PRODUCT", resultSet -> {
                responseWriter.println("<html><body>");
                responseWriter.println("Summary price: ");

                try {
                    if (resultSet.next()) {
                        responseWriter.println(resultSet.getInt(1));
                    }
                } catch (SQLException e) {
                    throw new DatabaseException("Error while reading from table", e);
                }
                responseWriter.println("</body></html>");
                return 0;
            });
        } else if ("count".equals(command)) {
            Database.executeQuery("SELECT COUNT(*) FROM PRODUCT", resultSet -> {
                responseWriter.println("<html><body>");
                responseWriter.println("Number of products: ");

                try {
                    if (resultSet.next()) {
                        responseWriter.println(resultSet.getInt(1));
                    }
                } catch (SQLException e) {
                    throw new DatabaseException("Error while reading from table", e);
                }
                responseWriter.println("</body></html>");
                return 0;
            });
        } else {
            responseWriter.println("Unknown command: " + command);
        }

        response.setContentType("text/html");
        response.setStatus(HttpServletResponse.SC_OK);
    }

}
