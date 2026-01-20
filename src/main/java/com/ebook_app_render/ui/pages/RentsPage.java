package com.ebook_app_render.ui.pages;

import com.ebook_app_render.api.dto.NewRentDTO;
import com.ebook_app_render.ui.utils.DatePeaker;
import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.util.List;
import java.util.NoSuchElementException;

import static com.ebook_app_render.ui.utils.WaitingTime.WAITING_TIME;

public class RentsPage extends BasePage {

    TitlesPage titlesPage = new TitlesPage();
    ItemsPage itemsPage = new ItemsPage();
    DatePeaker datePeaker = new DatePeaker();

    private final By RETURN_BUTTON_BY = By.cssSelector("#return-button");
    private final By RENT_THIS_COPY_BUTTON_BY = By.cssSelector("#add-rent-button");
    private final By CUSTOMER_NAME_INPUT_BY = By.cssSelector(".input-field__input[name='customer-name']");
    private final By RENT_DATE_READ_ONLY_CLICK_BY = By.cssSelector("#id[name='rent-date']");
    private final By EXPIRATION_DATE_READ_ONLY_CLICK_BY = By.cssSelector("#id[name='expiration-date']");
    private final By SUBMIT_BUTTON_BY = By.cssSelector(".btn.btn--primary[name='submit-button'");
    private final By RENT_ROW_LIST_BY = By.cssSelector(".rents-list__rent.list__item");

    public boolean rentsPageIsDisplayed() {
        return waitForElement(RENT_THIS_COPY_BUTTON_BY).isDisplayed();
    }

    public RentsPage goToRentsPage(String itemPurchaseDate, String itemStatus) {
        itemsPage.clickAddNewButtonBy().setPurchaseDate(itemPurchaseDate);
        itemsPage.clickSubmitButtonBy();
        itemsPage.waitForPageToBeLoaded();
        itemsPage.findItemByStatus(itemStatus).clickShowHistory();

        wait.until(ExpectedConditions.urlContains("rents"));

        RentsPage rentsPage = new RentsPage();
        rentsPage.waitForPageToBeLoaded();
        return rentsPage;
    }

    public void clickRentThisCopyButton() {
        clickWhenReady(RENT_THIS_COPY_BUTTON_BY);
    }

    public void inputCustomerName(String customerName) {
        waitForElement(CUSTOMER_NAME_INPUT_BY).sendKeys(customerName);
    }

    public void clickSubmitButton() {
        clickWhenReady(SUBMIT_BUTTON_BY);
        waitForFogAnimatedToDisappear();
    }

    public void rentItemWithRentDateForToday(String customerName) {
        clickRentThisCopyButton();
        inputCustomerName(customerName);
        clickSubmitButton();
        waitForPageToBeLoaded();
        waitForLoaderToDisappear(LOADER_BY, RENT_ROW_LIST_BY);
    }

    public void changeCustomerName(String customerName, String newCustomerName) {
        findRentalByCustomerName(customerName).clickEdit();
        waitForElement(CUSTOMER_NAME_INPUT_BY).clear();
        clickWhenReady(CUSTOMER_NAME_INPUT_BY);
        waitForElement(CUSTOMER_NAME_INPUT_BY).sendKeys(newCustomerName);
        clickSubmitButton();
        waitForFogAnimatedToDisappear();
    }

    public void changeRentDate(String customerName, String newDate) {
        findRentalByCustomerName(customerName).clickEdit();
        clickWhenReady(RENT_DATE_READ_ONLY_CLICK_BY);
        datePeaker.setDate(newDate);
        clickSubmitButton();
        waitForFogAnimatedToDisappear();
        waitForPageToBeLoaded();
    }

    public void changeExpirationDate(String customerName, String newDate) {
        findRentalByCustomerName(customerName).clickEdit();
        clickWhenReady(EXPIRATION_DATE_READ_ONLY_CLICK_BY);
        datePeaker.setDate(newDate);
        clickSubmitButton();
        waitForFogAnimatedToDisappear();
        waitForPageToBeLoaded();
    }

    public void removeRental(String customerName) {
        waitForFogAnimatedToDisappear();
        waitForPageToBeLoaded();
        findRentalByCustomerName(customerName).clickRemove();
        waitForFogAnimatedToDisappear();
    }

    public ItemsPage returnToItemPage() {
        waitForFogAnimatedToDisappear();
        clickWhenReady(RETURN_BUTTON_BY);

        wait.until(ExpectedConditions.urlContains("items"));

        ItemsPage itemsPage = new ItemsPage();
        itemsPage.waitForPageToBeLoaded();
        return itemsPage;
    }

    public void removeHistory(String title, String status) {
        waitForFogAnimatedToDisappear();
        returnToItemPage();
        itemsPage.findItemByStatus(status).clickRemove();
        waitForFogAnimatedToDisappear();
        itemsPage.waitForPageToBeLoaded();
        itemsPage.returnToTitlesPage();
        titlesPage.waitForPageToBeLoaded();
        titlesPage.removeTitle(title);
        titlesPage.waitForPageToBeLoaded();

    }

    public List<RentRow> getAllRents() {
        return driver.findElements(RENT_ROW_LIST_BY)
                .stream()
                .map(RentRow::new).toList();
    }

    public List<NewRentDTO> getAllRentsAsDTOs() {
        return getAllRents().stream()
                .map(RentRow::toDTO)
                .toList();
    }

    public boolean containsRentalWithCustomerName(String customerName) {
        return getAllRentsAsDTOs().stream()
                .anyMatch(newRentDTO -> newRentDTO.getCustomerName()
                        .equalsIgnoreCase(customerName));
    }

    public RentRow findRentalByCustomerName(String customerName) {
        waitForFogAnimatedToDisappear();
        return getAllRents().stream()
                .filter(rentRow -> rentRow.getCustomerName() != null &&
                        rentRow.getCustomerName().equalsIgnoreCase(customerName))
                .findFirst()
                .orElseThrow(() ->new NoSuchElementException(
                        "No rental found with customer name: " + customerName));
    }

    @Override
    public void waitForPageToBeLoaded() {
        WebDriverWait customWait = new WebDriverWait(driver, WAITING_TIME.getDuration());
                customWait.until(ExpectedConditions.textToBe(By.cssSelector(TEXT_ON_PAGE_CSS)
                        , "RENTS HISTORY"));
    }
}
