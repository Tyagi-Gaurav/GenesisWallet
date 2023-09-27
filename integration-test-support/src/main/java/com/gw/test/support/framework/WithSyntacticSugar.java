package com.gw.test.support.framework;

public interface WithSyntacticSugar {
    default <T> T isReceived(T t) {
        return t;
    }
    default <T> T with(T t) {
        return t;
    }
    default <T> T using(T t) {
        return t;
    }
}
