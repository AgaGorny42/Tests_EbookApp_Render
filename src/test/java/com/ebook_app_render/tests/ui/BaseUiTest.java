package com.ebook_app_render.tests.ui;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;
import com.ebook_app_render.ui.pages.LoginPage;
import com.ebook_app_render.ui.utils.DriverSingleton;
import io.qameta.allure.Allure;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.ITestResult;
import org.testng.annotations.*;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Date;

public abstract class BaseUiTest {

    protected static final String USER_LOGIN = "test11";
    protected static final String USER_PASSWORD = "test11";

    protected final By FOG_ANIMATED_BY = By.cssSelector(".fog.animated.fadeIn");

    protected static ExtentReports extent;
    protected static ThreadLocal<ExtentTest> test = new ThreadLocal<>();
    protected WebDriver driver;
    protected WebDriverWait wait;
    protected LoginPage loginPage;

    @BeforeClass(alwaysRun = true)
    public void setupClass() {
        driver = DriverSingleton.getDriver();
        wait = DriverSingleton.getWait();

        driver.get("https://ta-bookrental-fe.onrender.com/login");

        loginPage = new LoginPage();
        loginPage.waitForPageToBeLoaded();
    }

    @BeforeSuite
    public void setupReport() throws IOException {
        Files.createDirectories(Paths.get("reports"));
        Files.createDirectories(Paths.get("screenshots"));

        FileUtils.cleanDirectory(new File("reports"));
        FileUtils.cleanDirectory(new File("screenshots"));

        ExtentSparkReporter spark = new ExtentSparkReporter("reports/ExtentReport.html");
        spark.config().setDocumentTitle("UI Tests Report");
        spark.config().setReportName("Selenium Test Automation (Extent + Allure)");
        spark.config().setTheme(Theme.STANDARD);

        extent = new ExtentReports();
        extent.attachReporter(spark);
        extent.setSystemInfo("System", "Windows 11");
        extent.setSystemInfo("Browser", "Chrome");
        extent.setSystemInfo("Tester", "Agnieszka Górny");
    }

    @BeforeMethod
    public void setupTest(Method method) {
        ExtentTest extentTest = extent.createTest(method.getName());
        test.set(extentTest);
    }

    @AfterMethod
    public void tearDown(ITestResult result) {
        ExtentTest extentTest = test.get();

        switch (result.getStatus()) {
            case ITestResult.SUCCESS:
                extentTest.pass("✅ Test completed successfully.");
                Allure.step("Test completed successfully.");
                break;

            case ITestResult.FAILURE:
                extentTest.fail("❌ Test failed.");
                extentTest.fail(result.getThrowable());
                Allure.step("❌ Test failed.");
                Allure.step(result.getThrowable().toString());

                String screenshotPath = takeScreenshot(driver, result.getMethod().getMethodName());
                try {
                    extentTest.addScreenCaptureFromPath(screenshotPath);

                    try (InputStream is = Files.newInputStream(new File(screenshotPath).toPath())) {
                        Allure.addAttachment("Screenshot on error.", is);
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;

            case ITestResult.SKIP:
                extentTest.skip("⚠️ Test skipped.");
                Allure.step("⚠️ Test skipped.");
                break;
        }
    }

    @AfterSuite
    public void tearDownSuite() {
        extent.flush();
    }

    private String takeScreenshot(WebDriver driver, String methodName) {
        String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String screenshotDir = "reports/screenshots/";
        String screenshotPath = screenshotDir + methodName + "_" + timestamp + ".png";

        File src = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
        try {
            Files.createDirectories(new File(screenshotDir).toPath());
            Files.copy(src.toPath(), new File(screenshotPath).toPath());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return screenshotPath;
    }

    protected void waitForFogAnimatedToDisappear() {
        wait.until(ExpectedConditions.invisibilityOfElementLocated(FOG_ANIMATED_BY));
    }

    @AfterClass(alwaysRun = true)
    public void tearDown() {
        DriverSingleton.quitDriver();
    }
}

