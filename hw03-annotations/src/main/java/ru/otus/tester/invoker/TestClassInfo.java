package ru.otus.tester.invoker;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Objects;

public record TestClassInfo(Class<?> clazz, List<Method> after, List<Method> before, List<Method> tests) {
    @Override
    public String toString() {
        return "TestClassInfo{" + "clazz="
                + clazz + ", after="
                + after + ", before="
                + before + ", tests="
                + tests + '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TestClassInfo that = (TestClassInfo) o;
        return Objects.equals(clazz, that.clazz)
                && Objects.equals(after, that.after)
                && Objects.equals(before, that.before)
                && Objects.equals(tests, that.tests);
    }

    @Override
    public int hashCode() {
        return Objects.hash(clazz, after, before, tests);
    }
}
