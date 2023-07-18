package org.snowj.synthea.ingest.tests;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LogbackTest {
    private static final Logger logger = LoggerFactory.getLogger(LogbackTest.class);

    public static void main(String[] args) {
        logger.warn("Hello, World!");
        logger.error("This is a test on Logback.");
    }
}
