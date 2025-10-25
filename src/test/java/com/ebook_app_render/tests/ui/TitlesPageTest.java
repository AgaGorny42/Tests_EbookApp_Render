package com.ebook_app_render.tests.ui;

import com.ebook_app_render.ui.pages.ItemsPage;
import com.ebook_app_render.ui.pages.LoginPage;
import com.ebook_app_render.ui.pages.TitlesPage;
import com.ebook_app_render.ui.utils.DriverSingleton;
import org.testng.annotations.*;
import org.testng.asserts.SoftAssert;

public class TitlesPageTest extends BaseUiTest {

    private TitlesPage titlesPage;
    private ItemsPage itemsPage;
    private SoftAssert softAssert;

    @BeforeMethod
    public void setup() {
        DriverSingleton.getDriver().get("https://ta-bookrental-fe.onrender.com/login");
        loginPage = new LoginPage();
        loginPage.waitForPageToBeLoaded();

        titlesPage = loginPage.successfulLogin(USER_LOGIN, USER_PASSWORD);
        titlesPage.waitForPageToBeLoaded();
    }

    @BeforeMethod
    public void initSoftAssert() {
        softAssert = new SoftAssert();
    }

    @Test
    public void titlesPageShouldBeLoadedTest() {
        String actualText = titlesPage.getTextFromElement();
        String expectedText = "TITLES CATALOG";

        softAssert.assertEquals(actualText, expectedText, "Titles Catalog header text is incorrect.");
        softAssert.assertAll();
    }

    @Test
    public void shouldAddNewTitle() {
        titlesPage.clickAddNewTitleButton()
                .addTitle("Clean Code", "Robert C. Martin", "2008");

        softAssert.assertTrue(titlesPage.containsBook("Clean Code", "Robert C. Martin", 2008),
                "The new title is not visible in the catalog");

        titlesPage.removeTitle("Clean Code");
        softAssert.assertAll();
    }

    @Test
    public void shouldRemoveTitle() {
        titlesPage.clickAddNewTitleButton()
                .addTitle("Test Title", "Author", "2020");

        softAssert.assertTrue(titlesPage.containsBook("Test Title", "Author", 2020),
                "The new title is not visible in the catalog");

        titlesPage.removeTitle("Test Title");

        waitForFogAnimatedToDisappear();
        softAssert.assertFalse(titlesPage.containsBook("Test Title", "Author", 2020),
                "The removed title is visible in the catalog");
        softAssert.assertAll();
    }

    @Test
    public void shouldEditTitleTest() {
        titlesPage.clickAddNewTitleButton().addTitle("No logo", "Naomi Klein", "2004");
        titlesPage.editTitle("No logo", "Naomi Klein", "2024");

        titlesPage.waitForTitlesToLoad();
        int expectedYear = 2024;
        int actualYear = titlesPage.findTitleByName("No logo").toDTO().getYear();

        softAssert.assertEquals(actualYear, expectedYear, "The expected year should be 2024.");

        titlesPage.removeTitle("No logo");
        softAssert.assertAll();
    }

    @Test
    public void shouldNotBeAbleToDeleteTitleWithItemsAddedTest(){
        itemsPage = titlesPage.addTitleAndOpenListOfCopiesOfThisTitle("Anne of Green Gables", "L.M. Montgomery", "2018");

        itemsPage.addItem("2021-02-05");
        titlesPage= itemsPage.returnToTitlesPage();
        titlesPage.waitForTitlesToLoad();
        titlesPage.removeTitle("Anne of Green Gables");

        String expectedErrorMessage = "You can't remove titles with copies!";
        String actualErrorMessage = titlesPage.getErrorMessage();

        softAssert.assertEquals(actualErrorMessage, expectedErrorMessage,
                "The error message did not appear, titles is deleted.");

        titlesPage.showCopiesOfTitle("Anne of Green Gables");
        itemsPage.removeItem("2021-02-05");
        itemsPage.returnToTitlesPage().waitForPageToBeLoaded();
        titlesPage.removeTitle("Anne of Green Gables");
        softAssert.assertAll();
    }

    @Test
    public void itemsPageOfTitleShouldBeLoadedTest() {
        titlesPage.clickAddNewTitleButton().addTitle("No logo", "Naomi Klein", "2004");
        itemsPage = titlesPage.showCopiesOfTitle("No logo");

        softAssert.assertTrue(DriverSingleton.getDriver().getCurrentUrl().contains("items"));

        itemsPage.returnToTitlesPage().waitForTitlesToLoad();
        System.out.println("After test: " + DriverSingleton.getDriver().getCurrentUrl());
        titlesPage.removeTitle("No logo");
        softAssert.assertAll();
    }
}
