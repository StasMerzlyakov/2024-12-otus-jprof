package ru.otus.tester.annotations;

import ru.otus.tester.invoker.TestClassInfo;

public interface IMethodSearcher {
    TestClassInfo getClassInfo(String className) throws MethodSearchException;
}
