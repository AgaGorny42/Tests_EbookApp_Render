package com.ebook_app_render.ui.pages;

import com.ebook_app_render.ui.utils.DriverSingleton;
import org.openqa.selenium.*;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.Duration;

public abstract class BasePage {

    protected static final String LOGIN_INPUT_CSS = "span.input-field__label";
    protected static final String TITLES_ID_BY = "titles";
    protected static final String SIGN_UP_CSS = ".sub-title";
    protected static final String ADD_NEW_ITEM_BUTTON_CSS = "#add-item-button";
    protected static final String TEXT_ON_PAGE_CSS = ".sub-title.flex-grow--1.margin-right--1";
    protected static final String ERROR_MESSAGE_CSS = ".alert__content";
    protected static final By ALERT_CONTENT_BY = By.cssSelector(".alert__content");
    protected final By LOADER_BY = By.cssSelector(".lds-ripple");
    protected final By FOG_ANIMATED_BY = By.cssSelector(".fog.animated.fadeIn");
    protected WebDriver driver;
    protected WebDriverWait wait;

    public BasePage() {
        this.driver = DriverSingleton.getDriver();
        this.wait = DriverSingleton.getWait();
        PageFactory.initElements(driver, this);
    }

    protected WebElement waitForElement(By locator) {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
    }

    protected void clickWhenReady(By locator) {
        WebDriver driver = DriverSingleton.getDriver();
        WebDriverWait localWait = new WebDriverWait(driver, Duration.ofSeconds(20));
        localWait.pollingEvery(Duration.ofMillis(200));

        try {
            localWait.until(ExpectedConditions.or(
                    ExpectedConditions.invisibilityOfElementLocated(By.cssSelector(".lds-ripple")),
                    ExpectedConditions.invisibilityOfElementLocated(By.cssSelector(".fog.animated.fadeIn"))
            ));
        } catch (TimeoutException e) {
            System.out.println("Overlay did not disappear in time — continuing anyway.");
        }

        int attempts = 0;
        while (attempts < 5) {
            try {
                localWait.until(ExpectedConditions.elementToBeClickable(locator));
                WebElement element = driver.findElement(locator);
                element.click();
                return;
            } catch (ElementClickInterceptedException | StaleElementReferenceException e) {
                attempts++;
                try {
                    Thread.sleep(200);
                } catch (InterruptedException ex) {
                    Thread.currentThread().interrupt();
                }
            } catch (TimeoutException e) {
                throw new RuntimeException("Element not clickable after timeout: " + locator, e);
            }
        }

        throw new RuntimeException("Unable to click element after multiple attempts: " + locator);
    }

    protected void waitForFogAnimatedToDisappear() {
        wait.until(ExpectedConditions.invisibilityOfElementLocated(FOG_ANIMATED_BY));
    }

    public void waitForLoaderToDisappear(By loaderBy, By listBy) {
            wait.until(ExpectedConditions.invisibilityOfElementLocated(loaderBy));
            wait.until(ExpectedConditions.visibilityOfElementLocated(listBy));
    }

    public void waitForLoaderToDisappear() {
        try {
            if (!driver.findElements(LOADER_BY).isEmpty()) {
                wait.until(ExpectedConditions.invisibilityOfElementLocated(LOADER_BY));
            }
        } catch (TimeoutException ignored) {
        }
    }

    public abstract void waitForPageToBeLoaded();
}
