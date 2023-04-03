package com.gw.functional.steps;

import com.gw.functional.config.TestBeanFactory;
import io.cucumber.spring.CucumberContextConfiguration;
import org.springframework.boot.test.context.SpringBootTest;

@CucumberContextConfiguration
@SpringBootTest(classes = TestBeanFactory.class)
public class GlueConfiguration {
}