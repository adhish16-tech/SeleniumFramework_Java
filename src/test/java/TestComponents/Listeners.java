package TestComponents;

import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;

import Framework.resources.ExtendReporterNG;

import java.io.File;
import java.io.IOException;
import org.apache.commons.io.FileUtils;

// ✅ Import ChainTest
//import io.github.chaintest.ChainTest;
//import io.github.chaintest.ChainTestListener;

public class Listeners extends BaseTest implements ITestListener {

    ExtentReports extent = ExtendReporterNG.getReportObject();
    ThreadLocal<ExtentTest> extentTest = new ThreadLocal<>();
    ThreadLocal<WebDriver> driverRef = new ThreadLocal<>();

    @Override
    public void onTestStart(ITestResult result) {
        String testName = result.getTestClass().getName() + "." + result.getMethod().getMethodName();
        ExtentTest test = extent.createTest(testName);
        extentTest.set(test);

        // Save driver reference
        Object testInstance = result.getInstance();
        if (testInstance instanceof BaseTest) {
            driverRef.set(((BaseTest) testInstance).driver);
        }

        // ChainTest reporting
      //  ChainTestListener.onTestStart(result);

        System.out.println("🚀 Test started: " + testName);
    }

    @Override
    public void onTestSuccess(ITestResult result) {
        extentTest.get().log(Status.PASS, "✅ Test Passed Successfully");
       // ChainTestListener.onTestSuccess(result);
        System.out.println("✅ Test passed: " + result.getName());
    }

    @Override
    public void onTestFailure(ITestResult result) {
        extentTest.get().fail(result.getThrowable());

        try {
            WebDriver activeDriver = driverRef.get();
            if (activeDriver != null) {
                String screenshotPath = takeScreenshot(result.getMethod().getMethodName(), activeDriver);
                extentTest.get().addScreenCaptureFromPath(screenshotPath, result.getMethod().getMethodName());

                // ChainTest screenshot attachment
                byte[] screenshotBytes = activeDriver instanceof TakesScreenshot ?
                        ((TakesScreenshot) activeDriver).getScreenshotAs(OutputType.BYTES) : null;
                if (screenshotBytes != null) {
                //    ChainTestListener.embed(screenshotBytes, "image/png");
                }

                System.out.println("📸 Screenshot captured for failed test: " + screenshotPath);
            } else {
                System.out.println("⚠️ No active driver found to take screenshot for: " + result.getName());
            }
        } catch (IOException e) {
            System.out.println("⚠️ Failed to capture screenshot: " + e.getMessage());
        }

       // ChainTestListener.onTestFailure(result);
    }

    @Override
    public void onFinish(ITestContext context) {
        extent.flush();
       // ChainTestListener.onFinish(context);
        System.out.println("🏁 Test Suite finished: " + context.getName());
    }

    private String takeScreenshot(String testName, WebDriver driver) throws IOException {
        TakesScreenshot ts = (TakesScreenshot) driver;
        File source = ts.getScreenshotAs(OutputType.FILE);
        String path = System.getProperty("user.dir") + "/reports/" + testName + ".png";
        FileUtils.copyFile(source, new File(path));
        return path;
    }
}