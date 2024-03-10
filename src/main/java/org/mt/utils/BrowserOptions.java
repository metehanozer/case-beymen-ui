package org.mt.utils;

import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxDriverLogLevel;
import org.openqa.selenium.firefox.FirefoxOptions;

import java.util.List;

public class BrowserOptions {

    public static ChromeOptions getChromeOptions() {
        System.setProperty("webdriver.chrome.silentOutput", "true");
        var chromeOptions = new ChromeOptions();
        chromeOptions.addArguments("--incognito");
        chromeOptions.setExperimentalOption("excludeSwitches", List.of("enable-automation"));
        return chromeOptions;
    }

    public static FirefoxOptions getFirefoxOptions() {
        var firefoxOptions = new FirefoxOptions();
        firefoxOptions.addArguments("-private");
        firefoxOptions.setLogLevel(FirefoxDriverLogLevel.FATAL);
        return firefoxOptions;
    }

    public static EdgeOptions getEdgeOptions() {
        var edgeOptions = new EdgeOptions();
        edgeOptions.addArguments("--incognito");
        edgeOptions.setExperimentalOption("excludeSwitches", List.of("enable-automation"));
        return edgeOptions;
    }
}

