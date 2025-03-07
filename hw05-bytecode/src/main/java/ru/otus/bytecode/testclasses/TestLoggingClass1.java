package ru.otus.bytecode.testclasses;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.bytecode.ITestLogging;

public class TestLoggingClass1 implements ITestLogging {

    private static final Logger logger = LoggerFactory.getLogger(TestLoggingClass1.class);

    @Override
    public void calculation(int param1) {
        logger.info("calculation(param1)");
    }

    @Override
    public void calculation(int param1, int param2) {
        logger.info("calculation(param1, param2)");
    }

    @Override
    public void calculation(int param1, int param2, String param3) {
        logger.info("calculation(param1, param2, param3)");
    }
}
