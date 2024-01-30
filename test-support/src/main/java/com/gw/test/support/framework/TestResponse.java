package com.gw.test.support.framework;

public interface TestResponse<V> {
    String responseKey();

    V responseValue();
}
