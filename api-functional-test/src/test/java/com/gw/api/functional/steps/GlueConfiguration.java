package com.gw.api.functional.steps;

import com.gw.api.functional.config.TestBeanFactory;
import io.cucumber.spring.CucumberContextConfiguration;
import org.springframework.boot.test.context.SpringBootTest;

@CucumberContextConfiguration
@SpringBootTest(classes = TestBeanFactory.class)
public class GlueConfiguration {
}