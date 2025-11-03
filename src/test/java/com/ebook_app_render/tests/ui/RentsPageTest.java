package com.ebook_app_render.tests.ui;

import com.aventstack.extentreports.ExtentTest;
import com.ebook_app_render.ui.pages.ItemsPage;
import com.ebook_app_render.ui.pages.LoginPage;
import com.ebook_app_render.ui.pages.RentsPage;
import com.ebook_app_render.ui.pages.TitlesPage;
import io.qameta.allure.Allure;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class RentsPageTest extends BaseUiTest {

    private TitlesPage titlesPage;
    private RentsPage rentsPage;
    private final String dateToday = LocalDate.now().format(DateTimeFormatter.ISO_LOCAL_DATE);
    private SoftAssert softAssert;
    private ExtentTest extentTest;

    @BeforeMethod
    public void setup() {
        driver.get("https://ta-bookrental-fe.onrender.com/login");

        loginPage = new LoginPage();
        loginPage.waitForPageToBeLoaded();

        titlesPage = loginPage.successfulLogin(USER_LOGIN, USER_PASSWORD);
        titlesPage.waitForPageToBeLoaded();

        rentsPage = new RentsPage();
        softAssert = new SoftAssert();
    }

    @Test
    public void rentsPageIsLoadedCorrectly() {
        titlesPage.addTitleAndOpenListOfCopiesOfThisTitle("Title 1", "Author 1", "2004");
        rentsPage.goToRentsPage("2021-01-01", "Available");

        softAssert.assertTrue(rentsPage.rentsPageIsDisplayed(), "The rents page has not been loaded.");

        rentsPage.removeHistory("Title 1", "Available");
        softAssert.assertAll();
    }

    @Test
    public void shouldBePossibleToRentItem() {
        titlesPage.addTitleAndOpenListOfCopiesOfThisTitle("Title 2", "Author 2", "2004");
        rentsPage.goToRentsPage("2021-01-01", "Available")
                .rentItemWithRentDateForToday("Jack Sparrow");

        waitForFogAnimatedToDisappear();
        String actualCustomerName = rentsPage.findRentalByCustomerName("Jack Sparrow").getCustomerName();
        String expectedCustomerName = "JACK SPARROW";

        softAssert.assertEquals(actualCustomerName, expectedCustomerName, "Customer name has not been found.");

        rentsPage.removeRental("Jack Sparrow");
        rentsPage.removeHistory("Title 2", "Available");

        softAssert.assertAll();
    }

    @Test
    public void theRentalDateShouldBeSetForToday() {
        titlesPage.addTitleAndOpenListOfCopiesOfThisTitle("Title 3", "Author 3", "2004");
        rentsPage.goToRentsPage("2021-01-01", "Available")
                .rentItemWithRentDateForToday("Jack Sparrow");

        waitForFogAnimatedToDisappear();
        String actualRentDate = rentsPage.findRentalByCustomerName("Jack Sparrow").toDTO().getRentDate();

        softAssert.assertEquals(actualRentDate, dateToday, "The rental date is not today.");

        rentsPage.removeRental("Jack Sparrow");
        rentsPage.removeHistory("Title 3", "Available");
        softAssert.assertAll();
    }

    @Test
    public void theRentalShouldBeSetUpFor3Days() {
        titlesPage.addTitleAndOpenListOfCopiesOfThisTitle("Title 4", "Author 4", "2004");
        rentsPage.goToRentsPage("2021-01-01", "Available")
                .rentItemWithRentDateForToday("Jack Sparrow");

        waitForFogAnimatedToDisappear();
        String actualExpirationDate = rentsPage.findRentalByCustomerName("Jack Sparrow").toDTO().getExpirationDate();

        LocalDate actual = LocalDate.parse(actualExpirationDate);
        LocalDate expectedExpirationDate = LocalDate.now().plusDays(3);

        softAssert.assertEquals(actual, expectedExpirationDate, "The rental is not set for 3 days.");

        rentsPage.removeRental("Jack Sparrow");
        rentsPage.removeHistory("Title 4", "Available");
        softAssert.assertAll();
    }

    @Test
    public void itemStatusShouldChangeToRentedAfterRental() {
        extentTest = extent.createTest("Verifying purchase date during DST for date: ");

        titlesPage.addTitleAndOpenListOfCopiesOfThisTitle("Title 5", "Author 5", "2004");
        rentsPage.goToRentsPage("2021-01-01", "Available")
                .rentItemWithRentDateForToday("Jack Sparrow");
        ItemsPage itemsPage = rentsPage.returnToItemPage();

        waitForFogAnimatedToDisappear();
        String actualStatus = itemsPage.findItemByPurchaseDate("2021-01-01").getStatus();
        String expectedStatus = "RENTED";

        softAssert.assertEquals(actualStatus, expectedStatus, "The status has not changed to rented.");

        extentTest.info("Verifying status of rented item. Expected: '"
                + expectedStatus + "', Actual: '" + actualStatus + "'");
        Allure.step("Verifying status of rented item. Expected: '"
                + expectedStatus + "', Actual: '" + actualStatus + "'");

        itemsPage.findItemByPurchaseDate("2021-01-01").clickShowHistory();
        rentsPage.removeRental("Jack Sparrow");
        rentsPage.removeHistory("Title 5", "Available");
        softAssert.assertAll();
    }

    @Test
    public void shouldBePossibleToChangeCustomerName() {
        titlesPage.addTitleAndOpenListOfCopiesOfThisTitle("Title 6", "Author 6", "2004");
        rentsPage.goToRentsPage("2021-01-01", "Available")
                .rentItemWithRentDateForToday("Jack Sparrow");

        rentsPage.changeCustomerName("Jack Sparrow", "Aga");

        waitForFogAnimatedToDisappear();
        String actualCustomerName = rentsPage.findRentalByCustomerName("Aga").getCustomerName();
        String expectedCustomerName = "AGA";

        softAssert.assertEquals(actualCustomerName, expectedCustomerName,
                "The customer name has not been changed.");

        rentsPage.removeRental("Aga");
        rentsPage.removeHistory("Title 6", "Available");
        softAssert.assertAll();
    }

    @Test
    public void shouldBePossibleToChangeRentDate() {
        titlesPage.addTitleAndOpenListOfCopiesOfThisTitle("Title 7", "Author 7", "2004");
        rentsPage.goToRentsPage("2023-01-01", "Available")
                .rentItemWithRentDateForToday("Jack Sparrow");

        rentsPage.changeRentDate("Jack Sparrow", "2025-11-16");

        rentsPage.waitForLoaderToDisappear();
        String actualDate = rentsPage.findRentalByCustomerName("Jack Sparrow").toDTO().getRentDate();
        String expectedDate = "2025-11-16";

        softAssert.assertEquals(actualDate, expectedDate,
                "The date after change is incorrect. The date is shifted by one day during the daylight saving time period..");

        rentsPage.removeRental("Jack Sparrow");
        rentsPage.removeHistory("Title 7", "Available");
        softAssert.assertAll();
    }

    @Test(dataProvider = "newDates")
    public void shouldChangeExpirationDateCorrectly_duringDaylightSavingTime(String newDates) {
        extentTest = extent.createTest("Verifying purchase date during DST for date: " + newDates);

        titlesPage.addTitleAndOpenListOfCopiesOfThisTitle("Title 8", "Author 8", "2004");
        rentsPage.goToRentsPage("2021-01-01", "Available")
                .rentItemWithRentDateForToday("Jack Sparrow");

        rentsPage.changeExpirationDate("Jack Sparrow", newDates);

        waitForFogAnimatedToDisappear();
        String actualExpDate = rentsPage.findRentalByCustomerName("Jack Sparrow").toDTO().getExpirationDate();

        waitForFogAnimatedToDisappear();
        softAssert.assertEquals(actualExpDate, newDates,
                "Dates are shifted by one day during the daylight saving time period.");

        extentTest.info("Verifying expiration date during DST. Expected: '"
                + newDates + "', Actual: '" + actualExpDate + "'");
        Allure.step("Verifying expiration date during DST. Expected: '"
                + newDates + "', Actual: '" + actualExpDate + "'");

        rentsPage.removeRental("Jack Sparrow");
        rentsPage.removeHistory("Title 8", "Available");

        softAssert.assertAll();
    }

    @DataProvider(name = "newDates")
    public Object[][] newDates() {
        return new Object[][] {
                {"2025-10-26"},
                {"2025-10-27"},
                {"2026-03-29"},
                {"2026-03-30"}
        };
    }

    @Test
    public void shouldBePossibleToRemoveRentHistory() {
        titlesPage.addTitleAndOpenListOfCopiesOfThisTitle("No logo", "Naomi Klein", "2004");
        rentsPage.goToRentsPage("2021-01-01", "Available")
                .rentItemWithRentDateForToday("Tom");

        rentsPage.removeRental("Tom");

        waitForFogAnimatedToDisappear();
        rentsPage.waitForPageToBeLoaded();
        softAssert.assertFalse(rentsPage.containsRentalWithCustomerName("Tom"),
                "The customer name is still on rental list.");

        rentsPage.removeHistory("No logo", "Available");
        softAssert.assertAll();
    }
}
