package ru.akirakozov.sd.refactoring.servlet;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class Utils {
    public static HttpResponse<String> makeQuery(String page) throws IOException, InterruptedException {
        final HttpRequest requestMin = HttpRequest.newBuilder(URI.create(page)).build();
        return HttpClient.newHttpClient().send(requestMin, HttpResponse.BodyHandlers.ofString());
    }
}
