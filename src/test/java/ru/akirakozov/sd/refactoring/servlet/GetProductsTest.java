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

public class GetProductsTest {
    private static final String HOST = "http://localhost:" + Main.PORT + "/get-products";

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

    @Test
    void getProductsPage() throws IOException, InterruptedException {
        Assertions.assertEquals(getPageWithGivenBody(""), Utils.makeQuery(HOST).body());

        table.insert(new Product("SD", 3));
        table.insert(new Product("dbms", 1));

        Assertions.assertEquals(getPageWithGivenBody("SD\t3</br>dbms\t1</br>"), Utils.makeQuery(HOST).body());

        table.insert(new Product("math", 2));

        Assertions.assertEquals(getPageWithGivenBody("SD\t3</br>math\t2</br>dbms\t1</br>"), Utils.makeQuery(HOST).body());
    }
}
