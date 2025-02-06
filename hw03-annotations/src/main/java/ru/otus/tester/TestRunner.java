package ru.otus.tester;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.tester.annotations.MethodSearchException;
import ru.otus.tester.annotations.MethodSearcher;
import ru.otus.tester.annotations.MethodSearcherImpl;
import ru.otus.tester.invoker.TestInvocationException;
import ru.otus.tester.invoker.TestInvoker;
import ru.otus.tester.invoker.TestInvokerImpl;
import ru.otus.tester.statistics.ResultProcessor;
import ru.otus.tester.statistics.ResultProcessorImpl;

public class TestRunner {

    private final String className;
    private final MethodSearcher methodSearcher;
    private final TestInvoker testInvoker;
    private final ResultProcessor resultProcessor;

    private static final Logger logger = LoggerFactory.getLogger(TestRunner.class);

    public TestRunner(String className) {
        this.className = className;
        this.methodSearcher = new MethodSearcherImpl();
        this.testInvoker = new TestInvokerImpl();
        this.resultProcessor = new ResultProcessorImpl();
    }

    public void run() throws TestRunnerException {
        try {
            var info = methodSearcher.getClassInfo(className);
            var results = testInvoker.runTest(info);
            resultProcessor.printResults(results);
        } catch (MethodSearchException | TestInvocationException e) {
            throw new TestRunnerException(e);
        }
    }

    private class TestRunnerException extends Exception {
        TestRunnerException(Exception e) {
            super(e);
        }
    }

    public static void main(String[] args) throws TestRunnerException {
        if (args.length < 1) {
            logger.error("class name does not specified");
            System.exit(1);
        }
        String className = args[0];
        new TestRunner(className).run();
    }
}
