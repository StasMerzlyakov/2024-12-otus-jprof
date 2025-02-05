package ru.otus.tester.testclasses;

import ru.otus.tester.annotations.After;
import ru.otus.tester.annotations.Before;
import ru.otus.tester.annotations.Test;

@SuppressWarnings("squid:S1186")
public class TestClass4 {

    @Before
    public void before() {}

    @After
    public void after() {}

    @After
    public void test1() {}

    @Test
    public void test2() {}
}
