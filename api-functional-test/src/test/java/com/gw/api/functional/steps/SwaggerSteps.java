package com.gw.api.functional.steps;

import com.gw.api.functional.resource.TestSwaggerResource;
import io.cucumber.java.en.Given;
import org.springframework.beans.factory.annotation.Autowired;

public class SwaggerSteps {
    @Autowired
    private TestSwaggerResource testSwaggerResource;

    @Given("a client attempts to access swagger")
    public void aClientAttemptsToAccessSwagger() {
        testSwaggerResource.accessSwagger();
    }
}
