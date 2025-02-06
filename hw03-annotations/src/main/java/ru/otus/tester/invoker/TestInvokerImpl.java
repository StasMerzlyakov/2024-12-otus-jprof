package ru.otus.tester.invoker;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.tester.statistics.TestResult;

/**
 * Отвечает за вызов методов тестирования
 * - testclass инстанциируется перед каждым тестом
 * - @Before, если не нулевой, вызывается всегда
 * - @Test вызывается, если @Before отработан
 * - @After вызывается всегда (независимо от результата работы методов @Before и @Test)
 */
public class TestInvokerImpl implements TestInvoker {

    private static Logger logger = LoggerFactory.getLogger(TestInvokerImpl.class);

    @Override
    public TestResult[] runTest(TestClassInfo testClassInfo) throws TestInvocationException {

        var testResultList = new ArrayList<>();

        var after = testClassInfo.after();
        var before = testClassInfo.before();
        var testClazz = testClassInfo.clazz();

        for (Method test : testClassInfo.tests()) {
            Object object;
            try {
                var constructor = testClazz.getConstructor();
                object = constructor.newInstance();
            } catch (Exception e) {
                logger.error("instantiation exception");
                throw new TestInvocationException(e);
            }

            var testResult = invokeTest(object, before, after, test);
            testResultList.add(testResult);
        }

        return testResultList.toArray(new TestResult[] {});
    }

    private ResultPair invokeBefore(Object object, Method before) throws TestInvocationException {
        if (before != null) {
            try {
                before.invoke(object);
            } catch (IllegalAccessException ex) {
                logger.error("can't access before method");
                throw new TestInvocationException(ex);
            } catch (InvocationTargetException ex) {
                logger.error("before method exception");
                return new ResultPair(false, ex.getTargetException());
            }
        }
        return new ResultPair(true, null);
    }

    private ResultPair invokeTest(Object object, Method test) throws TestInvocationException {
        try {
            test.invoke(object);
        } catch (IllegalAccessException ex) {
            logger.error("can't access test method {}", test.getName());
            throw new TestInvocationException(ex);
        } catch (InvocationTargetException ex) {
            logger.error("test method exception");
            return new ResultPair(false, ex.getTargetException());
        }
        return new ResultPair(true, null);
    }

    private ResultPair invokeAfter(Object object, Method after) throws TestInvocationException {
        if (after != null) {
            try {
                after.invoke(object);
            } catch (IllegalAccessException ex) {
                logger.error("can't access after method {}", after.getName());
                throw new TestInvocationException(ex);
            } catch (InvocationTargetException ex) {
                logger.error("after method exception");
                return new ResultPair(false, ex.getTargetException());
            }
        }
        return new ResultPair(true, null);
    }

    TestResult invokeTest(Object object, Method before, Method after, Method test) throws TestInvocationException {

        var beforeResult = invokeBefore(object, before);
        boolean testResult = beforeResult.result;
        Throwable testErrorCause = beforeResult.throwable;

        if (testResult) {
            invokeTest(object, test);
        }

        var afterResult = invokeAfter(object, after);
        if (testResult && !afterResult.result) {
            testResult = false;
            testErrorCause = afterResult.throwable;
        }

        return new TestResult(test.getName(), testResult, testErrorCause);
    }

    private class ResultPair {

        public final boolean result;
        public final Throwable throwable;

        ResultPair(boolean result, Throwable throwable) {
            this.result = result;
            this.throwable = throwable;
        }
    }
}
