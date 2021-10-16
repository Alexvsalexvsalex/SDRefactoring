package ru.akirakozov.sd.refactoring.db;

import ru.akirakozov.sd.refactoring.domain.Product;

import java.util.List;
import java.util.Optional;

public class ProductTable extends Table<Product> {
    public ProductTable(String file) {
        super(file, "PRODUCT");
    }

    ProductTable(Database db) {
        super(db, "PRODUCT");
    }

    @Override
    protected String getTableDesc() {
        return "(ID INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL," +
                " NAME           TEXT    NOT NULL, " +
                " PRICE          INT     NOT NULL)";
    }

    protected List<String> getColumnsList() {
        return List.of(
                "NAME",
                "PRICE");
    }

    protected List<String> extractObject(Product object) {
        return List.of(
                asString(object.getName()),
                asRaw(object.getPrice()));
    }

    public List<Product> selectAllOrderedByPriceDesc() {
        return select("*", "ORDER BY PRICE DESC", "", Decoders.PRODUCT_DECODER);
    }

    public int getSumPrice() {
        return getFirstOptional(select("SUM(price)", "", "", Decoders.INTEGER_DECODER)).orElse(0);
    }

    private <K> Optional<K> getFirstOptional(List<K> list) {
        if (list == null || list.isEmpty()) {
            return Optional.empty();
        } else {
            return Optional.of(list.get(0));
        }
    }

    public Optional<Product> getMaxPriceProduct() {
        return getFirstOptional(select("*", "ORDER BY PRICE DESC", "LIMIT 1", Decoders.PRODUCT_DECODER));
    }

    public Optional<Product> getMinPriceProduct() {
        return getFirstOptional(select("*", "ORDER BY PRICE", "LIMIT 1", Decoders.PRODUCT_DECODER));
    }
}
