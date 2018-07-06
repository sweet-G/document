package com.kaishengit.dao;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Log4jTestCase {

	Logger logger = LoggerFactory.getLogger(Log4jTestCase.class);
	
	@Test
	public void testLevel() {
		logger.trace("{} login {}","tom","success");
		logger.debug("debug message");
		logger.info("{} login {}","tom","success");
		logger.warn("warn message");
		logger.error("error message");
//		logger.fatal("fatal message");
	}
	
}
