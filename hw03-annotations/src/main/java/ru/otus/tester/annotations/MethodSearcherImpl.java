package ru.otus.tester.annotations;

import java.lang.reflect.Method;
import java.util.ArrayList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.tester.invoker.TestClassInfo;

/**
 * Отвечает за:
 * - поиск класса по переданному имени
 * - проверку класса на возможность инстанциирования без параметров
 * - поиск методов помеченных аннотациями @After @Before @Test
 * - проверку, что аннотации расставлены как нужно
 */
public class MethodSearcherImpl implements MethodSearcher {

    private static Logger logger = LoggerFactory.getLogger(MethodSearcherImpl.class);

    @Override
    public TestClassInfo getClassInfo(String className) throws MethodSearchException {
        logger.debug("getClassInfo {} start", className);

        var clazz = loadClass(className);

        var after = new ArrayList<Method>();
        var before = new ArrayList<Method>();
        var tests = new ArrayList<Method>();

        for (Method meth : clazz.getMethods()) {
            if (meth.isAnnotationPresent(After.class)) {
                testAfterMethod(meth);
                logger.debug("found @Auth method {}", meth.getName());
                after.add(meth);
            }

            if (meth.isAnnotationPresent(Before.class)) {
                testBeforeMethod(meth);
                logger.debug("found @Before method {}", meth.getName());
                before.add(meth);
            }

            if (meth.isAnnotationPresent(Test.class)) {
                testTestMethod(meth);

                logger.debug("found @Test method {}", meth.getName());
                tests.add(meth);
            }
        }

        if (tests.isEmpty()) {
            logger.warn("no tests found");
        }

        return new TestClassInfo(clazz, after, before, tests);
    }

    private Class<?> loadClass(String className) throws MethodSearchException {
        Class<?> clazz;

        try {
            clazz = Class.forName(className);
        } catch (ClassNotFoundException e) {
            logger.error("class {} not found", className);
            throw new MethodSearchException(e.getMessage());
        }

        try {
            clazz.getConstructor();
        } catch (NoSuchMethodException ex) {
            logger.error("class {} does not have default non arguments constructor", className);
            throw new MethodSearchException(ex.getMessage());
        }
        return clazz;
    }

    private void testTestMethod(Method method) throws MethodSearchException {
        testMethod(method);

        if (method.isAnnotationPresent(Before.class)) {
            throw new MethodSearchException(
                    String.format("Method %s have @Test annotation and @Before", method.getName()));
        }
    }

    private void testBeforeMethod(Method method) throws MethodSearchException {
        testMethod(method);
        if (method.isAnnotationPresent(After.class)) {
            throw new MethodSearchException(
                    String.format("Method %s have @Before annotation and @After", method.getName()));
        }

        if (method.isAnnotationPresent(Test.class)) {
            throw new MethodSearchException(
                    String.format("Method %s have @Before annotation and @Test", method.getName()));
        }
    }

    private void testAfterMethod(Method method) throws MethodSearchException {
        testMethod(method);
        if (method.isAnnotationPresent(Before.class)) {
            throw new MethodSearchException(
                    String.format("Method %s have @After annotation and @Before", method.getName()));
        }

        if (method.isAnnotationPresent(Test.class)) {
            throw new MethodSearchException(
                    String.format("Method %s have @Test annotation and @Before", method.getName()));
        }
    }

    private void testMethod(Method method) throws MethodSearchException {
        if (!method.getReturnType().equals(void.class)) {
            throw new MethodSearchException(
                    String.format("method %s have wrong return type, expected void", method.getName()));
        }

        if (method.getParameterCount() != 0) {
            throw new MethodSearchException(String.format("method %s must not have parameters", method.getName()));
        }
    }
}
