package com.gw.test.support.framework;

@FunctionalInterface
public interface ResponseStep extends ExecutionStep {
     TestResponse apply(TestContext testContext);
}
