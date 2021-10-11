package ru.akirakozov.sd.refactoring;

import org.eclipse.jetty.http.HttpStatus;
import org.eclipse.jetty.server.Server;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.akirakozov.sd.refactoring.db.ProductTable;
import ru.akirakozov.sd.refactoring.servlet.Utils;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.concurrent.TimeUnit;

class MainTest {
    private static final String HOST = "http://localhost:" + Main.PORT;

    @BeforeEach
    void setUpServer() throws IOException {
        File tempFile = File.createTempFile("testdb", "");
        tempFile.deleteOnExit();

        ProductTable table = new ProductTable(tempFile.getAbsolutePath());
        Main.upServer(table);
    }

    @Test
    void upServer() throws IOException, InterruptedException {
        HttpResponse<String> response = Utils.makeQuery(HOST);
        Assertions.assertEquals(response.statusCode(), HttpStatus.NOT_FOUND_404);
    }
}