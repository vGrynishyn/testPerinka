package perlinka.browser;

import io.github.bonigarcia.wdm.ChromeDriverManager;
import org.openqa.selenium.WebDriver;
import perlinka.utils.Driver.BrowserType;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import perlinka.utils.Driver;

public class Chrome {
    private WebDriver driver;

    public WebDriver getDriver(){
        return this.driver;
    }

    public Chrome(){
        ChromeDriverManager.getInstance().setup();
        ChromeOptions options = new ChromeOptions();
        options.addArguments(Driver.BrowserType.findByName(BrowserType.CHROME.getName()).getOptions());
        this.driver = new ChromeDriver(options);
    }
}
