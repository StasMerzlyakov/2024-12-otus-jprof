package ru.otus.bytecode;

import ru.otus.bytecode.testclasses.TestLoggingClass1;
import ru.otus.bytecode.testclasses.TestLoggingClass2;
import ru.otus.bytecode.testclasses.TestLoggingClass3;

public class LogRunner {
    public static void main(String... args) {

        // Проверка вызова без методов без аннотации - параметры не выводятся
        TestLoggingClass1 class1 = new TestLoggingClass1();
        var proxy1 = TestLoggingProxy.createProxy(class1);

        proxy1.calculation(1); // нет вывода
        proxy1.calculation(1, 2);
        proxy1.calculation(1, 2, "3");

        // Аннотация на всех методах - параметры выводятся на всех методах
        TestLoggingClass2 class2 = new TestLoggingClass2();
        var proxy2 = TestLoggingProxy.createProxy(class2);

        proxy2.calculation(1);
        proxy2.calculation(1, 2);
        proxy2.calculation(1, 2, "3");

        // Аннотация на части методов - параметры легируются только на методах
        //     calculation(int param1) и calculation(int param1, int param2, String param3)
        TestLoggingClass3 class3 = new TestLoggingClass3();
        var proxy3 = TestLoggingProxy.createProxy(class3);

        proxy3.calculation(1);
        proxy3.calculation(1, 2);
        proxy3.calculation(1, 2, "3");
    }
}
