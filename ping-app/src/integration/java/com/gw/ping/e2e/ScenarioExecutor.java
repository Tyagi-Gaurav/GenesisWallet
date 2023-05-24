package com.gw.ping.e2e;

import com.gw.ping.e2e.function.SendPing;
import com.gw.test.support.executor.BaseScenarioExecutor;
import com.gw.test.support.framework.HttpResponseSpec;
import com.gw.test.support.framework.ResponseStep;

public class ScenarioExecutor extends BaseScenarioExecutor {
    public static ResponseStep aPingRequestIsSent() {
        return testContext -> {
            var responseSpec = new SendPing().apply(testContext.getWebTestClient());
            return new HttpResponseSpec(String.class, responseSpec);
        };
    }

}
