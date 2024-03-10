package org.mt.pages;

import io.qameta.allure.Allure;
import io.qameta.allure.Step;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;

import java.io.File;
import java.util.List;
import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mt.config.StaticConfig.Path.TEST_DATA_PATH;

@Slf4j
public class BasketPage extends BasePage {

    @FindBy(id = "emtyCart")
    WebElement emptyBasketDiv;

    @FindBy(css = ".priceBox__salePrice")
    WebElement salePriceLabel;

    @FindBy(id = "quantitySelect0-key-0")
    WebElement quantitySelect;

    @FindBy(css = ".m-basket__remove")
    WebElement basketRemoveButton;

    @FindBy(xpath = "//h4[text()='Sepetiniz Güncellenmiştir']")
    WebElement updateMessagePopup;

    @FindBy(xpath = "//h4[text()='Ürün Silindi']")
    WebElement deleteMessagePopup;

    @FindBy(css = ".m-basket__item")
    List<WebElement> basketItemList;

    public BasketPage(WebDriver driver) {
        super(driver);
        PageFactory.initElements(driver, this);
        waitUntilPageReady();
    }

    @Step("Verify product sale price")
    public BasketPage verifyProductSalePrice() {
        log.info("Verify product sale price");

        waitUntilBasketNotEmpty();

        var salePrice = getText(salePriceLabel);
        var msg = "Sale Price: " + salePrice;
        Allure.step(msg);
        log.info(msg);

        var expectedPrice = readPriceFromTxtFile();
        var msg2 = "Expected Price From Txt File: " + expectedPrice;
        Allure.step(msg2);
        log.info(msg2);

        assertEquals(expectedPrice, salePrice.replace(",00", ""));

        return this;
    }

    @SneakyThrows
    private String readPriceFromTxtFile() {
        var txtFile = TEST_DATA_PATH + "product.txt";
        var file = new File(txtFile);
        var scanner = new Scanner(file);

        while (scanner.hasNextLine()) {
            var line = scanner.nextLine();
            if (line.startsWith("Product Price: ")) {
                return line.replace("Product Price: ", "");
            }
        }
        throw new Error("Product price not found in txt file!");
    }

    @Step("Wait until basket not empty")
    private void waitUntilBasketNotEmpty() {
        wait.until(d -> !basketItemList.isEmpty());
        sleepWhile(100);
    }

    @Step("Select product quantity")
    public BasketPage selectproductQuantity(Integer quantity) {
        log.info("Select product quantity");

        var quantityList = new Select(quantitySelect).getOptions();

        if (quantityList.size() > 1) {
            var firstSalePrice = getText(salePriceLabel);
            selectByValue(quantitySelect, quantity.toString());
            wait.until(ExpectedConditions.visibilityOf(updateMessagePopup));
            wait.until((ExpectedCondition<Boolean>) d -> (Boolean) !getText(salePriceLabel).equals(firstSalePrice));

            var quantitySelectLabel = getAttribute(quantitySelect, "aria-label");
            var msg = "Quantity Select Label: " + quantitySelectLabel;
            Allure.step(msg);
            log.info(msg);

            assertEquals(quantity + " adet", quantitySelectLabel);

        } else {
            var msg = "Does not select quantity! Because critical stock.";
            Allure.step(msg);
            log.info(msg);
        }

        return this;
    }

    @Step("Clear basket and verify")
    public void clearBasketAndVerify() {
        log.info("Clear basket and verify");
        click(basketRemoveButton);
        wait.until(ExpectedConditions.visibilityOf(deleteMessagePopup));
        wait.until(ExpectedConditions.visibilityOf(emptyBasketDiv));
    }
}
