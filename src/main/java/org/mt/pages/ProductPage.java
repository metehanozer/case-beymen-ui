package org.mt.pages;

import io.qameta.allure.Allure;
import io.qameta.allure.Step;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import static org.mt.config.StaticConfig.Path.TEST_DATA_PATH;
import static org.mt.utils.RandomUtil.getRandomFrom;

@Slf4j
public class ProductPage extends BasePage {

    @FindBy(css = ".o-productDetail__description")
    WebElement descLabel;

    @FindBy(id = "priceNew")
    WebElement priceLabel;

    //@FindBy(xpath = "//span[@class='m-variation__item']")
    @FindBy(xpath = "//span[contains(@class, 'm-variation__item') and not(contains(@class, 'disabled'))]")
    List<WebElement> sizeSpanList;

    @FindBy(id = "addBasket")
    WebElement addBasketButton;

    @FindBy(xpath = "//h4[text()='Sepete Eklendi']")
    WebElement basketMessagePopup;

    @FindBy(css = ".m-notification__close")
    WebElement basketMessagePopupCloseButton;

    @FindBy(css = "a[title='Sepetim']")
    WebElement basketButton;

    public ProductPage(WebDriver driver) {
        super(driver);
        PageFactory.initElements(driver, this);
        waitUntilPageReady();
    }

    @Step("Write product info to file")
    public ProductPage writeProductInfoToTextFile() {
        log.info("Write product info to file");
        var descLabelText = getText(descLabel);
        var priceLabelText = getText(priceLabel);

        var data = String.format("Product desc: %s\nProduct Price: %s", descLabelText, priceLabelText);
        writeDataToFile(data);

        takeScreenshot("Product page");

        return this;
    }

    @Step("Write data to file")
    private static void writeDataToFile(String data) {
        var txtFile = TEST_DATA_PATH + "product.txt";
        try {
            Files.write(Paths.get(txtFile), data.getBytes());
        } catch (IOException e) {
            Allure.step(e.getMessage());
            log.info(e.getMessage());
        }
    }

    @Step("Add product to basket")
    public ProductPage addProductToBasket() {
        log.info("Add product to basket");
        selectRandomSizeIfExist();
        click(addBasketButton);
        wait.until(ExpectedConditions.visibilityOf(basketMessagePopup));
        click(basketMessagePopupCloseButton);
        return this;
    }

    @Step("Select random size if exist")
    private void selectRandomSizeIfExist() {
        var size = getRandomFrom(sizeSpanList);
        click(size);
        wait.until(ExpectedConditions.attributeContains(size, "class", "-active"));
    }

    @Step("Go to basket")
    public BasketPage goToBasketPage() {
        click(basketButton);
        return new BasketPage(driver);
    }
}
