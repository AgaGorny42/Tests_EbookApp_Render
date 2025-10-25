package com.ebook_app_render.tests.ui;

import com.ebook_app_render.ui.pages.LoginPage;
import com.ebook_app_render.ui.pages.RegisterPage;
import com.ebook_app_render.ui.utils.DriverSingleton;
import com.ebook_app_render.ui.utils.TestDataGenerator;
import org.testng.Assert;
import org.testng.annotations.*;

public class RegisterPageTest extends BaseUiTest {

    private RegisterPage registerPage;

    @BeforeMethod
    public void setup() {
        DriverSingleton.getDriver().get("https://ta-bookrental-fe.onrender.com/register");

        registerPage = new RegisterPage();
        registerPage.waitForPageToBeLoaded();
    }

    @Test
    public void checkIfRepeatPasswordElementIsDisplayedTest() {
        Assert.assertTrue(registerPage.registerPageIsDisplayed(),
                "Register page should display repeat password element.");
    }

    @Test
    public void signUpTextShouldBeVisibleOnRegisterPageTest() {
        String actualText = registerPage.getTextFromElement();
        String expectedText = "SIGN UP";

        Assert.assertEquals(actualText, expectedText, "Sign up header text is incorrect.");
    }

    @Test
    public void successfulRegistrationOfNewUserTest() {
        String login = TestDataGenerator.randomLogin();
        String password = TestDataGenerator.randomPassword();

        registerPage.userRegistration(login, password, password);

        String actualMessage = registerPage.getSuccessMessage();
        String expectedMessage = "You have been successfully registered!";

        Assert.assertEquals(actualMessage, expectedMessage,
                "Checking alert after correct registration.");
    }

    @Test
    public void emptyLoginAndPasswordTest() {
        registerPage.userRegistration("test1", "", "");

        String actualError = registerPage.getErrorMessage();
        String expectedError = "You can't leave fields empty";

        Assert.assertEquals(actualError, expectedError, "Checking error message after failed registration.");
    }

    @Test
    public void userAlreadyExistTest() {
        registerPage.userRegistration("test1", "test1", "test1");

        String actualError = registerPage.getErrorMessage();
        String expectedError = "This user already exist!";

        Assert.assertEquals(actualError, expectedError);
    }

    @Test
    public void wrongPasswordTest() {
        registerPage.userRegistration("test1", "test1", "wrong");

        String actualError = registerPage.getErrorMessage();
        String expectedError = "The passwords don't match";

        Assert.assertEquals(actualError, expectedError, "Checking alert after wrong password inserted.");
    }

    @Test
    public void returnToLoginPageTest() {
        LoginPage loginPage = registerPage.getLoginPage();
        loginPage.waitForPageToBeLoaded();

        Assert.assertTrue(DriverSingleton.getDriver().getCurrentUrl().contains("login"));
    }
}
