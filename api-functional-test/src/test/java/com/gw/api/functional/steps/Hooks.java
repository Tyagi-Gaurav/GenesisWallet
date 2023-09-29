package com.gw.api.functional.steps;

import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.Scenario;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Hooks {
    private static final Logger LOG = LogManager.getLogger("APP");
    private static final Logger FAILED_LOG = LogManager.getLogger("FAILED");
    private static final Logger PASSED_LOG = LogManager.getLogger("PASSED");

    @Before
    public void before(Scenario scenario) {
        LOG.info("[STARTING] : " + scenario.getName());
    }

    @After
    public void afterScenario(Scenario scenario) {
        if (scenario.isFailed()) {
            FAILED_LOG.error(scenario.getName());
        } else {
            PASSED_LOG.info(scenario.getName());

        }
    }
}