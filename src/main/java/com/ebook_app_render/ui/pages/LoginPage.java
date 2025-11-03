package com.ebook_app_render.ui.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import static com.ebook_app_render.ui.utils.WaitingTime.WAITING_TIME;

public class LoginPage extends BasePage {

    private final By LOGIN_INPUT_BY = By.id("login");
    private final By PASSWORD_INPUT_BY = By.id("password");
    private final By LOGIN_BUTTON_BY = By.id("login-btn");
    private final By SIGN_UP_BUTTON_BY = By.id("register-btn");

    public void unsuccessfulLogin(String login, String password) {
        driver.findElement(LOGIN_INPUT_BY).sendKeys(login);
        driver.findElement(PASSWORD_INPUT_BY).sendKeys(password);
        driver.findElement(LOGIN_BUTTON_BY).click();
    }

    public TitlesPage successfulLogin(String login, String password) {
        driver.findElement(LOGIN_INPUT_BY).sendKeys(login);
        driver.findElement(PASSWORD_INPUT_BY).sendKeys(password);
        driver.findElement(LOGIN_BUTTON_BY).click();
        return new TitlesPage();
    }

    public RegisterPage getRegisterPage() {
        waitForElement(SIGN_UP_BUTTON_BY).click();

        wait.until(ExpectedConditions.urlContains("register"));

        RegisterPage registerPage = new RegisterPage();
        registerPage.waitForPageToBeLoaded();
        return registerPage;
    }

    public String getErrorMessage() {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(ERROR_MESSAGE_CSS))).getText();
    }

    @Override
    public void waitForPageToBeLoaded() {
        WebDriverWait customWait = new WebDriverWait(driver, WAITING_TIME.getDuration());
        customWait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(LOGIN_INPUT_CSS)));
    }
}
