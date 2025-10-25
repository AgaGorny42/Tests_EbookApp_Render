package com.ebook_app_render.ui.pages;

import com.ebook_app_render.ui.utils.DriverSingleton;
import org.openqa.selenium.*;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.Duration;
import java.util.List;

public abstract class BasePage {

    protected static final String LOGIN_INPUT_CSS = "span.input-field__label";
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
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        PageFactory.initElements(driver, this);
    }

    protected WebElement waitForElement(By locator) {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
    }

    protected void clickWhenReady(By locator) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(20));
        wait.pollingEvery(Duration.ofMillis(200));

        try {
            wait.until(ExpectedConditions.or(
                    ExpectedConditions.invisibilityOfElementLocated(By.cssSelector(".lds-ripple")),
                    ExpectedConditions.invisibilityOfElementLocated(By.cssSelector(".fog.animated.fadeIn"))
            ));
        } catch (TimeoutException e) {
            System.out.println("Timeout waiting for overlay. Trying click anyway.");
        }
        WebElement element = wait.until(ExpectedConditions.elementToBeClickable(locator));

        int attempts = 0;
        while (attempts < 5) {
            try {
                element.click();
                return;
            } catch (ElementClickInterceptedException e) {
                attempts++;
                try {
                    Thread.sleep(200);
                } catch (InterruptedException ex) {
                    Thread.currentThread().interrupt();
                }
            }
        }
        throw new RuntimeException("Unable to click element after multiple attempts: " + locator);
    }

    protected void clickReturnButtonWhenReady(By returnButtonBy) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(20));
        wait.pollingEvery(Duration.ofMillis(200));

        wait.until(d -> {
            List<WebElement> fogElements = d.findElements(FOG_ANIMATED_BY);
            return fogElements.isEmpty() || fogElements.stream().allMatch(e -> !e.isDisplayed());
        });
        WebElement returnButton = wait.until(ExpectedConditions.elementToBeClickable(returnButtonBy));

        int attempts = 0;
        while (attempts < 5) {
            try {
                returnButton.click();
                return;
            } catch (ElementClickInterceptedException e) {
                attempts++;
                try {
                    Thread.sleep(200);
                } catch (InterruptedException ex) {
                    Thread.currentThread().interrupt();
                }
            }
        }

        throw new RuntimeException("Unable to click return button after multiple attempts: " + returnButtonBy);
    }

    protected void waitForFogAnimatedToDisappear(){
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(25));
        wait.until(ExpectedConditions.invisibilityOfElementLocated(FOG_ANIMATED_BY));
    }

    public void waitForLoaderToDisappear(By loaderBy, By listBy, Duration timeout) {
        WebDriverWait wait = new WebDriverWait(driver, timeout);
        wait.until(ExpectedConditions.invisibilityOfElementLocated(loaderBy));
        wait.until(ExpectedConditions.visibilityOfElementLocated(listBy));
    }

    public void waitForLoaderToDisappear() {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        try {
            wait.until(ExpectedConditions.presenceOfElementLocated(LOADER_BY));
        } catch (TimeoutException ignored) {
        }
        wait.until(ExpectedConditions.invisibilityOfElementLocated(LOADER_BY));
    }

    public abstract void waitForPageToBeLoaded();
}
