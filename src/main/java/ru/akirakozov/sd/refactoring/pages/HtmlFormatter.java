package ru.akirakozov.sd.refactoring.pages;

import ru.akirakozov.sd.refactoring.domain.Product;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class HtmlFormatter {
    private String head = "";
    private String body = "";

    public void printlnToHead(String s) {
        head += s + "\n";
    }

    public String getHeadBlock() {
        return "<head>" + head + "</head>";
    }

    public <T> void printlnToBody(T s) {
        body += s.toString() + "\n";
    }

    public void printlnToBody(Product product) {
        body += product.getName() + "\t" + product.getPrice() + "</br>";
    }

    public String getBodyBlock() {
        return "<body>" + body + "</body>";
    }

    public String getPage() {
        return "<html>" +
                getHeadBlock() +
                getBodyBlock() +
                "</html>";
    }

    public void writeToResponse(HttpServletResponse response) throws IOException {
        response.setContentType("text/html");
        response.getWriter().println(getPage());
    }
}
