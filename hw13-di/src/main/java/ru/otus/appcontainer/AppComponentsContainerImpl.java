package ru.otus.appcontainer;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;
import ru.otus.appcontainer.api.AppComponent;
import ru.otus.appcontainer.api.AppComponentsContainer;
import ru.otus.appcontainer.api.AppComponentsContainerConfig;

@SuppressWarnings("squid:S1068")
public class AppComponentsContainerImpl implements AppComponentsContainer {

    private final List<Object> appComponents = new ArrayList<>();
    private final Map<String, Object> appComponentsByName = new HashMap<>();

    // немного расширил интерфейс для возможности использования с несколькими классам
    public AppComponentsContainerImpl(Class<?>... initialConfigClasses) {
        processConfig(initialConfigClasses);
    }

    private void processConfig(Class<?>... configClasses) {

        var configClassOrder = new TreeMap<Integer, List<Class<?>>>();
        for (Class<?> configClass : configClasses) {
            checkConfigClass(configClass);
            var order = getConfigOrder(configClass);
            configClassOrder.computeIfAbsent(order, k -> new LinkedList<>()).add(configClass);
        }

        for (List<Class<?>> configList : configClassOrder.values()) {
            for (Class<?> configClass : configList) {
                initConfig(configClass);
            }
        }
    }

    private void initConfig(Class<?> configClass) {
        var methodInitOrder = new TreeMap<Integer, List<Method>>();

        Object configObj;
        try {
            configObj = configClass.getConstructor().newInstance();
        } catch (Exception e) {
            throw new IllegalArgumentException(String.format(
                    "Given class %s does not have default constructor or it's not acceptable", configClass.getName()));
        }

        for (Method meth : configClass.getMethods()) {
            if (meth.isAnnotationPresent(AppComponent.class)) {
                AppComponent annot = meth.getAnnotation(AppComponent.class);
                int order = annot.order();
                methodInitOrder.computeIfAbsent(order, k -> new LinkedList<>()).add(meth);
            }
        }

        for (List<Method> methodList : methodInitOrder.values()) {
            for (Method meth : methodList) {
                String beanName = meth.getAnnotation(AppComponent.class).name();
                if (appComponentsByName.containsKey(beanName)) {
                    throw new IllegalArgumentException(String.format("Duplicate bean name config %s", beanName));
                }

                Object[] invocationObjects = new Object[meth.getParameterCount()];
                for (int i = 0; i < meth.getParameterCount(); i++) {
                    var type = meth.getParameterTypes()[i];
                    Object objComponent = getAppComponent(type);
                    if (objComponent == null) {
                        throw new IllegalArgumentException(String.format(
                                "Can't find bean of type %s required by method %s", type.getName(), meth.getName()));
                    }
                    invocationObjects[i] = objComponent;
                }

                try {
                    Object bean = meth.invoke(configObj, invocationObjects);
                    appComponents.add(bean);
                    appComponentsByName.put(beanName, bean);
                } catch (IllegalAccessException e) {
                    throw new RuntimeException(e);
                } catch (InvocationTargetException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }

    private void checkConfigClass(Class<?> configClass) {
        if (!configClass.isAnnotationPresent(AppComponentsContainerConfig.class)) {
            throw new IllegalArgumentException(String.format("Given class is not config %s", configClass.getName()));
        }
    }

    private int getConfigOrder(Class<?> configClass) {
        return configClass.getAnnotation(AppComponentsContainerConfig.class).order();
    }

    @Override
    public <C> C getAppComponent(Class<C> componentClass) {
        return (C) appComponents.stream()
                .filter(obj -> componentClass.isInstance(obj))
                .findFirst()
                .orElse(null);
    }

    @Override
    public <C> C getAppComponent(String componentName) {
        Object obj = appComponentsByName.get(componentName);
        return (C) obj;
    }
}
