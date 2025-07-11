package pages;

import base.BaseClass;
import io.appium.java_client.pagefactory.AndroidFindBy;
import io.appium.java_client.pagefactory.AppiumFieldDecorator;
import io.appium.java_client.pagefactory.iOSXCUITFindBy;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;

public class DrawingPage extends BaseClass {

    public DrawingPage() {
        PageFactory.initElements(new AppiumFieldDecorator(getDriver()), this);
    }

    // NOTE: Sample placeholder is created for Android locator values

    @iOSXCUITFindBy(xpath = "//XCUIElementTypeStaticText[@name='DRAWING']")
    @AndroidFindBy(id = "TBU")
    private WebElement drawingScreen;

    @iOSXCUITFindBy(xpath = "//XCUIElementTypeOther[@name='test-SAVE']")
    @AndroidFindBy(id = "TBU")
    private WebElement saveButton;

    @iOSXCUITFindBy(xpath = "//XCUIElementTypeStaticText[@label='Save drawing']")
    @AndroidFindBy(id = "TBU")
    private WebElement saveDrawingText;

    @iOSXCUITFindBy(accessibility = "OK")
    @AndroidFindBy(id = "TBU")
    private WebElement okButton;

    public boolean isDrawingScreenVisible(){
        return isElementVisible(drawingScreen);
    }

    public boolean isSaveButtonVisible(){
        return isElementVisible(saveButton);
    }

    public boolean isSaveDrawingTextVisible(){
        return isElementVisible(saveDrawingText);
    }

    public boolean isOkButtonVisible(){
        return isElementVisible(okButton);
    }

    public void tapOnOkButton(){
        clickOnElement(okButton);
    }

    public void tapOnSaveButton(){
        clickOnElement(saveButton);
    }






}
