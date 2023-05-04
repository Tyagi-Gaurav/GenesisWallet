package com.gw.test.support.framework;

@FunctionalInterface
public interface VoidStep extends ExecutionStep {
     void apply(TestContext testContext);
}
