package ru.akirakozov.sd.refactoring.pages;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class HtmlFormatterTest {
    @Test
    void emptyPage() {
        HtmlFormatter formatter = new HtmlFormatter();
        Assertions.assertEquals("<html><head></head><body></body></html>", formatter.getPage());
        Assertions.assertEquals("<head></head>", formatter.getHeadBlock());
        Assertions.assertEquals("<body></body>", formatter.getBodyBlock());
    }

    @Test
    void fullPage() {
        HtmlFormatter formatter = new HtmlFormatter();
        formatter.printlnToHead("Hello");
        formatter.printlnToBody("world");
        Assertions.assertEquals("<html><head>Hello\n</head><body>world\n</body></html>", formatter.getPage());
        Assertions.assertEquals("<head>Hello\n</head>", formatter.getHeadBlock());
        Assertions.assertEquals("<body>world\n</body>", formatter.getBodyBlock());
    }

    @Test
    void multilinePage() {
        HtmlFormatter formatter = new HtmlFormatter();
        formatter.printlnToBody("world");
        formatter.printlnToBody("world");
        formatter.printlnToBody("world");
        Assertions.assertEquals("<html><head></head><body>world\nworld\nworld\n</body></html>", formatter.getPage());
        Assertions.assertEquals("<head></head>", formatter.getHeadBlock());
        Assertions.assertEquals("<body>world\nworld\nworld\n</body>", formatter.getBodyBlock());
    }
}