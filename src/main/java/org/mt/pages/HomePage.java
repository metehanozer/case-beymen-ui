package org.mt.pages;

import io.qameta.allure.Allure;
import io.qameta.allure.Step;
import lombok.extern.slf4j.Slf4j;
import org.mt.config.ConfigManager;
import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@Slf4j
public class HomePage extends BasePage {

    @FindBy(id = "onetrust-reject-all-handler")
    WebElement rejectAllHandlerButton;

    @FindBy(css = ".genderPopup .o-modal__closeButton")
    WebElement genderPopupCloseButton;

    @FindBy(css = ".o-navbar__link")
    List<WebElement> navBarItemList;

    @FindBy(css = ".o-header__search--input")
    WebElement searchInput;

    By cookieBanner = By.id("onetrust-banner-sdk");
    By genderPopup = By.cssSelector(".genderPopup");

    public HomePage(WebDriver driver) {
        super(driver);
        PageFactory.initElements(driver, this);
    }

    @Step("Open home page")
    public HomePage openHomePage(String homeUrl) {
        driver.get(homeUrl);
        waitUntilPageReady();
        rejectAllCookies();
        closeGenderPopup();
        return this;
    }

    @Step("Reject all cookie if exist in 3 second")
    private void rejectAllCookies() {
        try {
            wait3second.until(ExpectedConditions.visibilityOfElementLocated(cookieBanner));
            click(rejectAllHandlerButton);
        } catch (TimeoutException ignored) {
            var msg = "Cookies does't exist";
            Allure.step(msg);
            log.info(msg);
        }
    }

    @Step("Close gender popup if exist in 3 second")
    private void closeGenderPopup() {
        try {
            wait3second.until(ExpectedConditions.visibilityOfElementLocated(genderPopup));
            click(genderPopupCloseButton);
        } catch (TimeoutException ignored) {
            var msg = "Gender popup does't exist";
            Allure.step(msg);
            log.info(msg);
        }
    }

    @Step("Verify home page is open")
    public HomePage verifyHomePageIsOpen() {
        log.info("Verify home page is open");

        wait.until(driver -> navBarItemList.size() > 10);
        var expextedUrl = ConfigManager.getInstance().getStr("home.url");
        assertEquals(expextedUrl, driver.getCurrentUrl());

        takeScreenshot("Home Page");

        return this;
    }

    @Step("Click to search input")
    public SearchPage clickSearchInput() {
        click(searchInput);
        return new SearchPage(driver);
    }
}
