package org.mt.pages;

import io.qameta.allure.Step;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import java.util.List;

import static org.mt.utils.RandomUtil.getRandomFrom;

@Slf4j
public class SearchPage extends BasePage {

    @FindBy(css = ".o-productList__item")
    List<WebElement> productList;

    @FindBy(id = "o-searchSuggestion__input")
    WebElement searchInput;

    public SearchPage(WebDriver driver) {
        super(driver);
        PageFactory.initElements(driver, this);
        waitUntilPageReady();
    }

    @Step("Search {keyword}")
    public SearchPage search(String keyword) {
        log.info("Search " + keyword);
        sendKeys(searchInput, keyword);
        return this;
    }

    @Step("Search {keyword} and hit enter")
    public SearchPage searchAndHitEnter(String keyword) {
        log.info("Search " + keyword + " and hit enter");
        sendKeysWithEnter(searchInput, keyword);
        return this;
    }

    @Step("Clear search input")
    public SearchPage clearSearchInput() {
        log.info("Clear search input");
        clearInput(searchInput);
        return this;
    }

    @Step("Select random product")
    public ProductPage selectRandomProduct() {
        log.info("Select random product");
        var product = getRandomFrom(productList);
        click(product);
        return new ProductPage(driver);
    }
}
