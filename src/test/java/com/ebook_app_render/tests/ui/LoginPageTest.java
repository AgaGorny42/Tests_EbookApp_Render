package com.ebook_app_render.tests.ui;

import com.ebook_app_render.ui.pages.LoginPage;
import com.ebook_app_render.ui.pages.RegisterPage;
import com.ebook_app_render.ui.pages.TitlesPage;
import com.ebook_app_render.ui.utils.DriverSingleton;
import org.testng.Assert;
import org.testng.annotations.*;

import static com.ebook_app_render.tests.ui.BaseUiTest.USER_LOGIN;
import static com.ebook_app_render.tests.ui.BaseUiTest.USER_PASSWORD;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class LoginPageTest {

    private LoginPage loginPage;

    @BeforeMethod
    public void setup() {
        DriverSingleton.getDriver().get("https://ta-bookrental-fe.onrender.com/login");

        loginPage = new LoginPage();
        loginPage.waitForPageToBeLoaded();
    }

    @Test
    public void emptyLoginAndPasswordTest() {
        loginPage.unsuccessfulLogin("", "");

        String actualError = loginPage.getErrorMessage();
        String expectedError = "You can't leave fields empty";

        assertEquals(expectedError, actualError, "Checking error message after failed login");
    }

    @Test
    public void invalidLoginAndPasswordTest() {
        loginPage.unsuccessfulLogin("wrong", "wrong");

        String actualError = loginPage.getErrorMessage();
        String expectedError = "Login failed";

        assertEquals(expectedError, actualError, "Checking error message after failed login");
    }

    @Test
    public void goToRegisterPageTest() {
        RegisterPage registerPage = loginPage.getRegisterPage();

        String currentUrl = DriverSingleton.getDriver().getCurrentUrl();
        Assert.assertTrue(currentUrl != null && currentUrl.contains("register"));
        registerPage.getLoginPage().waitForPageToBeLoaded();
    }

    @Test
    public void successfulLoginTest() {
        TitlesPage titlesPage = loginPage.successfulLogin(USER_LOGIN, USER_PASSWORD);
        titlesPage.waitForPageToBeLoaded();

        assertTrue(titlesPage.isLoaded(), "TitlesPage is not loaded.");
    }

    @AfterClass
    public void tearDown() {
        DriverSingleton.quitDriver();
    }
}



