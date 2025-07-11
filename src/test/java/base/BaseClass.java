package base;

import com.google.common.collect.ImmutableList;
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.options.UiAutomator2Options;
import io.appium.java_client.ios.IOSDriver;
import io.appium.java_client.ios.options.XCUITestOptions;
import io.appium.java_client.service.local.AppiumDriverLocalService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.PointerInput;
import org.openqa.selenium.interactions.Sequence;
import org.openqa.selenium.remote.RemoteWebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.*;
import org.testng.annotations.Optional;

import java.io.File;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.*;

import static java.time.Duration.ofMillis;


public class BaseClass {

    protected static ThreadLocal<IOSDriver> driver = new ThreadLocal<>();
    protected static ThreadLocal<AndroidDriver> androidDriver = new ThreadLocal<>();
    //static Logger log = LogManager.getLogger(BaseClass.class.getName());
    public static Properties properties;
    InputStream inputStream;
    private static AppiumDriverLocalService server;

    String appFolderPath = System.getProperty("user.dir") + File.separator + "src" + File.separator + "main" +
            File.separator + "resources" + File.separator + "mobileAppBuilds" + File.separator;
    String androidAppPath = appFolderPath + "Android.SauceLabs.Mobile.Sample.app.2.7.1.apk";
    String iOSAppPath = appFolderPath + "iOS.Simulator.SauceLabs.Mobile.Sample.app.2.7.1.app";

    @BeforeSuite
    public void initializeProps() {
        try {
            properties = new Properties();
            String propertyFileName = "config.properties";
            inputStream = getClass().getClassLoader().getResourceAsStream(propertyFileName);
            properties.load(inputStream);
            log().info("Loaded and initialized config.properties file");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public Logger log() {
        return LogManager.getLogger(Thread.currentThread().getStackTrace()[2].getClassName());
    }

    @BeforeTest
    @Parameters({"platformName"})
    public void initializeIOSDriver(@Optional("iOS") String platformValue) {
        URL url;
        IOSDriver iosDriver;
        AndroidDriver androidDriver1;
        try {
            url = new URL("http://" + properties.getProperty("APPIUM_HOST") + ":" + properties.getProperty("APPIUM_PORT"));
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }

        switch (platformValue.toLowerCase()) {
            case "android" -> {
                UiAutomator2Options uiAutomator2Options = new UiAutomator2Options();
                uiAutomator2Options.setPlatformName("Android")
                        .setAutomationName("UiAutomator2")
                        .setAvd("Pixel_8_Pro")
                        .setApp(androidAppPath)
                        .setAvdLaunchTimeout(Duration.ofMinutes(5))
                        .setNewCommandTimeout(Duration.ofMinutes(2));
                androidDriver1 = new AndroidDriver(url, uiAutomator2Options);
                androidDriver.set(androidDriver1);
                log().info("Initialized the Android driver.");
            }
            case "ios" -> {
                XCUITestOptions xcuiTestOptions = new XCUITestOptions();
                xcuiTestOptions.setPlatformName("iOS")
                        .setAutomationName("XCUITest")
                        .setDeviceName(properties.getProperty("IOS_DEVICE_NAME"))
                        .setPlatformVersion(properties.getProperty("IOS_PLATFORM_VERSION"))
                        .setApp(iOSAppPath)
                        .setUdid(properties.getProperty("IOS_DEVICE_UDID"))
                        .setSimulatorStartupTimeout(Duration.ofMinutes(10))
                        .setNewCommandTimeout(Duration.ofMinutes(2));
                xcuiTestOptions.setCapability("autoAcceptAlerts", true);
                iosDriver = new IOSDriver(url, xcuiTestOptions);
                driver.set(iosDriver);
                log().info("Initialized the iOS driver.");
            }
            default -> Assert.fail("Invalid platform name is passed. Please pass either 'Android' or 'iOS'");

        }
    }

    public void closeApp() {
        getDriver().terminateApp(properties.getProperty("IOS_APP_BUNDLE_ID"));
    }

    public void launchApp() {
        getDriver().activateApp(properties.getProperty("IOS_APP_BUNDLE_ID"));
    }

    @BeforeMethod
    public void beforeMethod(Method method) {
        closeApp();
        launchApp();
        log().info("\n" + "****** Starting the test:" + method.getName() + "******" + "\n");
    }

    @AfterTest
    public void tearDown() {
        getDriver().quit();
    }

    public IOSDriver getDriver() {
        return driver.get();
    }

    public boolean isElementVisible(WebElement element) {
        try {
            WebDriverWait wait = new WebDriverWait(getDriver(), Duration.ofSeconds(15));
            wait.until(ExpectedConditions.visibilityOf(element));
            log().info("Checking for the visibility of element: " + element);
            return true;
        } catch (Exception ignored) {
            return false;
        }
    }

    public void clickOnElement(WebElement element) {
        Assert.assertTrue(isButtonEnabled(element), element + " button is not enabled");
        element.click();
        log().info("Tap on the element: " + element);
    }

    public boolean isButtonEnabled(WebElement element) {
        log().info("Checking the Enabled status of the element: " + element);
        return element.isEnabled();
    }

    public void sendValues(WebElement element, String value) {
        element.click();
        element.sendKeys(value);
    }

    public String getElementText(WebElement element) {
        return element.getText();
    }

    public String getElementAttribute(WebElement element, String attributeName) {
        return element.getAttribute(attributeName);
    }

    public void swipeScreen(AppiumDriver driver, int startX, int startY, int endX, int endY, int milliSeconds) {
        PointerInput input = new PointerInput(PointerInput.Kind.TOUCH, "thumbFinger");
        Sequence sequence = new Sequence(input, 0);
        sequence.addAction(input.createPointerMove(Duration.ZERO, PointerInput.Origin.viewport(), startX, startY));
        sequence.addAction(input.createPointerDown(PointerInput.MouseButton.LEFT.asArg()));
        sequence.addAction(input.createPointerMove(Duration.ofMillis(milliSeconds), PointerInput.Origin.viewport(), endX, endY));
        sequence.addAction(input.createPointerUp(PointerInput.MouseButton.LEFT.asArg()));
        driver.perform(ImmutableList.of(sequence));
    }

    public void zoomElement(WebElement element, double scaleValue, double velocityValue) {
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("elementId", ((RemoteWebElement) element).getId());
        parameters.put("scale", scaleValue);
        parameters.put("velocity", velocityValue);
        getDriver().executeScript("mobile:pinch", parameters);
    }

    public String captureScreenShotAndGetThePath(String testCaseName) {
        String timeStamp = new SimpleDateFormat("yyyyMMddhhmmss").format(new Date());
        TakesScreenshot takesScreenshot = (TakesScreenshot) getDriver();
        File sourceFile = takesScreenshot.getScreenshotAs(OutputType.FILE);
        String targetFilePath = System.getProperty("user.dir") + File.separator + "screenshots" + File.separator + testCaseName + "_" + timeStamp + ".png";
        File targetFile = new File(targetFilePath);
        sourceFile.renameTo(targetFile);
        return targetFilePath;
    }

    public void dragAndDropElement(WebElement source, WebElement target) {
        // Get the coordinates
        int startX = source.getLocation().getX() + (source.getSize().getWidth() / 2);
        int startY = source.getLocation().getY() + (source.getSize().getHeight() / 2);

        int endX = target.getLocation().getX() + (target.getSize().getWidth() / 2);
        int endY = target.getLocation().getY() + (target.getSize().getHeight() / 2);

        // Create a PointerInput instance for touch actions
        PointerInput finger = new PointerInput(PointerInput.Kind.TOUCH, "finger");

        // Create a Sequence for the drag-and-drop action
        Sequence dragAndDrop = new Sequence(finger, 1);
        dragAndDrop.addAction(finger.createPointerMove(Duration.ZERO, PointerInput.Origin.viewport(), startX, startY));
        dragAndDrop.addAction(finger.createPointerDown(PointerInput.MouseButton.LEFT.asArg()));
        dragAndDrop.addAction(finger.createPointerMove(ofMillis(700), PointerInput.Origin.viewport(), endX, endY));
        dragAndDrop.addAction(finger.createPointerUp(PointerInput.MouseButton.LEFT.asArg()));
        //getDriver().perform(Arrays.asList(dragAndDrop));
        getDriver().perform(List.of(dragAndDrop));
        log().info("Performed Drag and Drop operation");
    }

}
