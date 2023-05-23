package com.gw.ping.e2e;

import com.gw.ping.e2e.function.SendPing;
import com.gw.test.support.framework.HttpResponseSpec;
import com.gw.test.support.framework.ResponseStep;
import com.gw.test.support.framework.VoidStep;
import org.assertj.core.api.Assertions;
import org.assertj.core.matcher.AssertionMatcher;
import org.hamcrest.Matcher;

public class ScenarioExecutor {
    public static ResponseStep aPingRequestIsSent() {
        return testContext -> {
            var responseSpec = new SendPing().apply(testContext.getWebTestClient());
            return new HttpResponseSpec(String.class, responseSpec);
        };
    }

    public static VoidStep aHttpResponse(Matcher<Integer> statusMatcher) {
        return testContext -> {
            if (testContext.getLastResponse() instanceof HttpResponseSpec httpResponseSpec) {
                httpResponseSpec.matchStatus(statusMatcher);
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
}
