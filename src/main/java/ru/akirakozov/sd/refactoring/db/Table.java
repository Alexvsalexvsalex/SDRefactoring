package ru.akirakozov.sd.refactoring.db;

import java.util.List;

public abstract class Table<T> {
    protected final String name;
    protected final String tableDesc;
    protected final String insertTuple;
    protected final Decoder<T> objectDecoder;

    protected Table(String name, String tableDesc, String insertTuple, Decoder<T> objectDecoder) {
        this.name = name;
        this.tableDesc = tableDesc;
        this.insertTuple = insertTuple;
        this.objectDecoder = objectDecoder;
    }

    public void create() {
        Database.executeUpdate(
                "CREATE TABLE IF NOT EXISTS " + name + tableDesc);
    }

    protected abstract String getObjectInsertTuple(T object);

    public void insert(T object) {
        Database.executeUpdate("INSERT INTO " + name + " " +
                insertTuple + " VALUES " + getObjectInsertTuple(object));
    }

    protected <K> List<K> select(
            String what,
            String orderBy,
            String limit,
            Decoder<K> decoder) {
        return Database.executeQueryAndProcess(
                "SELECT " + what + " FROM " + name + " " + orderBy + " " + limit,
                decoder);
    }

    public int countAll() {
        return select("*", "", "", Decoders.INTEGER_DECODER).get(0);
    }
}
