package ru.akirakozov.sd.refactoring.pages;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class HtmlFormatter {
    private String head = "";
    private String body = "";

    public void printlnToHead(String s) {
        head += s + "\n";
    }

    public String getHeadBlock() {
        return "<head>\n" + head + "</head>";
    }

    public <T> void printlnToBody(T s) {
        body += s.toString() + "\n";
    }

    public String getBodyBlock() {
        return "<body>\n" + body + "</body>";
    }

    public String getPage() {
        return "<html>\n" +
                getHeadBlock() + "\n" +
                getBodyBlock() + "\n" +
                "</html>";
    }

    public void writeToResponse(HttpServletResponse response) throws IOException {
        response.setContentType("text/html");
        response.getWriter().println(getPage());
    }
}
