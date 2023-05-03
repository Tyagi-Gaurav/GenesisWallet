package com.gw.user.e2e.test;

public interface TestResponse<V> {
    String responseKey();

    V responseValue();
}
