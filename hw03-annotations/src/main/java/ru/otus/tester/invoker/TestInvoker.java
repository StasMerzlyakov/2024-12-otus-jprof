package ru.otus.tester.invoker;

import ru.otus.tester.statistics.TestResult;

public interface TestInvoker {
    TestResult[] runTest(TestClassInfo testClassInfo) throws TestInvocationException;
}
