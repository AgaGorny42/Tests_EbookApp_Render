package com.ebook_app_render.ui.pages;

import com.ebook_app_render.ui.utils.DriverSingleton;
import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

import static com.ebook_app_render.ui.utils.WaitingTime.WAITING_TIME;

public class RegisterPage extends BasePage {

    private final By LOGIN_INPUT_BY = By.id("login");
    private final By PASSWORD_INPUT_BY = By.id("password");
    private final By REPEAT_PASSWORD_INPUT_BY = By.cssSelector("#password-repeat");
    private final By LOGIN_BUTTON_BY = By.id("login-btn");
    private final By SIGN_UP_BUTTON_BY = By.id("register-btn");
    private final By ERROR_MESSAGE_BY = By.cssSelector(".alert__content");
    private final By SUCCESS_MESSAGE_BY = By.cssSelector("#app > div > form > div.alert.alert--success > p");
    private final By SUB_TITLE_ELEMENT_BY = By.xpath("//*[@id='app']/div/form/h2");

    public RegisterPage userRegistration(String login, String password, String repeatPassword) {
        waitForElement(LOGIN_INPUT_BY).sendKeys(login);
        waitForElement(PASSWORD_INPUT_BY).sendKeys(password);
        waitForElement(REPEAT_PASSWORD_INPUT_BY).sendKeys(repeatPassword);
        waitForElement(SIGN_UP_BUTTON_BY).click();
        return this;
    }

    public LoginPage getLoginPage() {
        waitForElement(LOGIN_BUTTON_BY).click();

        WebDriverWait wait = new WebDriverWait(DriverSingleton.getDriver(), Duration.ofSeconds(10));
        wait.until(ExpectedConditions.urlContains("login"));

        LoginPage loginPage = new LoginPage();
        loginPage.waitForPageToBeLoaded();
        return loginPage;
    }

    public boolean registerPageIsDisplayed() {
        return waitForElement(REPEAT_PASSWORD_INPUT_BY).isDisplayed();
    }

    public String getErrorMessage() {
        return waitForElement(ERROR_MESSAGE_BY).getText();
    }

    public String getSuccessMessage() {
        return waitForElement(SUCCESS_MESSAGE_BY).getText();
    }

    @Override
    public void waitForPageToBeLoaded() {
        new WebDriverWait(driver, WAITING_TIME.getDuration())
                .until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(SIGN_UP_CSS)));
    }

    public String getTextFromElement() {
        return waitForElement(SUB_TITLE_ELEMENT_BY).getText();
    }
}
