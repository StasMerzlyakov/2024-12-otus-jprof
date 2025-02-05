package ru.otus.tester.invoker;

import ru.otus.tester.statistics.TestResult;

public interface ITestInvoker {
    TestResult[] runTest(TestClassInfo testClassInfo);
}
