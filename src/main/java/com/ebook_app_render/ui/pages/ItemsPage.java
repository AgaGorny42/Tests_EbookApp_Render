package com.ebook_app_render.ui.pages;

import com.ebook_app_render.ui.utils.DatePeaker;
import com.ebook_app_render.ui.utils.DriverSingleton;
import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.Duration;
import java.util.List;
import java.util.NoSuchElementException;

import static com.ebook_app_render.ui.utils.WaitingTime.WAITING_TIME;

public class ItemsPage extends BasePage {

    DatePeaker datePeaker = new DatePeaker();

    private final By ADD_NEW_BUTTON_BY = By.cssSelector(ADD_NEW_ITEM_BUTTON_CSS);
    private final By RETURN_BUTTON_BY = By.cssSelector("#return-button");
    private final By ADD_COPY_BUTTON_BY = By.cssSelector(".btn.btn--primary");
    private final By LIST_OF_COPIES_H2_BY = By.cssSelector(".sub-title.flex-grow--1.margin-right--1");
    private final By ITEM_LIST_ITEM_BY = By.cssSelector(".items-list__item.list__item");
    private final By ITEM_LIST_BY = By.cssSelector(".items-list.list");
    private final By INPUT_BY = By.cssSelector("input[name='purchase-date']");

    public void waitForItemsToLoad() {
        waitForLoaderToDisappear(LOADER_BY, ITEM_LIST_BY, Duration.ofSeconds(10));
    }

    public void setPurchaseDate(String date) {
        clickWhenReady(INPUT_BY);
        datePeaker.setDate(date);
    }

    public ItemsPage clickAddNewButtonBy() {
        clickWhenReady(ADD_NEW_BUTTON_BY);
        return new ItemsPage();
    }

    public ItemsPage clickSubmitButtonBy() {
        clickWhenReady(ADD_COPY_BUTTON_BY);
        waitForLoaderToDisappear(LOADER_BY, ITEM_LIST_BY, Duration.ofSeconds(10));
        return new ItemsPage();
    }

    public void addItem(String purchaseDate) {
        clickAddNewButtonBy().setPurchaseDate(purchaseDate);
        clickSubmitButtonBy().waitForItemsToLoad();
    }

    public RentsPage addItemAndOpenRentHistory(String purchaseDate, String status) {
        clickAddNewButtonBy().setPurchaseDate(purchaseDate);
        clickSubmitButtonBy().findItemByStatus(status).clickShowHistory();

        WebDriverWait wait = new WebDriverWait(DriverSingleton.getDriver(), Duration.ofSeconds(15));
        wait.until(ExpectedConditions.urlContains("rents"));

        RentsPage rentsPage = new RentsPage();
        rentsPage.waitForPageToBeLoaded();
        return rentsPage;
    }

    public String getTextFromElement() {
        return waitForElement(LIST_OF_COPIES_H2_BY).getText();
    }

    public TitlesPage returnToTitlesPage() {
        clickReturnButtonWhenReady(RETURN_BUTTON_BY);
        waitForPageToBeLoaded();
        TitlesPage titlesPage = new TitlesPage();
        titlesPage.waitForPageToBeLoaded();
        return titlesPage;
    }

    public List<ItemRow> getAllItems() {
        waitForItemsToLoad();
        return driver.findElements(ITEM_LIST_ITEM_BY)
                .stream()
                .map(ItemRow::new)
                .toList();
    }

    public ItemRow findItemByStatus(String status) {
        return getAllItems().stream()
                .filter(r -> r.getStatus().equalsIgnoreCase(status))
                .findFirst()
                .orElseThrow(() -> new NoSuchElementException(
                        "No item found with status: " + status));
    }

    public ItemRow findItemByPurchaseDate(String purchaseDate) {
        return getAllItems().stream()
                .filter(r -> r.getPurchaseDate().equals(purchaseDate))
                .findFirst()
                .orElseThrow(() -> new NoSuchElementException(
                        "No item found with purchase date: " + purchaseDate));
    }

    public void removeItem(String purchaseDate) {
        findItemByPurchaseDate(purchaseDate).clickRemove();
        waitForFogAnimatedToDisappear();
    }

    public void removeItemByStatus(String status) {
        findItemByStatus(status).clickRemove();
        waitForFogAnimatedToDisappear();
    }

    public boolean itemsPageIsLoaded() {
        return driver.findElement(LIST_OF_COPIES_H2_BY).isDisplayed();
    }

    public String getAlertMessage() {
        return waitForElement(ALERT_CONTENT_BY).getText();
    }

    @Override
    public void waitForPageToBeLoaded() {
        new WebDriverWait(driver, WAITING_TIME.getDuration())
                .until(ExpectedConditions.textToBe(By.cssSelector(TEXT_ON_PAGE_CSS)
                        , "LIST OF COPIES"));
    }
}
