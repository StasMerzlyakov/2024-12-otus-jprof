package ru.otus.tester.testclasses;

import ru.otus.tester.annotations.After;
import ru.otus.tester.annotations.Before;
import ru.otus.tester.annotations.Test;

@SuppressWarnings({"squid:S1186", "squid:S112", "squid:S2166"})
public class BeforeMethodException {
    @Before
    public void before() {
        throw new RuntimeException("Test");
    }

    @Before
    public void before2() {}

    @After
    public void after() {}

    @Test
    public void test1() {}

    @Test
    public void test2() {}
}
