package ru.akirakozov.sd.refactoring.db;

import ru.akirakozov.sd.refactoring.exceptions.DatabaseException;

import java.sql.ResultSet;
import java.sql.SQLException;

public abstract class Decoder<T> {
    public T parse(ResultSet resultSet) {
        try {
            return parseImpl(resultSet);
        } catch (SQLException e) {
            throw new DatabaseException("Cannot parse object", e);
        }
    }

    protected abstract T parseImpl(ResultSet resultSet) throws SQLException;
}


