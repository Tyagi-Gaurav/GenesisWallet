package com.gw.test.support.framework;

import org.hamcrest.Matcher;

import java.util.function.Supplier;

import static org.hamcrest.MatcherAssert.assertThat;

public class GenericTestState {
    protected <T> void then(Supplier<T> input, Matcher<? super T> matcher) {
        assertThat(input.get(), matcher);
    }
}
