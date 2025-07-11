package utilities;

import base.BaseClass;
import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import static base.BaseClass.properties;


public class ExtentReportManager implements ITestListener {
    private ExtentSparkReporter sparkReporter;
    private ExtentReports extent;
    private ExtentTest test;

    String reportName;
    String reportPath;

    public void onStart(ITestContext testContext) {

        String timeStamp = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new Date());// time stamp
        reportName = "Test-Report-" + timeStamp + ".html";
        reportPath = System.getProperty("user.dir") + File.separator + "reports" + File.separator + reportName;
        sparkReporter = new ExtentSparkReporter(reportPath);// specify location of the report

        sparkReporter.config().setDocumentTitle("SwagLabs"); // Title of report
        sparkReporter.config().setReportName("SwagLabs Native app automation report"); // name of the report
        sparkReporter.config().setTheme(Theme.STANDARD);

        extent = new ExtentReports();
        extent.attachReporter(sparkReporter);
        extent.setSystemInfo("Application", "SwagLabs App");
        extent.setSystemInfo("User Name", System.getProperty("user.name"));

        String platformName = properties.getProperty("platformName");
        extent.setSystemInfo("Platform Name", platformName);

        String deviceName;
        String osVersion;
        if (platformName.equalsIgnoreCase("iOS")) {
            deviceName = properties.getProperty("IOS_DEVICE_NAME");
            osVersion = properties.getProperty("IOS_PLATFORM_VERSION");
        } else {
            deviceName = properties.getProperty("ANDROID_DEVICE_NAME");
            osVersion = properties.getProperty("ANDROID_PLATFORM_VERSION");
        }
        extent.setSystemInfo("Device name", deviceName);
        extent.setSystemInfo("Platform version", osVersion);
        List<String> includedGroups = testContext.getCurrentXmlTest().getIncludedGroups();
        if (!includedGroups.isEmpty()) {
            extent.setSystemInfo("Groups", includedGroups.toString());
        }
    }

    public void onTestSuccess(ITestResult result) {
        test = extent.createTest(result.getTestClass().getName());
        test.assignCategory(result.getMethod().getGroups()); // to display groups in report
        test.log(Status.PASS, result.getName() + " got successfully executed");
        test.log(Status.PASS, result.getMethod().getSuccessPercentage() + " % pass");
    }

    public void onTestFailure(ITestResult result) {
        test = extent.createTest(result.getTestClass().getName());
        test.assignCategory(result.getMethod().getGroups());
        test.log(Status.FAIL, result.getName() + " got failed");
        test.log(Status.INFO, result.getThrowable().getMessage());
        try {
            String imagePath = new BaseClass().captureScreenShotAndGetThePath(result.getName());
            test.addScreenCaptureFromPath(imagePath);
        } catch (Exception e1) {
            e1.printStackTrace();
        }
    }

    public void onTestSkipped(ITestResult result) {
        test = extent.createTest(result.getTestClass().getName());
        test.assignCategory(result.getMethod().getGroups());
        test.log(Status.SKIP, result.getName() + " got skipped");
        test.log(Status.INFO, result.getThrowable().getMessage());
    }

    public void onFinish(ITestContext testContext) {
        extent.flush();
        File extentReport = new File(reportPath);
        try {
            Desktop.getDesktop().browse(extentReport.toURI()); // this method will open the report in the browser after the execution.
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
