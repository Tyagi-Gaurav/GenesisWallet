package com.gw.functional;

import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;
import org.junit.BeforeClass;
import org.junit.runner.RunWith;

@RunWith(Cucumber.class)
@CucumberOptions(
        features = "classpath:features",
        plugin = {"pretty", "json:target/cucumber-report.json"},
        tags = "not @Migrated",
        glue = {"com.gw.functional.steps"},
        monochrome = true
)
public class CucumberTest {
}

