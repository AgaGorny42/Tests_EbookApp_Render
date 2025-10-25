package com.ebook_app_render.tests.ui;

import com.ebook_app_render.ui.pages.ItemsPage;
import com.ebook_app_render.ui.pages.LoginPage;
import com.ebook_app_render.ui.pages.RentsPage;
import com.ebook_app_render.ui.pages.TitlesPage;
import com.ebook_app_render.ui.utils.DriverSingleton;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

public class ItemsPageTest extends BaseUiTest {

    private TitlesPage titlesPage;
    private ItemsPage itemsPage;
    SoftAssert softAssert;

    @BeforeClass
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
    public void shouldCheckIfItemsPageIsLoadedTest() {
        itemsPage = titlesPage.addTitleAndOpenListOfCopiesOfThisTitle("Beloved", "Toni Morrison", "1987");

        String actualText = itemsPage.getTextFromElement();
        String expectedText = "LIST OF COPIES";

        softAssert.assertTrue(itemsPage.itemsPageIsLoaded(), "Items page has not been loaded.");
        softAssert.assertEquals(actualText, expectedText, "List of copies header text is incorrect.");

        itemsPage.returnToTitlesPage().waitForTitlesToLoad();
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
        itemsPage.returnToTitlesPage().waitForTitlesToLoad();
        titlesPage.removeTitle("No logo");

        softAssert.assertAll();
    }

    @Test
    public void shouldBePossibleToDeleteNotRentedItemTest(){
        itemsPage = titlesPage.addTitleAndOpenListOfCopiesOfThisTitle("Winnie the Pooh", "A.A. Milne", "2015");

        itemsPage.clickAddNewButtonBy().setPurchaseDate("2021-01-10");
        itemsPage.clickSubmitButtonBy().findItemByStatus("Available").clickRemove();

        waitForFogAnimatedToDisappear();
        String actualAlert = itemsPage.getAlertMessage();
        String expectedAlert = "No copies...";

        softAssert.assertEquals(actualAlert, expectedAlert, "The item list is not empty.");

        itemsPage.returnToTitlesPage().waitForTitlesToLoad();
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

        waitForFogAnimatedToDisappear();
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

        String expectedPurchaseDate = "2020-03-10";
        String actualPurchaseDate = itemsPage.findItemByStatus("Available").getPurchaseDate();

        softAssert.assertEquals(actualPurchaseDate, expectedPurchaseDate, "The displayed date is not the chosen one");

        itemsPage.removeItem("2020-03-10");
        itemsPage.waitForPageToBeLoaded();
        itemsPage.returnToTitlesPage().waitForTitlesToLoad();
        titlesPage.removeTitle("White Nights");

        softAssert.assertAll();
    }

    @Test(dataProvider = "purchaseDatesProvider")
    public void shouldAddItemToTitleWith29thOfMonth(String purchaseDate) {
        SoftAssert softAssert = new SoftAssert();

        itemsPage = titlesPage.addTitleAndOpenListOfCopiesOfThisTitle("No logo", "Naomi Klein", "2004");
        itemsPage.clickAddNewButtonBy().setPurchaseDate(purchaseDate);
        itemsPage.clickSubmitButtonBy();

        String actualDate = itemsPage.findItemByStatus("Available").getPurchaseDate();

        softAssert.assertEquals(actualDate, purchaseDate, "The displayed date is not the chosen one");

        itemsPage.removeItemByStatus("Available");
        itemsPage.waitForPageToBeLoaded();
        itemsPage.returnToTitlesPage().waitForTitlesToLoad();
        titlesPage.removeTitle("No logo");

        softAssert.assertAll();
    }

    @DataProvider(name = "purchaseDatesProvider")
    public Object[][] purchaseDatesProvider() {
        Object[][] dates = new Object[12][1];
        for (int month = 1; month <= 12; month++) {
            String monthStr = month < 10 ? "0" + month : String.valueOf(month);
            dates[month - 1][0] = "2025-" + monthStr + "-29";
        }
        return dates;
    }
}
