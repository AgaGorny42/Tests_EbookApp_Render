package com.ebook_app_render.ui.pages;

import com.ebook_app_render.ui.utils.DriverSingleton;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.Duration;
import static com.ebook_app_render.ui.utils.WaitingTime.WAITING_TIME;

public class LoginPage extends BasePage {

    private final By LOGIN_INPUT_BY = By.id("login");
    private final By PASSWORD_INPUT_BY = By.id("password");
    private final By LOGIN_BUTTON_BY = By.id("login-btn");
    private final By SIGN_UP_BUTTON_BY = By.id("register-btn");

    public LoginPage unsuccessfulLogin(String login, String password) {
        driver.findElement(LOGIN_INPUT_BY).sendKeys(login);
        driver.findElement(PASSWORD_INPUT_BY).sendKeys(password);
        driver.findElement(LOGIN_BUTTON_BY).click();
        return this;
    }

    public TitlesPage successfulLogin(String login, String password) {
        driver.findElement(LOGIN_INPUT_BY).sendKeys(login);
        driver.findElement(PASSWORD_INPUT_BY).sendKeys(password);
        driver.findElement(LOGIN_BUTTON_BY).click();
        return new TitlesPage();
    }

    public RegisterPage getRegisterPage() {
        waitForElement(SIGN_UP_BUTTON_BY).click();

        WebDriverWait wait = new WebDriverWait(DriverSingleton.getDriver(), Duration.ofSeconds(15));
        wait.until(ExpectedConditions.urlContains("register"));
        System.out.println("After wait: " + DriverSingleton.getDriver().getCurrentUrl());

        RegisterPage registerPage = new RegisterPage();
        registerPage.waitForPageToBeLoaded();
        return registerPage;
    }

    public String getErrorMessage() {
        WebDriverWait wait = new WebDriverWait(driver, WAITING_TIME.getDuration());
        WebElement alert = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(ERROR_MESSAGE_CSS)));
        return alert.getText();
    }

    @Override
    public void waitForPageToBeLoaded() {
        new WebDriverWait(driver, WAITING_TIME.getDuration())
                .until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(LOGIN_INPUT_CSS)));
    }
}
