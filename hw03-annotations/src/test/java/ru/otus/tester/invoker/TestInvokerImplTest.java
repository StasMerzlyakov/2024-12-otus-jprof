package ru.otus.tester.invoker;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.lang.reflect.Method;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InOrder;
import org.mockito.Mockito;
import ru.otus.tester.statistics.TestResult;
import ru.otus.tester.testclasses.CustomTestException;
import ru.otus.tester.testclasses.TestClass1;

@DisplayName("Класс TestInvokerImpl")
class TestInvokerImplTest {

    private InOrder inOrder;

    @DisplayName("если методы @Before и @After существуют - то они должны вызываться для каждого теста")
    @Test
    void doInvokeTest() throws Exception {
        Class<?> testClass = TestClass1.class;
        Method before = testClass.getMethod("before");
        assertThat(before).isNotNull();

        Method after = testClass.getMethod("after");
        assertThat(after).isNotNull();

        Method test1 = testClass.getMethod("test1");
        assertThat(test1).isNotNull();

        Method test2 = testClass.getMethod("test2");
        assertThat(test2).isNotNull();

        TestClass1 object = Mockito.mock(TestClass1.class);
        inOrder = inOrder(object);

        TestInvokerImpl invoker = new TestInvokerImpl();
        TestResult testResult = invoker.invokeTest(object, before, after, test1);

        // test results
        assertThat(testResult).isNotNull();
        assertThat(testResult.result()).isTrue();
        assertThat(testResult.throwable()).isNull();
        assertThat(testResult.methodName()).isEqualTo(test1.getName());

        // test order

        inOrder.verify(object, times(1)).before();
        inOrder.verify(object, times(1)).test1();
        inOrder.verify(object, times(1)).after();

        verify(object, times(0)).test2();
    }

    @DisplayName("если в методе @Before вылетело исключение - то метод @Test не вызывается, а @After вызывается")
    @Test
    void doInvokeTest2() throws Exception {
        Class<?> testClass = TestClass1.class;
        Method before = testClass.getMethod("before");
        assertThat(before).isNotNull();

        Method after = testClass.getMethod("after");
        assertThat(after).isNotNull();

        Method test1 = testClass.getMethod("test1");
        assertThat(test1).isNotNull();

        Method test2 = testClass.getMethod("test2");
        assertThat(test2).isNotNull();

        TestClass1 object = Mockito.mock(TestClass1.class);

        doThrow(new CustomTestException()).when(object).before();

        inOrder = inOrder(object);

        TestInvokerImpl invoker = new TestInvokerImpl();
        TestResult testResult = invoker.invokeTest(object, before, after, test1);

        // test results
        assertThat(testResult).isNotNull();
        assertThat(testResult.result()).isFalse();
        assertThat(testResult.throwable()).isNotNull();
        assertThat(testResult.throwable()).isInstanceOf(CustomTestException.class);
        assertThat(testResult.methodName()).isEqualTo(test1.getName());

        // test order

        inOrder.verify(object, times(1)).before();
        inOrder.verify(object, times(0)).test1();
        inOrder.verify(object, times(1)).after();

        verify(object, times(0)).test2();
    }
}
