package perlinka.pageobjects;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import perlinka.BasePage;
import perlinka.utils.Browser;
import perlinka.utils.LogInformation;

import java.util.ArrayList;
import java.util.List;

public class SalesPage extends BasePage {

    @FindBy(xpath = "//*[@class='item item__discont']")
    private List<WebElement> discountItemtLinks;
    @FindBy(xpath = "//*[contains(@class, 'item ')]")
    private List<WebElement> itemtLinks;
    @FindBy(xpath = ".//*[@id='menu']/li[4]/a")
    private WebElement menuSales;
    @FindBy(className= "item-details__new-price")
    private WebElement newPrice;
    @FindBy(xpath = ".//*[@class='filter']/div[2]/ul/li[5]/label")
    private WebElement filterGirlShoes;
    @FindBy(xpath = ".//*[@class='filter']/div[1]/ul/li[1]/label")
    private WebElement filterBoysDemisezon;
    @FindBy(xpath = ".//*[@class='filter']/div[1]/ul/li[2]/label")
    private WebElement filterBoysWinter;
    @FindBy(xpath = ".//*[@class='filter__buttons']/button")
    private WebElement applyFilterButton;
    @FindBy(xpath = ".//*[@class='btn btn__big']")
    private WebElement seeMoreButton;
    @FindBy(xpath = ".//*[@class='pager']/ul/li")
    private List<WebElement> pageButtons;
    @FindBy(xpath = ".//*[@class='item__price']/b")
    private WebElement priceLabel;
    @FindBy (className = "label__checked")
    private List<WebElement> checkBoxChecked;


    public SalesPage openSalesPage(){
        String url = "https://www.perlinka.ua/index.php/view/category/category_id/8/s/rasprodazha";
        LogInformation.info(String.format("Open '%s' address", url));
        Browser.openWebPage(url);
        return this;
    }

    public ArrayList<Integer> getRandomItems(int numElements){
        ArrayList<Integer> randomElements = new ArrayList<>();
        int itemsNum = discountItemtLinks.size();
        for (int i=0; i<numElements; i++) {
            int index = (int) (Math.random()*itemsNum);
            randomElements.add((index));
        }
        return randomElements;
    }

    public boolean checkSalesPrice(int numElements) {
        boolean isSalePresent= false;
        WebElement element = getElement(numElements);
        element.click();
        if (newPrice.isDisplayed()) {
            isSalePresent= true;
        }
        menuSales.click();
        return isSalePresent;
    }

    public SalesPage selectBoysDemisezonFilter(){
        if(filterBoysDemisezon.isDisplayed())
            filterBoysDemisezon.click();
        applyFilterButton.click();
        return this;
    }

    public SalesPage selectFilters(String[]filter){
        for (String s : filter) {
            switch (s) {
                case "GirlShoes":
                    filterGirlShoes.click();
                    break;
                case "BoysDemisezon":
                    filterBoysDemisezon.click();
                    break;
                case "BoysWinter":
                    filterBoysWinter.click();
                    break;
            }
        }
        applyFilterButton.click();
        return this;

    }

    public ArrayList<WebElement> getItemElements(){
        ArrayList<WebElement> linkTexts = new ArrayList<>();
        linkTexts.addAll(itemtLinks);
        return linkTexts;
    }

    public int getPagesNum(){
        return pageButtons.size();
    }

    public boolean checkPriceExists(WebElement element){
        boolean bExists=false;
        String[] resArray = element.getText().split("\n");
        if (resArray[4].matches("\\d*-?(\\d*) грн.")){
            bExists=true;
        }
        return bExists;
    }

    public boolean checkOrderNum(WebElement element){
        boolean bExists=false;
        String[] resArray = element.getText().split("\n");
        if (!resArray[0].matches("Кроссовки|Кросівки")){
            System.out.println(resArray[1]);
            bExists=true;
        }
        return bExists;
    }

    public void clickSeeMoreButton(){
        if (isElementExists(seeMoreButton)){
            seeMoreButton.click();}
    }

    private boolean isElementExists(WebElement element) {
        try {
            element.isDisplayed();
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public ArrayList<String> veryfyFilteredLists(ArrayList<String> expectedItems, ArrayList<String> actualItems) {
        ArrayList<String> rsult = new ArrayList<>();
        if(expectedItems.size() > actualItems.size()) {
            expectedItems.removeAll(actualItems);
            return expectedItems;
        }
        else {
            actualItems.removeAll(expectedItems);
            return actualItems;
        }
    }

    public ArrayList<String> getExpectedFilterItems(int numOfPages){
        ArrayList<String> expectedFilteredItems = new ArrayList<>();
        for (int i = 0; i < numOfPages; i++) {
            ArrayList<String> allItems = getItemText();
            expectedFilteredItems.addAll(allItems);
            clickSeeMoreButton();
        }
        return expectedFilteredItems;
    }

    private WebElement getElement(int num) {
        ArrayList<WebElement> linkTexts = new ArrayList<>();
        linkTexts.addAll(discountItemtLinks);
        return linkTexts.get(num);
    }

    private ArrayList<String> getItemText(){
        ArrayList<String> linkTexts = new ArrayList<>();
        for (WebElement element: itemtLinks){
            linkTexts.add(element.getText());
        }
        return linkTexts;
    }
}
