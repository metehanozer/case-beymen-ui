import io.qameta.allure.Allure;
import io.qameta.allure.Step;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.mt.config.ConfigManager;
import org.mt.pages.HomePage;
import org.mt.utils.DriverFactory;

import java.time.Duration;

import static org.mt.config.StaticConfig.Selenium.TIMEOUT;

public class BaseTest {

    @BeforeEach
    public void classLevelSetup() {
        DriverFactory.setupWebDriver();
        var driver = DriverFactory.getDriver();
        driver.manage().window().maximize();
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(TIMEOUT));
        Allure.step("Driver thread: " + Thread.currentThread().getId());

    }

    @Step("Get home page")
    protected HomePage getHomePage() {
        var homeUrl = ConfigManager.getInstance().getStr("home.url");
        return new HomePage(DriverFactory.getDriver()).openHomePage(homeUrl);
    }


    @AfterEach
    public void teardown() {
        Allure.step("driver thread: " + Thread.currentThread().getId());
        DriverFactory.getDriver().manage().deleteAllCookies();
        DriverFactory.tearDownWebDriver();
    }
}
