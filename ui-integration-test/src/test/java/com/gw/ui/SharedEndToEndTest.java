package com.gw.ui;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.Playwright;
import com.microsoft.playwright.options.AriaRole;
import org.hamcrest.Matcher;
import org.junit.jupiter.api.BeforeAll;

import java.util.function.Supplier;

import static org.hamcrest.MatcherAssert.assertThat;

public abstract class SharedEndToEndTest {
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

    protected Supplier<Locator> loginButton() {
        return () -> page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("login"));
    }

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

    protected static class Action<T> {
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
}
