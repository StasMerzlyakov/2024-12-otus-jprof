package ru.otus.tester.invoker;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
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
                logger.debug("test object created");
            } catch (Exception e) {
                logger.error("instantiation exception");
                throw new TestInvocationException(e);
            }

            var testResult = invokeTest(object, before, after, test);
            testResultList.add(testResult);
        }

        return testResultList.toArray(new TestResult[] {});
    }

    private ResultPair invokeBefore(Object object, List<Method> before) throws TestInvocationException {
        for (Method m : before) {
            try {
                m.invoke(object);
                logger.debug("before method {} invoked", m.getName());
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
            logger.debug("test method {} invoked", test.getName());
        } catch (IllegalAccessException ex) {
            logger.error("can't access test method {}", test.getName());
            throw new TestInvocationException(ex);
        } catch (InvocationTargetException ex) {
            logger.error("test method exception");
            return new ResultPair(false, ex.getTargetException());
        }
        return new ResultPair(true, null);
    }

    private ResultPair invokeAfter(Object object, List<Method> after) throws TestInvocationException {
        for (Method m : after) {
            try {
                m.invoke(object);
                logger.debug("after method {} invoked", m.getName());
            } catch (IllegalAccessException ex) {
                logger.error("can't access after method {}", m.getName());
                throw new TestInvocationException(ex);
            } catch (InvocationTargetException ex) {
                logger.error("after method exception");
                return new ResultPair(false, ex.getTargetException());
            }
        }

        return new ResultPair(true, null);
    }

    // Метод имеет доступ уровня пакета для возможности вызова в тесте. Использование object,
    // а не class, в качестве аргумента позволяет подавать на вход метода mock-объект.
    TestResult invokeTest(Object object, List<Method> before, List<Method> after, Method test)
            throws TestInvocationException {

        var beforeResult = invokeBefore(object, before);
        boolean result = beforeResult.result;
        Throwable testErrorCause = beforeResult.throwable;

        if (result) {
            var testResult = invokeTest(object, test);
            if (!testResult.result) {
                result = false;
                testErrorCause = testResult.throwable;
            }
        }

        var afterResult = invokeAfter(object, after);
        if (result
                && !afterResult.result) { // проверяем testResults чтобы не переписать ошибку теста, если таковая была
            result = false;
            testErrorCause = afterResult.throwable;
        }

        return new TestResult(test.getName(), result, testErrorCause);
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
