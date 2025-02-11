package ru.otus.tester.statistics;

public record TestResult(String methodName, Boolean result, Throwable throwable) {}
