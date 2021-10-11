package ru.akirakozov.sd.refactoring.db;

import java.util.List;

public abstract class Table<T> {
    protected final String name;
    private final Database database;

    protected Table(Database database, String name) {
        this.database = database;
        this.name = name;
    }

    protected Table(String file, String name) {
        this(new Database(file), name);
    }

    protected abstract String getTableDesc();

    public void create() {
        database.executeUpdate(
                "CREATE TABLE IF NOT EXISTS " + name + getTableDesc());
    }

    protected abstract String getInsertTupleTemplate();

    protected abstract String getObjectInsertTuple(T object);

    public void insert(T object) {
        database.executeUpdate("INSERT INTO " + name + " " +
                getInsertTupleTemplate() + " VALUES " + getObjectInsertTuple(object));
    }

    protected <K> List<K> select(
            String what,
            String orderBy,
            String limit,
            Decoder<K> decoder) {
        return database.executeQueryAndProcess(
                "SELECT " + what + " FROM " + name + " " + orderBy + " " + limit,
                decoder);
    }

    public int countAll() {
        return select("COUNT(*)", "", "", Decoders.INTEGER_DECODER).get(0);
    }
}
