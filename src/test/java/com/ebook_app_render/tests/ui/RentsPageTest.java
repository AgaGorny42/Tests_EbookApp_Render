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
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class RentsPageTest extends BaseUiTest {

    private TitlesPage titlesPage;
    private final RentsPage rentsPage = new RentsPage();
    private final String dateToday = LocalDate.now().format(DateTimeFormatter.ISO_LOCAL_DATE);
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
    public void rentsPageIsLoadedTest() {
        titlesPage.addTitleAndOpenListOfCopiesOfThisTitle("No logo", "Naomi Klein", "2004");
        rentsPage.goToRentsPage("2021-01-01", "Available");

        softAssert.assertTrue(rentsPage.rentsPageIsDisplayed(), "The rents page has not been loaded.");

        rentsPage.removeHistory("No logo", "Available");

        softAssert.assertAll();
    }

    @Test
    public void shouldRentItemTest() {
        titlesPage.addTitleAndOpenListOfCopiesOfThisTitle("No logo", "Naomi Klein", "2004");
        rentsPage.goToRentsPage("2021-01-01", "Available")
                .rentItemWithRentDateForToday("Jack Sparrow");

        waitForFogAnimatedToDisappear();
        String actualCustomerName = rentsPage.findRentalByCustomerName("Jack Sparrow").getCustomerName();
        String expectedCustomerName = "JACK SPARROW";

        softAssert.assertEquals(actualCustomerName, expectedCustomerName, "Customer name has not been found.");

        rentsPage.removeRental("Jack Sparrow");
        rentsPage.removeHistory("No logo", "Available");

        softAssert.assertAll();
    }

    @Test
    public void theRentalDateIsSetForTodayTest() {
        titlesPage.addTitleAndOpenListOfCopiesOfThisTitle("No logo", "Naomi Klein", "2004");
        rentsPage.goToRentsPage("2021-01-01", "Available")
                .rentItemWithRentDateForToday("Jack Sparrow");

        waitForFogAnimatedToDisappear();
        String actualRentDate = rentsPage.findRentalByCustomerName("Jack Sparrow").toDTO().getRentDate();

        softAssert.assertEquals(actualRentDate, dateToday, "The rental date is not today.");

        rentsPage.removeRental("Jack Sparrow");
        rentsPage.removeHistory("No logo", "Available");
        softAssert.assertAll();
    }

    @Test
    public void theRentalShouldBeSetUpFor3DaysTest() {
        titlesPage.addTitleAndOpenListOfCopiesOfThisTitle("No logo", "Naomi Klein", "2004");
        rentsPage.goToRentsPage("2021-01-01", "Available")
                .rentItemWithRentDateForToday("Jack Sparrow");

        waitForFogAnimatedToDisappear();
        String actualExpirationDate = rentsPage.findRentalByCustomerName("Jack Sparrow").toDTO().getExpirationDate();

        LocalDate actual = LocalDate.parse(actualExpirationDate);
        LocalDate expectedExpirationDate = LocalDate.now().plusDays(3);

        softAssert.assertEquals(actual, expectedExpirationDate, "The rental is not set for 3 days.");

        rentsPage.removeRental("Jack Sparrow");
        rentsPage.removeHistory("No logo", "Available");
        softAssert.assertAll();
    }

    @Test(dataProvider = "newDates")
    public void shouldCheckRentalDurationTest(String newDates) {
        titlesPage.addTitleAndOpenListOfCopiesOfThisTitle("No logo", "Naomi Klein", "2004");
        rentsPage.goToRentsPage("2021-01-01", "Available")
                .rentItemWithRentDateSetUpNotForToday("Jack Sparrow", newDates);

        waitForFogAnimatedToDisappear();
        String actualExpirationDate = rentsPage.findRentalByCustomerName("Jack Sparrow").toDTO().getExpirationDate();
        LocalDate actual = LocalDate.parse(actualExpirationDate);
        LocalDate expectedExpirationDate = LocalDate.parse(newDates).plusDays(3);

        softAssert.assertEquals(actual, expectedExpirationDate,
                "The expected expiration date is not set up for 3 days.");

        rentsPage.removeRental("Jack Sparrow");
        rentsPage.removeHistory("No logo", "Available");
        softAssert.assertAll();
    }//the expirationDate is always set up for 3 days from actual rental date, the change of rental date
    //does not change the expiration date accordingly.

    @Test
    public void itemStatusShouldChangeToRentedAfterRentalTest() {
        titlesPage.addTitleAndOpenListOfCopiesOfThisTitle("No logo", "Naomi Klein", "2004");
        rentsPage.goToRentsPage("2021-01-01", "Available")
                .rentItemWithRentDateForToday("Jack Sparrow");
        ItemsPage itemsPage = rentsPage.returnToItemPage();

        waitForFogAnimatedToDisappear();
        String actualStatus = itemsPage.findItemByPurchaseDate("2021-01-01").getStatus();
        String expectedStatus = "Rented";

        softAssert.assertEquals(actualStatus, expectedStatus, "The status has not changed to rented.");

        itemsPage.findItemByPurchaseDate("2021-01-01").clickShowHistory();
        rentsPage.waitForPageToBeLoaded();
        rentsPage.removeRental("Jack Sparrow");
        rentsPage.removeHistory("No logo", "Available");
        softAssert.assertAll();
    }

    @Test
    public void shouldBePossibleToChangeCustomerNameTest() {
        titlesPage.addTitleAndOpenListOfCopiesOfThisTitle("No logo", "Naomi Klein", "2004");
        rentsPage.goToRentsPage("2021-01-01", "Available")
                .rentItemWithRentDateForToday("Jack Sparrow");

        rentsPage.changeCustomerName("Jack Sparrow", "Aga");

        waitForFogAnimatedToDisappear();
        String actualCustomerName = rentsPage.findRentalByCustomerName("Aga").getCustomerName();
        String expectedCustomerName = "AGA";

        softAssert.assertEquals(actualCustomerName, expectedCustomerName,
                "The customer name has not been changed.");

        rentsPage.removeRental("Aga");
        rentsPage.removeHistory("No logo", "Available");
        softAssert.assertAll();
    }

    @Test
    public void shouldBePossibleToChangeRentDateTest() {
        titlesPage.addTitleAndOpenListOfCopiesOfThisTitle("No logo", "Naomi Klein", "2004");
        rentsPage.goToRentsPage("2021-01-01", "Available")
                .rentItemWithRentDateForToday("Jack Sparrow");

        rentsPage.changeRentDate("Jack Sparrow", "2025-10-16");

        String actualDate = rentsPage.findRentalByCustomerName("Jack Sparrow").toDTO().getRentDate();
        String expectedDate = "2025-10-16";

        softAssert.assertEquals(actualDate, expectedDate,
                "The date after change is incorrect.");

        rentsPage.removeRental("Jack Sparrow");
        rentsPage.removeHistory("No logo", "Available");
        softAssert.assertAll();
    }

    @Test(dataProvider = "newDates")
    public void shouldBePossibleToChangeExpirationDateTest(String newDates) {
        titlesPage.addTitleAndOpenListOfCopiesOfThisTitle("No logo", "Naomi Klein", "2004");
        rentsPage.goToRentsPage("2021-01-01", "Available")
                .rentItemWithRentDateForToday("Jack Sparrow");

        rentsPage.changeExpirationDate("Jack Sparrow", newDates);

        waitForFogAnimatedToDisappear();
        String actualExpDate = rentsPage.findRentalByCustomerName("Jack Sparrow").toDTO().getExpirationDate();

        softAssert.assertEquals(actualExpDate, newDates,
                "The expiration date is incorrect, one day before the expected one.");

        rentsPage.removeRental("Jack Sparrow");
        rentsPage.removeHistory("No logo", "Available");
        softAssert.assertAll();
    }

    @DataProvider(name = "newDates")
    public Object[][] newDates() {
        return new Object[][] {
                {"2025-03-29"},
                {"2025-03-30"},
                {"2025-04-29"},
                {"2025-10-29"},
                {"2025-11-29"},
                {"2025-11-30"}
        };
    }

    @Test
    public void shouldBePossibleToRemoveRentHistoryTest() {
        titlesPage.addTitleAndOpenListOfCopiesOfThisTitle("No logo", "Naomi Klein", "2004");
        rentsPage.goToRentsPage("2021-01-01", "Available")
                .rentItemWithRentDateForToday("Tom");

        rentsPage.removeRental("Tom");

        softAssert.assertFalse(rentsPage.containsRentalWithCustomerName("Tom"),
                "The customer name is still on rental list.");

        rentsPage.removeHistory("No logo", "Available");
        softAssert.assertAll();
    }
}
