package com.gw.user.e2e.test;

@FunctionalInterface
public interface VoidStep extends ExecutionStep {
     void apply(TestContext testContext);
}
