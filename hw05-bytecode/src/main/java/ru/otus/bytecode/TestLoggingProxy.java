package ru.otus.bytecode;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TestLoggingProxy implements InvocationHandler {
    private static final Logger logger = LoggerFactory.getLogger(TestLoggingProxy.class);

    private final ITestLogging internal;

    // Вот такая оптимизация.
    private final Map<String, Boolean> logMethodsMap;

    private TestLoggingProxy(ITestLogging internal) {
        this.internal = internal;
        this.logMethodsMap = searchMethodsToLog(internal);
    }

    private Map<String, Boolean> searchMethodsToLog(ITestLogging internal) {
        Map<String, Boolean> methodMap = new HashMap<>();
        Class<?> clazz = internal.getClass();
        for (Method m : clazz.getMethods()) {
            if (m.getAnnotation(Log.class) != null) {
                String methodName = getMethodInfo(m);
                methodMap.put(methodName, true);
                logger.info("found method {} with {} annotations", methodName, Log.class.getName());
            }
        }
        return methodMap;
    }

    private String getMethodInfo(Method method) {
        StringBuilder sb = new StringBuilder();
        sb.append(method.getName());
        var parameterTypes = method.getParameterTypes();
        sb.append(Arrays.stream(parameterTypes).map(Type::getTypeName).collect(Collectors.joining(",", "(", ")")));
        return sb.toString();
    }

    void logMethodParameters(Method method, Object[] args) {
        logger.atInfo()
                .setMessage("executed method: {}, param: {}")
                .addArgument(method::getName)
                .addArgument(() -> Arrays.toString(args))
                .log();
    }

    public static ITestLogging createProxy(ITestLogging testLogging) {
        InvocationHandler proxy = new TestLoggingProxy(testLogging);
        return (ITestLogging) Proxy.newProxyInstance(
                TestLoggingProxy.class.getClassLoader(), new Class<?>[] {ITestLogging.class}, proxy);
    }

    @Override
    public Object invoke(Object o, Method method, Object[] args) throws Throwable {
        String methodName = getMethodInfo(method);
        if (logMethodsMap.containsKey(methodName)) {
            logMethodParameters(method, args);
        }
        return method.invoke(internal, args);
    }
}
