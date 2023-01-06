package com.gw.functional.steps;

import com.gw.functional.resource.TestActuatorResource;
import io.cucumber.java.en.And;
import io.cucumber.java.en.When;
import org.springframework.beans.factory.annotation.Autowired;

public class MetricSteps {

    @Autowired
    private TestActuatorResource testActuatorResource;

    @When("the user service is requested for metrics endpoint")
    public void theUserServiceIsRequestedForMetricsEndpoint() {
        testActuatorResource.accessUserMetrics();
    }

    @And("the metrics data should be received")
    public void theMetricsDataShouldBeReceived() {

    }
}
