package ru.otus.bytecode.testclasses;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.bytecode.ITestLogging;
import ru.otus.bytecode.Log;

public class TestLoggingClass2 implements ITestLogging {

    private static final Logger logger = LoggerFactory.getLogger(TestLoggingClass2.class);

    @Log
    @Override
    public void calculation(int param1) {
        logger.info("calculation(param1)");
    }

    @Log
    @Override
    public void calculation(int param1, int param2) {
        logger.info("calculation(param1, param2)");
    }

    @Log
    @Override
    public void calculation(int param1, int param2, String param3) {
        logger.info("calculation(param1, param2, param3)");
    }
}
