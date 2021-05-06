package com.sammyyx;

import com.sammyyx.common.util.LogUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Test;

public class LogTest {

    private static final Logger logger = LogManager.getLogger(LogTest.class);

    @Test
    public void testLogger() {
        logger.info("i am {},{}", "sammyyx","test");
    }
}
