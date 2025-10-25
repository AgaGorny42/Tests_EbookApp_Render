package com.ebook_app_render.ui.pages;

import com.ebook_app_render.ui.utils.DriverSingleton;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class BaseComponentPage {
    protected WebElement rootElement;

    public BaseComponentPage(WebElement rootElement) {
        this.rootElement = rootElement;
    }

    protected WebElement waitForElement(By locator) {
        WebDriverWait wait = new WebDriverWait(DriverSingleton.getDriver(), Duration.ofSeconds(10));
        return wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
    }

    protected void clickWhenReady(WebElement element) {
        WebDriverWait wait = new WebDriverWait(DriverSingleton.getDriver(), Duration.ofSeconds(10));
        wait.until(ExpectedConditions.elementToBeClickable(element)).click();
    }
}
