package ru.otus.tester.annotations;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatNoException;
import static org.assertj.core.api.ThrowableAssert.catchThrowable;

import java.util.concurrent.atomic.AtomicReference;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import ru.otus.tester.invoker.TestClassInfo;

@DisplayName("Если загружаем класс")
class MethodSearcherImplTest {

    @ParameterizedTest
    @ValueSource(strings = {"unkown.class.Name"})
    @DisplayName("который не найден, то получим исключение MethodSearchException")
    void doUnknownClassTest(String className) {
        checkMethodSearchException(className);
    }

    @ParameterizedTest
    @ValueSource(strings = {"ru.otus.tester.testclasses.TestClass1"})
    @DisplayName("который содержит корректные данные, то методы найдены")
    void doGoodClassTest(String className) {
        MethodSearcherImpl methodSearcher = new MethodSearcherImpl();

        // Можно ли совместить assertThatNoException с получением результата
        AtomicReference<TestClassInfo> testClassInfo = new AtomicReference<>();
        assertThatNoException().isThrownBy(() -> testClassInfo.set(methodSearcher.getClassInfo(className)));
        TestClassInfo classInfo = testClassInfo.get();

        assertThat(classInfo.clazz()).isNotNull();
        assertThat(classInfo.after()).isNotNull();
        assertThat(classInfo.before()).isNotNull();

        assertThat(classInfo.tests()).isNotNull();
        assertThat(classInfo.tests()).hasSize(2);
    }

    @ParameterizedTest
    @ValueSource(strings = {"ru.otus.tester.testclasses.TestClass2"})
    @DisplayName("который содержит корректные данные, но без методов @After и @Before")
    void doGoodClassTest2(String className) {
        MethodSearcherImpl methodSearcher = new MethodSearcherImpl();

        // Можно ли совместить assertThatNoException с получением результата
        AtomicReference<TestClassInfo> testClassInfo = new AtomicReference<>();
        assertThatNoException().isThrownBy(() -> testClassInfo.set(methodSearcher.getClassInfo(className)));
        TestClassInfo classInfo = testClassInfo.get();

        assertThat(classInfo.clazz()).isNotNull();
        assertThat(classInfo.after()).isNull();
        assertThat(classInfo.before()).isNull();

        assertThat(classInfo.tests()).isNotNull();
        assertThat(classInfo.tests()).hasSize(2);
    }

    @ParameterizedTest
    @ValueSource(strings = {"ru.otus.tester.testclasses.TestClass3"})
    @DisplayName("который не содержит методов для тестирования")
    void doGoodClassTest3(String className) {
        MethodSearcherImpl methodSearcher = new MethodSearcherImpl();

        // Можно ли совместить assertThatNoException с получением результата
        AtomicReference<TestClassInfo> testClassInfo = new AtomicReference<>();
        assertThatNoException().isThrownBy(() -> testClassInfo.set(methodSearcher.getClassInfo(className)));
        TestClassInfo classInfo = testClassInfo.get();

        assertThat(classInfo.clazz()).isNotNull();
        assertThat(classInfo.after()).isNull();
        assertThat(classInfo.before()).isNull();
        assertThat(classInfo.tests()).isEmpty();
    }

    @ParameterizedTest
    @ValueSource(strings = {"ru.otus.tester.testclasses.TestClass4"})
    @DisplayName("который содержит два метода @After, то получим исключение MethodSearchException")
    void doDoubleAfterTest(String className) {
        checkMethodSearchException(className);
    }

    @ParameterizedTest
    @ValueSource(strings = {"ru.otus.tester.testclasses.TestClass5"})
    @DisplayName("который содержит аннотацию @After и @Test на методе, то получим исключение MethodSearchException")
    void doAfterAndTestAnnotationOnMethod(String className) {
        checkMethodSearchException(className);
    }

    private void checkMethodSearchException(String className) {
        MethodSearcherImpl methodSearcher = new MethodSearcherImpl();

        // when
        Throwable thrown = catchThrowable(() -> {
            methodSearcher.getClassInfo(className);
        });

        // then
        assertThat(thrown).isInstanceOf(MethodSearchException.class);
    }
}
