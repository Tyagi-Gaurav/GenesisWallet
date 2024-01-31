package com.gw.ui;

import com.gw.test.support.framework.GenesisTestExtension;
import com.gw.test.support.framework.WithSyntacticSugar;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;

@ExtendWith(GenesisTestExtension.class)
class BasicElementsTest extends SharedEndToEndTest implements WithSyntacticSugar {
    @Test
    void checkBasicElementsOnThePage() {
        when(mainPageIsLoaded()).then(pageTitle(), is(equalTo("Secrets")));
    }

    @Test
    void clickingOnLoginButtonShouldShowForm() {
        when(user(clicks(loginButton())))
            .then(innerTextFor(headingWithTitle("login")), is(equalTo("Login")));
    }
}
