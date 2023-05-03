package com.gw.user.e2e.test;

public class DatabaseResponseSpec<V> implements TestResponse<V> {
    private final String responseKey;
    private final V responseSpec;

    public DatabaseResponseSpec(Class<?> payloadClass, V object) {
        this.responseKey = payloadClass.getSimpleName();
        this.responseSpec = object;
    }

    @Override
    public String responseKey() {
        return responseKey;
    }

    @Override
    public V responseValue() {
        return responseSpec;
    }
}
