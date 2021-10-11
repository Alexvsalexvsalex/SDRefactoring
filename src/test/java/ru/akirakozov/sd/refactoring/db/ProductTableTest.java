package ru.akirakozov.sd.refactoring.db;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.akirakozov.sd.refactoring.domain.Product;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ProductTableTest {
    private Database database;
    private ProductTable table;

    @BeforeEach
    void setUp() throws IOException {
        File tempFile = File.createTempFile("testdb", "");
        tempFile.deleteOnExit();

        database = new Database(tempFile.getAbsolutePath());
        table = new ProductTable(database);
        table.create();
    }

    private List<Product> getAllProductsFromDb() {
        return database.executeQuery("SELECT * FROM PRODUCT", rs -> {
            try {
                List<Product> products = new ArrayList<>();
                while (rs.next()) {
                    products.add(new Product(rs.getString("name"), rs.getInt("price")));
                }
                return products;
            } catch (SQLException e) {
                throw new RuntimeException();
            }
        });
    }

    @Test
    void insert() {
        Assertions.assertEquals(List.of(), getAllProductsFromDb());

        Product product1 = new Product("1", 2);
        table.insert(product1);
        Assertions.assertEquals(List.of(product1), getAllProductsFromDb());

        Product product2 = new Product("3", 4);
        table.insert(product2);
        Product product3 = new Product("5", 6);
        table.insert(product3);
        Assertions.assertEquals(List.of(product1, product2, product3), getAllProductsFromDb());
    }

    @Test
    void selectAllOrderedByPriceDesc() {
        Product product1 = new Product("1", 2);
        Product product2 = new Product("3", 4);
        Product product3 = new Product("5", 6);
        Product product4 = new Product("7", 8);
        Assertions.assertEquals(List.of(),
                table.selectAllOrderedByPriceDesc());
        table.insert(product1);
        table.insert(product4);
        Assertions.assertEquals(List.of(product4, product1),
                table.selectAllOrderedByPriceDesc());
        table.insert(product3);
        table.insert(product2);
        Assertions.assertEquals(List.of(product4, product3, product2, product1),
                table.selectAllOrderedByPriceDesc());
    }

    @Test
    void countAll() {
        Product product1 = new Product("1", 2);
        Product product2 = new Product("3", 4);
        Product product3 = new Product("5", 6);
        Product product4 = new Product("7", 8);
        Assertions.assertEquals(0, table.countAll());
        table.insert(product1);
        table.insert(product4);
        Assertions.assertEquals(2, table.countAll());
        table.insert(product3);
        table.insert(product2);
        Assertions.assertEquals(4, table.countAll());
    }
}