package pages;

import base.BaseClass;
import io.appium.java_client.pagefactory.AndroidFindBy;
import io.appium.java_client.pagefactory.AppiumFieldDecorator;
import io.appium.java_client.pagefactory.iOSXCUITFindBy;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;
import org.testng.Assert;

public class LoginPage extends BaseClass {

    // NOTE: Sample placeholder is created for Android locator values

    @iOSXCUITFindBy(accessibility = "test-LOGIN")
    @AndroidFindBy(id = "TBU")
    private WebElement loginButton;

    @iOSXCUITFindBy(accessibility = "test-Username")
    @AndroidFindBy(id = "TBU")
    private WebElement usernameTextField;

    @iOSXCUITFindBy(accessibility = "test-Password")
    @AndroidFindBy(id = "TBU")
    private WebElement passwordTextField;

    @iOSXCUITFindBy(accessibility = "test-standard_user")
    @AndroidFindBy(id = "TBU")
    private WebElement autofillCredentials;

    public LoginPage() {
        PageFactory.initElements(new AppiumFieldDecorator(driver.get()), this);
    }

    public boolean isUsernameTextFieldVisible() {
        return isElementVisible(usernameTextField);
    }

    public boolean isPasswordTextFieldVisible() {
        return isElementVisible(passwordTextField);
    }

    public boolean isLoginButtonVisible() {
        return isElementVisible(loginButton);
    }

    public boolean isAutofillCredentialVisible() {
        return isElementVisible(autofillCredentials);
    }

    public void loginToApplication(String username, String password) {
        sendValues(usernameTextField, username);
        sendValues(passwordTextField, password);
        clickOnElement(loginButton);
    }

    // This method will click on the first username
    public void tapOnAutofillCredential(){
        Assert.assertTrue(isAutofillCredentialVisible());
        clickOnElement(autofillCredentials);
    }

    public void tapOnLoginButton(){
        clickOnElement(loginButton);
    }

    public String getUsernameValue(){
        return getElementText(usernameTextField);
    }

    public String getPasswordValue(){
        return getElementText(passwordTextField);
    }
}