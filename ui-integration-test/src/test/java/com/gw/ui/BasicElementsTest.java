package com.gw.ui;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsEqual.equalTo;

@Disabled
class BasicElementsTest extends SharedEndToEndTest {
    @Test
    void checkBasicElementsOnThePage() {
        when(mainPageIsLoaded());
        then(pageTitle(), is(equalTo("Secrets")));
    }

    @Test
    void clickingOnLoginButtonShouldShowForm() {
        //when user navigates to http://localhost:3000/secrets
        when(user(clicks(loginButtonForGoogle())));
//        then(pageContainsAnElement("div")
//                , equalTo(anElement("div")
//                        .withInnerText("Sign in with Google")));
        and(user(fills(googleUserName(), as("gaurav.tyagi@toptal.com"))));
//        and(user(fills(googlePassword(), as(""))));
    }

    private Object anElement(String div) {
        return null;
    }
}
