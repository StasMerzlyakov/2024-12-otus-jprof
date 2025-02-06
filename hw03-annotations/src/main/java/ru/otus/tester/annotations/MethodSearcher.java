package ru.otus.tester.annotations;

import java.lang.reflect.Method;
import java.util.ArrayList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.tester.invoker.TestClassInfo;

/**
 * Отвечает за:
 *  - поиск класса по переданному имени
 *  - проверку класса на возможность инстанциирования без параметров
 *  - поиск методов помеченных аннотациями @After @Before @Test
 *  - проверку, что аннотации расставлены как нужно
 */
public class MethodSearcher implements IMethodSearcher {

    private static Logger logger = LoggerFactory.getLogger(MethodSearcher.class);

    private static final String WRONG_ANNOTOTATION = "wrong annotations";
    private static final String WRONG_ANNOTOTATION_TEMPLATE = "wrong annotations on method {}";

    @Override
    public TestClassInfo getClassInfo(String className) throws MethodSearchException {
        logger.debug("getClassInfo {} start", className);

        var clazz = loadClass(className);

        Method after = null;
        Method before = null;
        ArrayList<Method> tests = new ArrayList<>();

        for (Method meth : clazz.getMethods()) {
            if (meth.isAnnotationPresent(After.class)) {
                if (after != null) {
                    logger.error("method with annotation @After already present on method {}", after.getName());
                    throw new MethodSearchException(WRONG_ANNOTOTATION);
                }
                testAfterMethod(meth);
                logger.debug("found @Auth method {}", meth.getName());
                after = meth;
            }

            if (meth.isAnnotationPresent(Before.class)) {

                if (before != null) {
                    logger.error("method with annotation @Before already present on method {}", before.getName());
                    throw new MethodSearchException(WRONG_ANNOTOTATION);
                }

                testBeforeMethod(meth);

                logger.debug("found @Before method {}", meth.getName());
                before = meth;
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

        return new TestClassInfo(clazz, after, before, tests.toArray(new Method[] {}));
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
        if (method.isAnnotationPresent(After.class) || method.isAnnotationPresent(Before.class)) {
            logger.error(WRONG_ANNOTOTATION_TEMPLATE, method.getName());
            throw new MethodSearchException(WRONG_ANNOTOTATION);
        }
    }

    private void testBeforeMethod(Method method) throws MethodSearchException {
        testMethod(method);
        if (method.isAnnotationPresent(After.class) || method.isAnnotationPresent(Test.class)) {
            logger.error(WRONG_ANNOTOTATION_TEMPLATE, method.getName());
            throw new MethodSearchException(WRONG_ANNOTOTATION);
        }
    }

    private void testAfterMethod(Method method) throws MethodSearchException {
        testMethod(method);
        if (method.isAnnotationPresent(Before.class) || method.isAnnotationPresent(Test.class)) {
            logger.error(WRONG_ANNOTOTATION_TEMPLATE, method.getName());
            throw new MethodSearchException(WRONG_ANNOTOTATION);
        }
    }

    private void testMethod(Method method) throws MethodSearchException {
        if (!method.getReturnType().equals(void.class)) {
            logger.error("method {} have wrong return type, expected void", method.getName());
            throw new MethodSearchException("wrong return type");
        }

        if (method.getParameterCount() != 0) {
            logger.error("method {} have unexpected parameters", method.getName());
            throw new MethodSearchException("test method have paramters");
        }
    }
}
