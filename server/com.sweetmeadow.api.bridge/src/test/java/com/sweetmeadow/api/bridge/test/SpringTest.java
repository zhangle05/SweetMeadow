package com.sweetmeadow.api.bridge.test;

import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * @author zhangle
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"/test-context-bridge.xml", "/test-common-services.xml"})
public abstract class SpringTest {
    protected final Logger logger = LoggerFactory.getLogger(getClass());
}