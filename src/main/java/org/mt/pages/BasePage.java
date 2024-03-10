package org.mt.pages;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import io.qameta.allure.Attachment;
import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

import static org.mt.config.StaticConfig.Selenium.POLLING;
import static org.mt.config.StaticConfig.Selenium.TIMEOUT;

public class BasePage {

    protected WebDriver driver;
    protected WebDriverWait wait;
    protected WebDriverWait wait3second;

    protected Gson gson = new GsonBuilder().setPrettyPrinting().create();

    public BasePage(WebDriver driver) {
        this.driver = driver;
        wait = new WebDriverWait(driver, Duration.ofSeconds(TIMEOUT), Duration.ofMillis(POLLING));
        wait3second = new WebDriverWait(driver, Duration.ofSeconds(3), Duration.ofMillis(POLLING));
    }

    protected void waitUntilPageReady() {
        wait.until(d -> ((JavascriptExecutor) d).executeScript("return document.readyState").equals("complete"));
    }

    protected WebElement findElement(By by) {
        wait.until(ExpectedConditions.presenceOfElementLocated(by));
        return driver.findElement(by);
    }

    protected List<WebElement> findElements(By by) {
        wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(by));
        return driver.findElements(by);
    }

    protected <T> void scrollIntoView(T attr) {
        WebElement element = attr.getClass().getName().contains("By") ? findElement((By) attr) : (WebElement) attr;
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", element);
    }

    protected <T> String getText(T attr) {
        WebElement element = attr.getClass().getName().contains("By") ? findElement((By) attr) : (WebElement) attr;
        wait.until(ExpectedConditions.visibilityOf(element));
        return element.getText();
    }

    protected <T> List<String> getTextList(T attr) {
        List<WebElement> elementList = attr.getClass().getName().contains("By")
                ? findElements((By) attr)
                : (List<WebElement>) attr;
        List<String> textList = new ArrayList<>();
        for (WebElement element : elementList) {
            //scrollIntoView(element);
            textList.add(getText(element));
        }
        return textList;
    }

    protected <T> String getAttribute(T attr, String nameAttr) {
        WebElement element = attr.getClass().getName().contains("By") ? findElement((By) attr) : (WebElement) attr;
        wait.until(ExpectedConditions.visibilityOf(element));
        return element.getAttribute(nameAttr);
    }

    protected <T> void mouseOver(T attr) {
        WebElement element = attr.getClass().getName().contains("By") ? findElement((By) attr) : (WebElement) attr;
        wait.until(ExpectedConditions.visibilityOf(element));
        new Actions(driver).moveToElement(element).build().perform();
        sleepWhile(10);
    }

    protected <T> void click(T attr) {
        WebElement element = attr.getClass().getName().contains("By") ? findElement((By) attr) : (WebElement) attr;
        wait.until(ExpectedConditions.elementToBeClickable(element));
        element.click();
        sleepWhile(10);
    }

    /**
     * Element Click Intercepted oluşursa biraz bekleyip 5 tekrara kadar deniyor.
     */
    protected <T> void click(T attr, boolean handleInterceptedClick) {
        WebElement element = attr.getClass().getName().contains("By") ? findElement((By) attr) : (WebElement) attr;

        int loopCount = 0;
        while (handleInterceptedClick) {
            try {
                click(element);
                handleInterceptedClick = false;
            } catch (ElementClickInterceptedException ex) {
                sleepWhile(250);
                loopCount++;
                if (loopCount == 5) throw new ElementClickInterceptedException(ex.toString());
            }
        }
    }

    protected <T> void javascriptClick(T attr) {
        WebElement element = attr.getClass().getName().contains("By") ? findElement((By) attr) : (WebElement) attr;
        wait.until(ExpectedConditions.visibilityOf(element));
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", element);
        sleepWhile(10);
    }

    protected <T> void clearInput(T attr) {
        WebElement element = attr.getClass().getName().contains("By") ? findElement((By) attr) : (WebElement) attr;
        wait.until(ExpectedConditions.visibilityOf(element));
        element.clear();
        sleepWhile(10);
    }

    protected <T> void sendKeys(T attr, String value) {
        WebElement element = attr.getClass().getName().contains("By") ? findElement((By) attr) : (WebElement) attr;
        wait.until(ExpectedConditions.visibilityOf(element));
        //element.clear();
        element.sendKeys(value);
        sleepWhile(10);
    }

    protected <T> void sendKeysWithEnter(T attr, String value) {
        WebElement element = attr.getClass().getName().contains("By") ? findElement((By) attr) : (WebElement) attr;
        wait.until(ExpectedConditions.visibilityOf(element));
        //element.clear();
        element.sendKeys(Keys.chord(value, Keys.ENTER));
        sleepWhile(10);
    }

    protected <T> void selectByValue(T attr, String value) {
        WebElement element = attr.getClass().getName().contains("By") ? findElement((By) attr) : (WebElement) attr;
        wait.until(ExpectedConditions.visibilityOf(element));
        Select select = new Select(element);
        select.selectByValue(value);
        sleepWhile(10);
    }

    @Attachment(value = "{0}", type = "image/png")
    protected byte[] takeScreenshot(String title) {
        return ((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES);
    }

    /**
     * Senkronizasyonu daha kolay sağlamak için bazı işlemlerden sonra biraz bekleniyor.
     */
    protected void sleepWhile(int milisecond) {
        try {
            Thread.sleep(milisecond);
        } catch (InterruptedException ignored) { }
    }
}
