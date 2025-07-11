package pages;

import base.BaseClass;
import io.appium.java_client.pagefactory.AndroidFindBy;
import io.appium.java_client.pagefactory.AppiumFieldDecorator;
import io.appium.java_client.pagefactory.iOSXCUITFindBy;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;
import org.testng.Assert;

public class ProductsMainPage extends BaseClass {

    // NOTE: Sample placeholder is created for Android locator values

    @iOSXCUITFindBy(xpath = "//XCUIElementTypeOther[@name='PRODUCTS']")
    @AndroidFindBy(id = "TBU")
    private WebElement productsHeader;

    @iOSXCUITFindBy(xpath = "//XCUIElementTypeStaticText[@name='test-Item title' and @label='Sauce Labs Bolt T-Shirt']")
    @AndroidFindBy(id = "TBU")
    private WebElement boltTShirtProduct;

    @iOSXCUITFindBy(xpath = "(//XCUIElementTypeOther[@name='ADD TO CART'])[3]")
    @AndroidFindBy(id = "TBU")
    private WebElement boltTShirtAddToCartButton;

    @iOSXCUITFindBy(accessibility = "test-Cart")
    @AndroidFindBy(id = "TBU")
    private WebElement cartIcon;

    @iOSXCUITFindBy(xpath = "(//XCUIElementTypeOther[@name='test-Drag Handle'])[3]/XCUIElementTypeStaticText")
    @AndroidFindBy(id = "TBU")
    private WebElement dragIconOfBoltTShirtProduct;


    public ProductsMainPage() {
        PageFactory.initElements(new AppiumFieldDecorator(driver.get()), this);
    }

    public boolean isProductsHeaderVisible() {
        return isElementVisible(productsHeader);
    }

    public boolean isBoltTShirtProductVisible() {
        return isElementVisible(boltTShirtProduct);
    }

    public boolean isBoltTShirtAddToCartButtonVisible() {
        return isElementVisible(boltTShirtProduct);
    }

    public boolean isCartArtIconVisible() {
        return isElementVisible(cartIcon);
    }

    public String getCartIconNumber(){
        return getElementAttribute(cartIcon, "label");
    }

    public boolean isDragIconOfBoltTShirtProductVisible() {
        return isElementVisible(dragIconOfBoltTShirtProduct);
    }

    public void tapOnBoltTShirtAddToCartButton(){
        Assert.assertTrue(isBoltTShirtAddToCartButtonVisible());
        clickOnElement(boltTShirtAddToCartButton);
    }

    public void tapOnBoltTShirtProduct(){
        clickOnElement(boltTShirtProduct);
    }

    public WebElement getDragIconOfBoltTShirtProductElement() {
        return dragIconOfBoltTShirtProduct;
    }

    public WebElement getProductHeaderElement() {
        return productsHeader;
    }
}