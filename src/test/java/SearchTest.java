import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import org.junit.jupiter.api.Test;
import org.mt.utils.ExcelUtil;

import java.io.File;

import static org.mt.config.StaticConfig.Path.TEST_DATA_PATH;

@Epic("Regression Tests")
@Feature("Search Tests")
public class SearchTest extends BaseTest {

    @Test
    @Story("Search Product")
    @Description("Customer search product and add to basket.")
    public void checkSearch() {
        var excelFile = new File(TEST_DATA_PATH + "search_data.xlsx");
        var excelUtil = new ExcelUtil();

        excelUtil.readFile(excelFile, "sheet1");
        var dataList = excelUtil.getDataList();
        var keyword1 = dataList.get(0).get(0);
        var keyword2 = dataList.get(0).get(1);

        getHomePage()
                .verifyHomePageIsOpen()
                .clickSearchInput()
                .search(keyword1.toString())
                .clearSearchInput()
                .searchAndHitEnter(keyword2.toString())
                .selectRandomProduct()
                .writeProductInfoToTextFile()
                .addProductToBasket()
                .goToBasketPage()
                .verifyProductSalePrice()
                .selectproductQuantity(2)
                .clearBasketAndVerify();
    }
}
