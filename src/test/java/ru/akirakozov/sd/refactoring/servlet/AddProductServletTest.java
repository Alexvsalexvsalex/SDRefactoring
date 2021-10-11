package ru.akirakozov.sd.refactoring.servlet;

import org.eclipse.jetty.server.Server;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.akirakozov.sd.refactoring.Main;
import ru.akirakozov.sd.refactoring.db.ProductTable;
import ru.akirakozov.sd.refactoring.domain.Product;

import java.io.File;
import java.io.IOException;
import java.net.http.HttpResponse;
import java.util.List;

public class AddProductServletTest {
    private static final String HOST = "http://localhost:" + Main.PORT + "/add-product";

    private Server server;
    private ProductTable table;

    @BeforeEach
    void setUpServer() throws IOException {
        File tempFile = File.createTempFile("testdb", "");
        tempFile.deleteOnExit();

        table = new ProductTable(tempFile.getAbsolutePath());
        server = Main.upServer(table);
    }

    @AfterEach
    void destroyServer() throws Exception {
        server.stop();
    }

    private String getPageWithGivenBody(String body) {
        return String.format("<html><head></head><body>%s</body></html>\n", body);
    }

    private HttpResponse<String> addProductQuery(String name, long price) throws IOException, InterruptedException {
        return Utils.makeQuery(HOST + "?name=" + name + "&price=" + price);
    }

    @Test
    void addProductPage() throws IOException, InterruptedException {
        Assertions.assertEquals(List.of(), table.selectAllOrderedByPriceDesc());

        Assertions.assertEquals(addProductQuery("1", 2).body(), getPageWithGivenBody("OK\n"));

        Assertions.assertEquals(List.of(new Product("1", 2)), table.selectAllOrderedByPriceDesc());

        Assertions.assertEquals(addProductQuery("3", 4).body(), getPageWithGivenBody("OK\n"));
        Assertions.assertEquals(addProductQuery("5", 6).body(), getPageWithGivenBody("OK\n"));


        Assertions.assertEquals(
                List.of(new Product("5", 6),
                        new Product("3", 4),
                        new Product("1", 2)),
                table.selectAllOrderedByPriceDesc());
    }
}
