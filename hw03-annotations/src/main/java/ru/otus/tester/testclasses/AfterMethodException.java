package ru.otus.tester.testclasses;

import ru.otus.tester.annotations.After;
import ru.otus.tester.annotations.Before;
import ru.otus.tester.annotations.Test;

@SuppressWarnings({"squid:S1186", "squid:S112", "squid:S2166"})
public class AfterMethodException {
    @Before
    public void before() {}

    @Before
    public void before2() {}

    @After
    public void after() {
        throw new RuntimeException("Test");
    }

    @Test
    public void test1() {}

    @Test
    public void test2() {}
}
