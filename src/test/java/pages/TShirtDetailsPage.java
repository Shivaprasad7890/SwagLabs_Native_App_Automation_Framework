package pages;

import base.BaseClass;
import io.appium.java_client.pagefactory.AndroidFindBy;
import io.appium.java_client.pagefactory.AppiumFieldDecorator;
import io.appium.java_client.pagefactory.iOSXCUITFindBy;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;

public class TShirtDetailsPage extends BaseClass {

    // NOTE: Sample placeholder is created for Android locator values

    @iOSXCUITFindBy(accessibility = "assets/src/img/bolt-shirt.jpg")
    @AndroidFindBy(id = "TBU")
    private WebElement boltShirtImage;


    public TShirtDetailsPage() {
        PageFactory.initElements(new AppiumFieldDecorator(getDriver()), this);
    }

    public boolean isBoltShirtImageVisible() {
        return isElementVisible(boltShirtImage);
    }

    public WebElement getBoltShirtImageElement() {
        return boltShirtImage;
    }
}