package org.mt.utils;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

import java.util.Optional;

public class DriverFactory {

    private DriverFactory() {
    }

    private static final ThreadLocal<WebDriver> driver = new ThreadLocal<>();
    private static final String ERROR_MSG = "WebDriver instance error for thread " + Thread.currentThread().getId();

    public static WebDriver getDriver() {
        return Optional.ofNullable(driver.get()).orElseThrow(() -> new IllegalStateException(ERROR_MSG));
    }

    public static void setupWebDriver() {
        driver.set(createBrowserInstance());
    }

    public static void tearDownWebDriver() {
        Optional.ofNullable(driver.get()).orElseThrow(() -> new IllegalStateException(ERROR_MSG)).quit();
        driver.remove();
    }

    private static WebDriver createBrowserInstance() {
         var chromeOptions = BrowserOptions.getChromeOptions();
         return new ChromeDriver(chromeOptions);
    }
}

