package com.gw.ui;

import com.gw.test.support.framework.Action;
import com.gw.test.support.framework.GenericTestState;
import com.gw.test.support.framework.WithSyntacticSugar;
import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.Playwright;
import com.microsoft.playwright.options.AriaRole;
import org.junit.jupiter.api.BeforeAll;

import java.util.function.Supplier;

public abstract class SharedEndToEndTest extends GenericTestState implements WithSyntacticSugar {
    protected static Page page;

    @BeforeAll
    static void beforeAll() {
        final var playwright = Playwright.create();
        final var browser = playwright.chromium().launch();
        page = browser.newPage();
        page.setDefaultTimeout(2000);
        page.navigate("http://localhost:3000");
    }

    protected Action<Page> mainPageIsLoaded() {
        return new Action(page);
    }

    protected Supplier<Locator> loginButtonForGoogle() {
        return () -> page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Sign In with Google"));
    }

    protected Supplier<Locator> googleUserName() {
        return () -> page.getByTestId("identifierId");
    }

//    protected Supplier<Locator> googlePassword() {
//        return () -> page.getByRole(AriaRole.TEXTBOX, Page.GetByRoleOptions);
//    }

    protected Supplier<Locator> headingWithTitle(String title) {
        return () -> page.getByRole(AriaRole.HEADING, new Page.GetByRoleOptions().setName(title));
    }

    protected Supplier<String> innerTextFor(Supplier<Locator> element) {
        return () -> element.get().innerText();
    }

    protected Supplier<String> pageTitle() {
        return () -> page.title();
    }

    Action<Void> clicks(Supplier<Locator> locatorSupplier) {
        locatorSupplier.get().click();
        return new Action(Void.TYPE);
    }

    Action<Void> fills(Supplier<Locator> locatorSupplier, String text) {
        locatorSupplier.get().fill(text);
        return new Action(Void.TYPE);
    }
}
