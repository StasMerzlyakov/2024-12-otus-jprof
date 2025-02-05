package ru.otus.tester.statistics;

import java.io.OutputStream;

public interface IResultProcessor {
    void printResults(TestResult[] results, OutputStream out) throws PrintResultsException;
}
