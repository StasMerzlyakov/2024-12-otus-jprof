package ru.otus.tester.statistics;

import java.util.Arrays;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ResultProcessorImpl implements ResultProcessor {

    private Logger logger = LoggerFactory.getLogger(ResultProcessorImpl.class);

    @Override
    public void printResults(TestResult[] results) {
        long faultCount =
                Arrays.stream(results).filter(result -> !result.result()).count();
        long allCount = results.length;
        long successCount = allCount - faultCount;
        logger.info("Success tests: {}\nFault tests: {}\nAll tests: {}\n", successCount, faultCount, allCount);
    }
}
