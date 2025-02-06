package ru.otus.tester;

import ru.otus.tester.annotations.After;
import ru.otus.tester.annotations.Before;
import ru.otus.tester.annotations.Test;

// Исключительно для проверки вызова java -jar ./build/libs/gradleTestRunner-0.1.jar ru.otus.tester.TestClassInv
@SuppressWarnings("squid:S1186")
public class TestClassInv {
    @Before
    public void before() {}

    @After
    public void after() {}

    @Test
    public void test1() {}

    @Test
    public void test2() {}
}
