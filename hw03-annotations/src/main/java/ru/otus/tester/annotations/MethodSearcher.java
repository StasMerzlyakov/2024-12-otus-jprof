package ru.otus.tester.annotations;

import ru.otus.tester.invoker.TestClassInfo;

public interface MethodSearcher {
    TestClassInfo getClassInfo(String className) throws MethodSearchException;
}
