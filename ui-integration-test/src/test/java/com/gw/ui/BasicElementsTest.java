package com.gw.ui;

import org.junit.jupiter.api.Test;

import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsEqual.equalTo;

class BasicElementsTest extends SharedEndToEndTest {
    @Test
    void checkBasicElementsOnThePage() {
        when(mainPageIsLoaded());
        then(pageTitle(), is(equalTo("Secrets")));
    }

    @Test
    void clickingOnLoginButtonShouldShowForm() {
        when(user(clicks(loginButton())));
        then(innerTextFor(headingWithTitle("login")), is(equalTo("Login")));
    }
}
