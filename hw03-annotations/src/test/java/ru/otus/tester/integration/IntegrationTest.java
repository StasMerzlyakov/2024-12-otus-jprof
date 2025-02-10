package ru.otus.tester.integration;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.otus.tester.annotations.MethodSearcherImpl;
import ru.otus.tester.invoker.TestInvokerImpl;
import ru.otus.tester.statistics.TestResult;

@DisplayName("Класс IntegrationTest")
class IntegrationTest {

    @DisplayName("проверка работы на классе TestClassOk")
    @Test
    void doTestClassOkTest() throws Exception {
        var testClass = "ru.otus.tester.testclasses.ClassOk";
        var searcher = new MethodSearcherImpl();
        var invoker = new TestInvokerImpl();

        var info = searcher.getClassInfo(testClass);
        var testResult = invoker.runTest(info);
        assertThat(testResult).hasSize(2);
        assertThat(testResult).filteredOn(TestResult::result).hasSize(2);
    }

    @DisplayName("проверка работы на классе BeforeMethodException")
    @Test
    void doBeforeMethodExceptionTest() throws Exception {
        var testClass = "ru.otus.tester.testclasses.BeforeMethodException";
        var searcher = new MethodSearcherImpl();
        var invoker = new TestInvokerImpl();

        var info = searcher.getClassInfo(testClass);
        var testResult = invoker.runTest(info);
        assertThat(testResult).hasSize(2);
        assertThat(testResult).filteredOn(TestResult::result).isEmpty();
    }

    @DisplayName("проверка работы на классе TestMethodException")
    @Test
    void doTestMethodExceptionTest() throws Exception {
        var testClass = "ru.otus.tester.testclasses.TestMethodException";
        var searcher = new MethodSearcherImpl();
        var invoker = new TestInvokerImpl();

        var info = searcher.getClassInfo(testClass);
        var testResult = invoker.runTest(info);
        assertThat(testResult).hasSize(2);
        assertThat(testResult).filteredOn(TestResult::result).hasSize(1);
    }

    @DisplayName("проверка работы на классе AfterMethodException")
    @Test
    void doAfterMethodExceptionTest() throws Exception {
        var testClass = "ru.otus.tester.testclasses.AfterMethodException";
        var searcher = new MethodSearcherImpl();
        var invoker = new TestInvokerImpl();

        var info = searcher.getClassInfo(testClass);
        var testResult = invoker.runTest(info);
        assertThat(testResult).hasSize(2);
        assertThat(testResult).filteredOn(TestResult::result).isEmpty();
    }
}
