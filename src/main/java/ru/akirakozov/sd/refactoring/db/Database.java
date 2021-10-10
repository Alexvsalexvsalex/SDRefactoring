package ru.akirakozov.sd.refactoring.db;

import ru.akirakozov.sd.refactoring.exceptions.DatabaseException;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public class Database {
    private static <T> T withDatabaseConnection(Function<Connection, T> function) {
        try (Connection c = DriverManager.getConnection("jdbc:sqlite:test.db")) {
            return function.apply(c);
        } catch (SQLException e) {
            throw new DatabaseException("Cannot create connection", e);
        }
    }

    private static <T> T withStatement(Connection connection,
                                Function<Statement, T> function) {
        try (Statement stmt = connection.createStatement()) {
            return function.apply(stmt);
        } catch (SQLException e) {
            throw new DatabaseException("Cannot create statement", e);
        }
    }

    private static <T> T execute(Function<Statement, T> function) {
        return withDatabaseConnection(c -> withStatement(c, function));
    }

    public static <T> T executeQuery(String query,
                              Function<ResultSet, T> function) {
        return execute(
                s -> {
                    try (ResultSet rs = s.executeQuery(query)) {
                        return function.apply(rs);
                    } catch (SQLException e) {
                        throw new DatabaseException("Cannot execute query", e);
                    }
                }
        );
    }

    public static int executeUpdate(String query) {
        return execute(
                s -> {
                    try {
                        return s.executeUpdate(query);
                    } catch (SQLException e) {
                        throw new DatabaseException("Cannot execute update", e);
                    }
                }
        );
    }

    public static <T> List<T> executeQueryAndProcess(String query,
                                           Decoder<T> decoder) {
        return executeQuery(query, resultSet -> {
            try {
                List<T> result = new ArrayList<>();
                while (resultSet.next()) {
                    result.add(decoder.parse(resultSet));
                }
                return result;
            } catch (SQLException e) {
                throw new DatabaseException("Error while reading from table", e);
            }
        });
    }
}
