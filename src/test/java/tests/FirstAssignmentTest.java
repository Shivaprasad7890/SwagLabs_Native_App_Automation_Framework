package tests;

import base.BaseClass;
import org.openqa.selenium.*;
import org.openqa.selenium.interactions.PointerInput;
import org.openqa.selenium.interactions.Sequence;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import pages.DrawingPage;
import pages.LoginPage;
import pages.ProductsMainPage;
import pages.TShirtDetailsPage;
import utilities.ExtentReportManager;
import java.time.Duration;
import java.util.Arrays;
import java.util.Set;

import static java.time.Duration.ofMillis;

@Listeners(ExtentReportManager.class)
public class FirstAssignmentTest extends BaseClass {

    /*
            Test case 1 : Draw a Square on drawing screen & Tap on ‘Save’ icon, validate if the Pop-up
            contains ‘Save Drawing’ & Tap on ‘Submit’
    */

    @Test()
    public void drawSquareOnDrawingScreenAndVerifyThePopupMessage() {
        getDriver().activateApp("com.apple.mobilesafari");
        getDriver().get("swaglabs://drawing");

        // Switch to native context
        Set<String> contexts = getDriver().getContextHandles();
        for (String context : contexts) {
            log().info("context is: "+context);
        }
        driver.get().context("NATIVE_APP"); // Switch to native context

        // Initially for 2-3 times only, 'Open this page in SwagLabs’ App popup appears
        // driver.switchTo().alert().accept();
        //  WebElement popup = driver.findElement(AppiumBy.accessibilityId("SFDialogView"));
        // Assert.assertTrue(popup.isDisplayed(), "Popup is not displayed");

        DrawingPage drawingPage = new DrawingPage();
        Assert.assertTrue(drawingPage.isDrawingScreenVisible(), "drawingScreen is not displayed");
        System.out.println("Drawing screen is displayed");

        // Code to draw Square

        Dimension screenSize = getDriver().manage().window().getSize();
        int width = screenSize.getWidth();
        int height = screenSize.getHeight();

        // 1st) top left corner
        int topLeftX = (int) (width * 0.20);
        int topLeftY = (int) (height * 0.30);
        // 2nd) top right corner
        int topRightX = (int) (width * 0.75);
        int topRightY = (int) (height * 0.30);

        // 3rd) bottom right corner
        int bottomRightX = (int) (width * 0.75);
        int bottomRightY = (int) (height * 0.80);

        // 4th) bottom left corner
        int bottomLeftX = (int) (width * 0.20);
        int bottomLeftY = (int) (height * 0.80);


        // Create a PointerInput instance for touch actions
        PointerInput finger = new PointerInput(PointerInput.Kind.TOUCH, "finger");

        // Create a Sequence for the drawing action
        Sequence drawSquare = new Sequence(finger, 0);

        // Move to the starting point (top-left corner)
        drawSquare.addAction(finger.createPointerMove(Duration.ZERO, PointerInput.Origin.viewport(), topLeftX, topLeftY));
        drawSquare.addAction(finger.createPointerDown(PointerInput.MouseButton.LEFT.asArg()));

        // Draw to the top-right corner
        drawSquare.addAction(finger.createPointerMove(ofMillis(500), PointerInput.Origin.viewport(), topRightX, topRightY));

        // Draw to the bottom-right corner
        drawSquare.addAction(finger.createPointerMove(ofMillis(500), PointerInput.Origin.viewport(), bottomRightX, bottomRightY));

        // Draw to the bottom-left corner
        drawSquare.addAction(finger.createPointerMove(ofMillis(500), PointerInput.Origin.viewport(), bottomLeftX, bottomLeftY));

        // Draw back to the top-left corner to close the square
        drawSquare.addAction(finger.createPointerMove(ofMillis(500), PointerInput.Origin.viewport(), topLeftX, topLeftY));

        // Release the touch
        drawSquare.addAction(finger.createPointerUp(PointerInput.MouseButton.LEFT.asArg()));

        // Perform the sequence
        getDriver().perform(Arrays.asList(drawSquare));

        Assert.assertTrue(drawingPage.isSaveButtonVisible(), "Save button text is not displayed on the screen");
        drawingPage.tapOnSaveButton();
        Assert.assertTrue(drawingPage.isSaveDrawingTextVisible(), "Save Drawing text is not displayed on the Popup");
        Assert.assertTrue(drawingPage.isOkButtonVisible(), "OK button is not displayed on the Popup,");
        drawingPage.tapOnOkButton();
    }

    /*
            Test case 2 : Login with default credentials on SwagLabs app (standard_user / secret_sauce)
                            a) Go to ‘Products Listing’ section
                            b) Validate if ‘Sauce Labs Bolt T-Shirt’ product is available in the search list
                            c) Drag and drop ‘Sauce Labs Bolt T-Shirt’ to ‘PRODUCTS’ Menu (Top bar) & validate if the cart count increased to 1

     */

    @Test(dataProvider = "loginData")
    public void verifyAddToCartCountIncrease(String username, String password) {
        LoginPage loginPage = new LoginPage();
        ProductsMainPage productsMainPage = new ProductsMainPage();
        Assert.assertTrue(loginPage.isLoginButtonVisible(), "Login button is not visible on the Login Page.");
        loginPage.loginToApplication(username, password);
        Assert.assertTrue(productsMainPage.isProductsHeaderVisible(), "Products header is not visible");

        // Performing 1 swipe up action
        Dimension screenSize = getDriver().manage().window().getSize();
        int width = screenSize.getWidth();
        int height = screenSize.getHeight();

        // 1st) top left corner
        int topX = (int) (width * 0.50);
        int topY = (int) (height * 0.30);
        // 2nd) top right corner
        int bottomX = (int) (width * 0.5);
        int bottomY = (int) (height * 0.60);

        swipeScreen(getDriver(), bottomX, bottomY, topX, topY, 1000);
        Assert.assertTrue(productsMainPage.isBoltTShirtProductVisible(), "'Sauce Labs Bolt T-Shirt' product is not displayed");

        WebElement sourceElement = productsMainPage.getDragIconOfBoltTShirtProductElement();
        WebElement targetElement = productsMainPage.getProductHeaderElement();
        dragAndDropElement(sourceElement, targetElement);

        Assert.assertTrue(productsMainPage.isCartArtIconVisible(), "Cart Icon is not displayed");
        String numberOfProductAdded = productsMainPage.getCartIconNumber();
        Assert.assertEquals(numberOfProductAdded, "1", "Mismatch in number of products added");
        log().info("Number of products added is :" + numberOfProductAdded);
    }

    //Test case 3: Proceed to ‘Sauce Labs Bolt T-Shirt’ detail page & validate if user can zoom-in & zoom-out the image
    @Test(dataProvider = "loginData")
    public void verifyZoomInAndZoomOut(String username, String password) {
        LoginPage loginPage = new LoginPage();
        ProductsMainPage productsMainPage = new ProductsMainPage();
        TShirtDetailsPage tShirtDetailsPage = new TShirtDetailsPage();
        Assert.assertTrue(loginPage.isLoginButtonVisible(), "Login button is not visible on the Login Page.");
        loginPage.loginToApplication(username, password);
        Assert.assertTrue(productsMainPage.isProductsHeaderVisible(), "Products header is not visible");

        // Performing 1 swipe up action
        Dimension screenSize = getDriver().manage().window().getSize();
        int width = screenSize.getWidth();
        int height = screenSize.getHeight();
        // 1st) top left corner
        int topX = (int) (width * 0.50);
        int topY = (int) (height * 0.30);
        // 2nd) top right corner
        int bottomX = (int) (width * 0.5);
        int bottomY = (int) (height * 0.60);

        swipeScreen(getDriver(), bottomX, bottomY, topX, topY, 1000);
        Assert.assertTrue(productsMainPage.isBoltTShirtProductVisible(), "'Sauce Labs Bolt T-Shirt' product is not displayed");

        System.out.println("Sauce Labs Bolt T-Shirt product is  displayed");
        log().info("Sauce Labs Bolt T-Shirt product is  displayed");

        productsMainPage.tapOnBoltTShirtProduct();
        Assert.assertTrue(tShirtDetailsPage.isBoltShirtImageVisible(), "Sauce Labs Bolt T-Shirt image is not displayed");

        // This will Zoom in
        zoomElement(tShirtDetailsPage.getBoltShirtImageElement(), 3, 2);

        // This will Zoom Out
        zoomElement(tShirtDetailsPage.getBoltShirtImageElement(), 0.4, -0.5);
    }


    @DataProvider(name = "loginData")
    Object[][] loginData() {
        return new Object[][]{
                {"standard_user", "secret_sauce"} // Since it's a dummy data, no need of encryption
        };
    }
}