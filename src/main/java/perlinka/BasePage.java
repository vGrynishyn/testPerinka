package perlinka;

import org.openqa.selenium.support.PageFactory;
import static perlinka.utils.Driver.getWebDriver;

public abstract class BasePage {

    public BasePage(){
        PageFactory.initElements(getWebDriver(), this);
    }


}
