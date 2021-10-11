package ru.akirakozov.sd.refactoring.db;

import ru.akirakozov.sd.refactoring.domain.Product;

import java.util.List;

public class ProductTable extends Table<Product> {
    public ProductTable(String file) {
        super(file, "PRODUCT");
    }

    @Override
    protected String getTableDesc() {
        return "(ID INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL," +
                " NAME           TEXT    NOT NULL, " +
                " PRICE          INT     NOT NULL)";
    }

    @Override
    protected String getInsertTupleTemplate() {
        return null;
    }

    @Override
    protected String getObjectInsertTuple(Product object) {
        return "(" + object.getName() + ", " + object.getPrice() + ")";
    }

    public List<Product> selectAllOrderedByPriceDesc() {
        return select("*", "ORDER BY PRICE DESC", "", Decoders.PRODUCT_DECODER);
    }

    public int getSumPrice() {
        return select("SUM(price)", "", "", Decoders.INTEGER_DECODER).get(0);
    }

    public Product getMaxPriceProduct() {
        return select("*", "ORDER BY PRICE DESC", "LIMIT 1", Decoders.PRODUCT_DECODER).get(0);
    }

    public Product getMinPriceProduct() {
        return select("*", "ORDER BY PRICE", "LIMIT 1", Decoders.PRODUCT_DECODER).get(0);
    }
}
