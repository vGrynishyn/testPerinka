import org.openqa.selenium.WebElement;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;
import perlinka.pageobjects.SalesPage;
import perlinka.utils.Browser;

import java.util.ArrayList;

public class LocalTest {

    private SoftAssert softAssert = new SoftAssert();
    private static final String NUM_OF_ITEMS = "3";
    private static final String GIRLS_SHOES_FILTER = "GirlShoes";
    private static final String BOYS_DEMISESON_FILTER = "BoysDemisezon";
    private static final String BOYS_WINTER_FILTER = "BoysWinter";

    @AfterMethod
    public static void afterMethod(){
        Browser.closeBrowser();
    }

    /*
    * 1. в разделе Распродажи проверить на трех товарах есть ли в карточке товара акционная цена
    * Для этого, рандомно, на первой страничке выбрать товар, провалиться в карточку товара и проверить наличие цены
    * Так для 3-х товаров (выбор количества проверяемых товаров сделать гибким)
    */

    @Test
    @Parameters("numOfItems")
    public void checkIsSalesPriseExists(@Optional(NUM_OF_ITEMS) String numOfItems) {
        ArrayList<Integer> randomIndex = new SalesPage().openSalesPage().getRandomItems(Integer.parseInt(numOfItems));
        boolean actualSalesPriceExisrs;
        for (int i : randomIndex) {
            actualSalesPriceExisrs = new SalesPage().checkSalesPrice(i);
            softAssert.assertEquals(actualSalesPriceExisrs, true, String.format("Sales prise not present for item %d - ", i));
        }
    }

    /*
     * 2. Применить фильтр "Девочка - туфли"
     * Проверить что у всех товаров этого раздела есть цена
     */
    @Test
    public void checkIsPricePresentForAllItems() {
        int numOfPages = new SalesPage().openSalesPage().selectFilters(new String[]{GIRLS_SHOES_FILTER}).getPagesNum();
        for (int i = 0; i < numOfPages; i++) {
            ArrayList<WebElement> items = new SalesPage().getItemElements();
            for (WebElement item : items) {
                boolean bActualPriceExists = new SalesPage().checkPriceExists(item);
                softAssert.assertEquals(bActualPriceExists, true, String.format("Price does not exist for the following item(s): %s", item.getText()));
            }
            new SalesPage().clickSeeMoreButton();
        }
    }

    /*
     * 3. Применить фильтр "Мальчик - Демисезон"
     * Вывести "Номер для заказа" всех товаров у которых название не "Кроссовки"
     */
    @Test
    public void printOrderNumberIfNotSneakers() {
        int numOfPages = new SalesPage().openSalesPage().selectBoysDemisezonFilter().getPagesNum();
        for (int i = 0; i < numOfPages; i++) {
            ArrayList<WebElement> items = new SalesPage().getItemElements();
            for (WebElement item : items) {
                boolean bActualPriceExists = new SalesPage().checkOrderNum(item);
                softAssert.assertEquals(bActualPriceExists, true, String.format("Sneakers are present for the following item(s): %s", item.getText()));
            }
            new SalesPage().clickSeeMoreButton();
        }
    }

    /*
     * 4. Применить фильтр по двум видам товаров
     * Сравнить общее количество товаров по двум видам с суммой каждого по отдельности
     * Если будут найдены отличия, вывести эти товары в лог упавшего теста
     */
     @Test
     public void checkTwoFiltersForItems() {
         ArrayList<String> expectedFilteredItems = new ArrayList<>();
         ArrayList<String> actualfilterItems = new ArrayList<>();
         int expectetNumOfItemsForTwoFilters = 0;
         int actualNumOfItemsForTwoFilters  = 0;

         int numOfPages = new SalesPage().openSalesPage().selectFilters(new String[]{BOYS_DEMISESON_FILTER, BOYS_WINTER_FILTER}).getPagesNum();
         expectedFilteredItems = new SalesPage().getExpectedFilterItems(numOfPages);
         expectetNumOfItemsForTwoFilters = expectedFilteredItems.size();

         numOfPages = new SalesPage().openSalesPage().selectFilters(new String[]{BOYS_DEMISESON_FILTER}).getPagesNum();
         actualfilterItems.addAll(new SalesPage().getExpectedFilterItems(numOfPages));

         numOfPages = new SalesPage().openSalesPage().selectFilters(new String[]{BOYS_WINTER_FILTER}).getPagesNum();
         actualfilterItems.addAll(new SalesPage().getExpectedFilterItems(numOfPages));
         actualNumOfItemsForTwoFilters=actualfilterItems.size();

         if (expectetNumOfItemsForTwoFilters != actualNumOfItemsForTwoFilters) {
             ArrayList<String> resultList = new SalesPage().veryfyFilteredLists(expectedFilteredItems, actualfilterItems);
             int sizeOfResList = resultList.size();
             softAssert.assertEquals(sizeOfResList, 0);
             System.out.println(resultList);
         }
     }
}
