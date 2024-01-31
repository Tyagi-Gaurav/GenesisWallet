package com.gw.test.support.framework;

import org.hamcrest.Matcher;

import java.util.function.Supplier;

import static org.hamcrest.MatcherAssert.assertThat;

public class Action<T> {
        private final T t;

        public Action(T t) {
            this.t = t;
        }

        public void then(Supplier<String> input, Matcher<String> matcher) {
            assertThat(input.get(), matcher);
        }

        public T andNavigatedTo(String s) {
            return null;
        }
    }