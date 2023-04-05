package com.gw.ui.functional;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

class OauthTest {
    private WebDriver driver;

    @BeforeEach
    void setUp() throws MalformedURLException {
        ChromeOptions browserOptions = new ChromeOptions();
        browserOptions.setPlatformName("Linux");
        browserOptions.setBrowserVersion("111.0");
        browserOptions.addArguments("headless");
        browserOptions.addArguments("--remote-allow-origins=*");
        Map<String, Object> cloudOptions = new HashMap<>();
        cloudOptions.put("build", 1);
        cloudOptions.put("name", "OauthTest");
        browserOptions.setCapability("cloud:options", cloudOptions);
        driver = new RemoteWebDriver(new URL("http://localhost:4444"), browserOptions);
    }

    @AfterEach
    void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }

    @Test
    void shouldGetLogin() {
        driver.get("http://local.api-gateway/gw-ui/index.html");
        assertThat(driver.getTitle()).isEqualTo("Oauth 2 Demo");
        WebElement element = driver.findElement(By.linkText("Sign-In with Google"));
        element.click();

        assertThat(driver.getTitle()).contains("Sign in").contains("Google Accounts");
    }
}
