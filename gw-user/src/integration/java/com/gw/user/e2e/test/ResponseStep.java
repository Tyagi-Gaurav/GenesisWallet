package com.gw.user.e2e.test;

import java.util.function.Function;

public interface ResponseStep extends ExecutionStep {
     TestResponse apply(TestContext testContext);
}
