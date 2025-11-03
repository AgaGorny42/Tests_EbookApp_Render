package com.ebook_app_render.tests.ui;

import com.aventstack.extentreports.ExtentTest;
import com.ebook_app_render.ui.pages.ItemsPage;
import com.ebook_app_render.ui.pages.RentsPage;
import com.ebook_app_render.ui.pages.TitlesPage;
import io.qameta.allure.Allure;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

public class ItemsPageTest extends BaseUiTest {

    private TitlesPage titlesPage;
    private ItemsPage itemsPage;
    SoftAssert softAssert;
    ExtentTest extentTest;

    @BeforeClass
    public void setup() {
        titlesPage = loginPage.successfulLogin(USER_LOGIN, USER_PASSWORD);
        titlesPage.waitForPageToBeLoaded();
    }

    @BeforeMethod
    public void initSoftAssert() {
        softAssert = new SoftAssert();
    }

    @Test
    public void shouldCheckIfItemsPageIsLoadedTest() {
        itemsPage = titlesPage.addTitleAndOpenListOfCopiesOfThisTitle("Beloved", "Toni Morrison", "1987");

        String actualText = itemsPage.getTextFromElement();
        String expectedText = "LIST OF COPIES";

        softAssert.assertTrue(itemsPage.itemsPageIsLoaded(), "Items page has not been loaded.");
        softAssert.assertEquals(actualText, expectedText, "List of copies header text is incorrect.");

        waitForFogAnimatedToDisappear();
        itemsPage.returnToTitlesPage();
        titlesPage.removeTitle("Beloved");

        softAssert.assertAll();
    }

    @Test
    public void shouldAddItemToTitleTest() {
        itemsPage = titlesPage.addTitleAndOpenListOfCopiesOfThisTitle("No logo", "Naomi Klein", "2004");

        itemsPage.clickAddNewButtonBy().setPurchaseDate("2025-01-10");
        itemsPage.clickSubmitButtonBy();

        String expectedStatus = "Available";
        String actualStatus = itemsPage.findItemByStatus("Available").getStatus();
        String expectedDate = "2025-01-10";
        String actualDate = itemsPage.findItemByStatus("Available").getPurchaseDate();

        softAssert.assertEquals(actualStatus, expectedStatus, "The correct status is available.");
        softAssert.assertEquals(actualDate, expectedDate, "The displayed date is not the chosen one");

        itemsPage.removeItem("2025-01-10");
        itemsPage.waitForPageToBeLoaded();
        itemsPage.returnToTitlesPage();
        titlesPage.removeTitle("No logo");

        softAssert.assertAll();
    }

    @Test
    public void shouldBePossibleToDeleteNotRentedItemTest(){
        itemsPage = titlesPage.addTitleAndOpenListOfCopiesOfThisTitle("Winnie the Pooh", "A.A. Milne", "2015");

        itemsPage.clickAddNewButtonBy().setPurchaseDate("2021-01-10");
        itemsPage.clickSubmitButtonBy().findItemByStatus("Available").clickRemove();

        itemsPage.waitForPageToBeLoaded();
        String actualAlert = itemsPage.getAlertMessage();
        String expectedAlert = "No copies...";

        softAssert.assertEquals(actualAlert, expectedAlert, "The item list is not empty.");

        itemsPage.returnToTitlesPage();
        titlesPage.removeTitle("Winnie the Pooh");

        softAssert.assertAll();
    }

    @Test
    public void shouldNotBePossibleToDeleteRentedItemTest(){
        itemsPage = titlesPage.addTitleAndOpenListOfCopiesOfThisTitle("A Gallery of Children", "A.A. Milne", "1925");
        RentsPage rentsPage = itemsPage.addItemAndOpenRentHistory("2021-02-10", "Available");
        rentsPage.rentItemWithRentDateForToday("Tomi");
        rentsPage.returnToItemPage().waitForLoaderToDisappear();
        itemsPage.findItemByStatus("Available").clickRemove();

        itemsPage.waitForPageToBeLoaded();
        String actualAlert = itemsPage.getAlertMessage();
        String expectedAlert = "You can't remove copy with the rents history!";

        softAssert.assertEquals(actualAlert, expectedAlert,
                "The alert message is not displayed. Item has been deleted.");

        itemsPage.findItemByStatus("Available").clickShowHistory();
        rentsPage.waitForLoaderToDisappear();
        rentsPage.removeRental("Tomi");
        rentsPage.removeHistory("A Gallery of Children", "Available");

        softAssert.assertAll();
    }

    @Test
    public void shouldBePossibleToChangePurchaseDateTest(){
        itemsPage = titlesPage.addTitleAndOpenListOfCopiesOfThisTitle("White Nights", "Fiodor Dostojewski", "2016");

        itemsPage.clickAddNewButtonBy().setPurchaseDate("2016-03-08");
        itemsPage.clickSubmitButtonBy().findItemByStatus("Available").clickEdit();
        itemsPage.setPurchaseDate("2020-03-10");
        itemsPage.clickSubmitButtonBy();

        waitForFogAnimatedToDisappear();
        itemsPage.waitForPageToBeLoaded();
        String expectedPurchaseDate = "2020-03-10";
        String actualPurchaseDate = itemsPage.findItemByStatus("Available").getPurchaseDate();

        softAssert.assertEquals(actualPurchaseDate, expectedPurchaseDate, "The displayed date is not the chosen one");

        itemsPage.removeItem("2020-03-10");
        itemsPage.waitForPageToBeLoaded();
        itemsPage.returnToTitlesPage();
        titlesPage.removeTitle("White Nights");

        softAssert.assertAll();
    }

    @Test(dataProvider = "purchaseDatesProvider")
    public void shouldSaveCorrectPurchaseDateDuringDaylightSavingTime(String purchaseDate) {
        extentTest = extent.createTest("Verifying purchase date during DST for date: " + purchaseDate);

        itemsPage = titlesPage.addTitleAndOpenListOfCopiesOfThisTitle("Don Quixote", "Miguel de Cervantes", "1605");
        itemsPage.clickAddNewButtonBy().setPurchaseDate(purchaseDate);
        itemsPage.clickSubmitButtonBy();

        String actualDate = itemsPage.findItemByStatus("Available").getPurchaseDate();

        softAssert.assertEquals(actualDate, purchaseDate,
                "Dates are shifted by one day during the daylight saving time period.");

        extentTest.info("Verifying purchase date during DST. Expected: '"
                + purchaseDate + "', Actual: '" + actualDate + "'");
        Allure.step("Verifying purchase date during DST. Expected: '"
                + purchaseDate + "', Actual: '" + actualDate + "'");

        itemsPage.removeItemByStatus("Available");
        itemsPage.waitForPageToBeLoaded();
        itemsPage.returnToTitlesPage();
        titlesPage.removeTitle("Don Quixote");

        softAssert.assertAll();
    }

    @DataProvider(name = "purchaseDatesProvider")
    public Object[][] purchaseDatesProvider() {
        return new Object[][] {
                {"2025-10-26"},
                {"2025-10-27"},
                {"2026-03-29"},
                {"2026-03-30"}
        };
    }
}
