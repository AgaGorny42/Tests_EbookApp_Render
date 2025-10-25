package com.ebook_app_render.tests.ui;

import com.ebook_app_render.ui.pages.LoginPage;
import com.ebook_app_render.ui.utils.DriverSingleton;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.AfterClass;

import java.time.Duration;

public class BaseUiTest {

    protected static final String USER_LOGIN = "test11";
    protected static final String USER_PASSWORD = "test11";
    protected final By FOG_ANIMATED_BY = By.cssSelector(".fog.animated.fadeIn");
    WebDriver driver = DriverSingleton.getDriver();
    WebDriverWait wait;
    protected LoginPage loginPage;

    protected void waitForFogAnimatedToDisappear() {
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        wait.until(ExpectedConditions.invisibilityOfElementLocated(FOG_ANIMATED_BY));
    }

    @AfterClass(alwaysRun = true)
    public void tearDown() {
        DriverSingleton.quitDriver();
    }
}

