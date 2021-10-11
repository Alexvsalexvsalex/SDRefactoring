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

public class QueryServletTest {
    private static final String HOST = "http://localhost:" + Main.PORT + "/query";

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

    private HttpResponse<String> anyCommand(String command) throws IOException, InterruptedException {
        return Utils.makeQuery(HOST + "?command=" + command);
    }

    private HttpResponse<String> minCommand() throws IOException, InterruptedException {
        return anyCommand("min");
    }

    private HttpResponse<String> maxCommand() throws IOException, InterruptedException {
        return anyCommand("max");
    }

    private HttpResponse<String> sumCommand() throws IOException, InterruptedException {
        return anyCommand("sum");
    }

    private HttpResponse<String> countCommand() throws IOException, InterruptedException {
        return anyCommand("count");
    }

    private String getPageWithGivenBody(String body) {
        return String.format("<html><head></head><body>%s</body></html>\n", body);
    }

    @Test
    void minPage() throws IOException, InterruptedException {
        Assertions.assertEquals(getPageWithGivenBody("<h1>Product with min price: </h1>\nNone\n"), minCommand().body());

        table.insert(new Product("SD", 3));
        table.insert(new Product("dbms", 1));

        Assertions.assertEquals(getPageWithGivenBody("<h1>Product with min price: </h1>\ndbms\n"), minCommand().body());

        table.insert(new Product("math", 2));

        Assertions.assertEquals(getPageWithGivenBody("<h1>Product with min price: </h1>\ndbms\n"), minCommand().body());
    }

    @Test
    void maxPage() throws IOException, InterruptedException {
        Assertions.assertEquals(getPageWithGivenBody("<h1>Product with max price: </h1>\nNone\n"), maxCommand().body());

        table.insert(new Product("SD", 3));
        table.insert(new Product("dbms", 1));

        Assertions.assertEquals(getPageWithGivenBody("<h1>Product with max price: </h1>\nSD\n"), maxCommand().body());

        table.insert(new Product("math", 2));

        Assertions.assertEquals(getPageWithGivenBody("<h1>Product with max price: </h1>\nSD\n"), maxCommand().body());
    }

    @Test
    void sumPage() throws IOException, InterruptedException {
        Assertions.assertEquals(getPageWithGivenBody("Summary price: \n0\n"), sumCommand().body());

        table.insert(new Product("SD", 3));
        table.insert(new Product("dbms", 1));

        Assertions.assertEquals(getPageWithGivenBody("Summary price: \n4\n"), sumCommand().body());

        table.insert(new Product("math", 2));

        Assertions.assertEquals(getPageWithGivenBody("Summary price: \n6\n"), sumCommand().body());
    }

    @Test
    void countPage() throws IOException, InterruptedException {
        Assertions.assertEquals(getPageWithGivenBody("Number of products: \n0\n"), countCommand().body());

        table.insert(new Product("SD", 3));
        table.insert(new Product("dbms", 1));

        Assertions.assertEquals(getPageWithGivenBody("Number of products: \n2\n"), countCommand().body());

        table.insert(new Product("math", 2));

        Assertions.assertEquals(getPageWithGivenBody("Number of products: \n3\n"), countCommand().body());
    }
}
