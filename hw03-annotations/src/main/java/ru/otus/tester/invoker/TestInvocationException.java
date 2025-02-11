package ru.otus.tester.invoker;

public class TestInvocationException extends Exception {
    public TestInvocationException(Exception e) {
        super(e);
    }
}
