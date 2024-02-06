package com.gw.test.support.executor;

import com.gw.test.support.framework.HttpResponseSpec;
import com.gw.test.support.framework.ResponseStep;
import com.gw.test.support.framework.VoidStep;
import com.gw.test.support.function.SendStatus;
import org.assertj.core.api.Assertions;
import org.assertj.core.matcher.AssertionMatcher;
import org.hamcrest.Matcher;

public class BaseScenarioExecutor {
    public static ResponseStep aStatusRequestIsSent() {
        return testContext -> {
            var responseSpec = new SendStatus().apply(testContext.getWebTestClient());
            return new HttpResponseSpec(String.class, responseSpec);
        };
    }


    public static VoidStep aHttpResponse(Matcher<Integer> statusMatcher) { //TODO Refactor to work on any matcher or return a matcher builder with this matcher
        return testContext -> {
            if (testContext.getLastResponse() instanceof HttpResponseSpec httpResponseSpec) {
                httpResponseSpec.matchStatus(statusMatcher);
            } else {
                Assertions.fail("Invalid response type detected for Http Response");
            }
        };
    }

    public static VoidStep withHttpResponseBody(String body) { //TODO Refactor to work on any matcher or return a matcher builder with this matcher
        return testContext -> {
            if (testContext.getLastResponse() instanceof HttpResponseSpec httpResponseSpec) {
                Assertions.assertThat(httpResponseSpec.responseValue()).isEqualTo(body);
            } else {
                Assertions.fail("Invalid response type detected for Http Response");
            }
        };
    }

    public static Matcher<Integer> withStatus(int statusCode) {
        return new AssertionMatcher<>() {
            @Override
            public void assertion(Integer actual) throws AssertionError {
                Assertions.assertThat(actual).isEqualTo(statusCode);
            }
        };
    }

    public static Matcher<Integer> withBody(String body) {
        return new AssertionMatcher<>() {
            @Override
            public void assertion(Integer actual) throws AssertionError {
                Assertions.assertThat(actual).isEqualTo(body);
            }
        };
    }
}
