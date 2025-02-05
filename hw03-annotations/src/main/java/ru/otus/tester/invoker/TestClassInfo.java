package ru.otus.tester.invoker;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Objects;

public record TestClassInfo(Class<?> clazz, Method after, Method before, Method[] tests) {
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TestClassInfo that = (TestClassInfo) o;
        return clazz.equals(that.clazz)
                && after.equals(that.after)
                && before.equals(that.before)
                && Arrays.equals(tests, that.tests);
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(clazz, after, before);
        result = 31 * result + Arrays.hashCode(tests);
        return result;
    }

    @Override
    public String toString() {
        return "TestClassInfo{" + "clazz="
                + clazz + ", after="
                + after + ", before="
                + before + ", tests="
                + Arrays.toString(tests) + '}';
    }
}
