package com.gw.functional.steps;

import com.gw.functional.resource.TestMonitoringResource;
import com.gw.functional.util.ResponseHolder;
import io.cucumber.datatable.DataTable;
import io.cucumber.java8.En;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Arrays;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

public class MonitoringSteps implements En {

    @Autowired
    private TestMonitoringResource testMonitoringResource;

    @Autowired
    private ResponseHolder responseHolder;

    public MonitoringSteps() {
        When("the user service is requested for status", () -> {
            testMonitoringResource.accessUserStatus();
        });

        When("the user service is requested for metrics", () -> {
            testMonitoringResource.accessMetrics();
        });

        Then("the response should contain following metrics", (DataTable metrics) -> {
            String metricsFromServer = responseHolder.readResponse(String.class);
            assertThat(metrics.asList().stream()
                    .allMatch(metricsFromServer::contains)).isTrue();
        });

        Given("^that metrics are captured$", () -> {
            testMonitoringResource.accessMetrics();
            responseHolder.storeMetrics(responseHolder.readResponse(String.class));
        });

        Then("^total registration metrics is incremented by (\\d+)$", (Integer increment) -> {
            String oldMetrics = responseHolder.getMetrics();
            String newMetrics = responseHolder.readResponse(String.class);

            Double user_registration_count_old = getValue(oldMetrics, "user_registration_count_total", "source", "WEB", "type", "HOMEPAGE");
            Double user_registration_count_new = getValue(newMetrics, "user_registration_count_total");

            assertThat(user_registration_count_new - user_registration_count_old).isEqualTo(1.0);
        });
    }

    private Double getValue(String metricsStream, String metricName, String... tags) {
        String fullMetricName = getFullMetricName(metricName, tags);
        return Arrays.stream(metricsStream.split("\n"))
                .filter(s -> s.startsWith(fullMetricName))
                .findFirst()
                .map(this::extractValue)
                .orElse(0.0);
    }

    private String getFullMetricName(String metricName, String[] tags) {
        StringBuilder stringBuilder = new StringBuilder(metricName);
        if (tags != null && tags.length > 0) {
            if (tags.length % 2 != 0) {
                throw new IllegalArgumentException("Tags should have key/value pair");
            }
            int i = 0;
            stringBuilder.append("{");
            while (i < tags.length) {
                stringBuilder.append(tags[i]).append("=").append("\"").append(tags[i+1]).append("\",");
                i+=2;
            }
            stringBuilder.append("}");
        }

        return stringBuilder.toString();
    }

    private Double extractValue(String s) {
        int lastEqualIndex = s.lastIndexOf(" ");
        if (lastEqualIndex < 0) {
            return 0.0;
        }
        return Double.valueOf(s.substring(lastEqualIndex));
    }
}
