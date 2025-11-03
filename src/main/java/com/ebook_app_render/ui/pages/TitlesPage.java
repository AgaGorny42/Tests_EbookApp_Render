package com.ebook_app_render.ui.pages;

import com.ebook_app_render.api.dto.TitleDTO;
import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.util.List;
import java.util.NoSuchElementException;
import static com.ebook_app_render.ui.utils.WaitingTime.WAITING_TIME;

public class TitlesPage extends BasePage {

    private final By ADD_NEW_TITLE_BUTTON_BY = By.cssSelector("#add-title-button");
    private final By TITLE_INPUT_BY = By.cssSelector(".input-field__input[name='title']");
    private final By AUTHOR_INPUT_BY = By.cssSelector(".input-field__input[name='author']");
    private final By YEAR_INPUT_BY = By.cssSelector(".input-field__input[name='year']");
    private final By ADD_TITTLE_BUTTON_BY = By.cssSelector(".btn.btn--primary[name='submit-button']");
    private final By ERROR_MESSAGE_BY = By.cssSelector(".alert__content");
    private final By TITLES_LIST_BY = By.cssSelector(".titles-list.list");
    private final By TITLES_LIST_ITEM_BY = By.cssSelector(".titles-list__item");
    private final By LOADER_BY = By.cssSelector(".lds-ripple");
    private final By TITLES_CATALOG = By.cssSelector(".sub-title.flex-grow--1.margin-right--1");
    private final By EDIT_TITLE_TITLE_BY = By.cssSelector("input.input-field__input[name='title']");
    private final By EDIT_TITLE_AUTHOR_BY = By.cssSelector("input.input-field__input[name='author']");
    private final By EDIT_TITLE_YEAR_BY = By.cssSelector("input.input-field__input[name='year']");
    private final By EDIT_TITLE_BUTTON_BY = By.cssSelector(".btn.btn--primary[name='submit-button']");

    public TitlesPage clickAddNewTitleButton() {
        waitForPageToBeLoaded();
        clickWhenReady(ADD_NEW_TITLE_BUTTON_BY);
        return this;
    }

    public String getTextFromElement() {
        return waitForElement(TITLES_CATALOG).getText();
    }

    public void addTitle(String title, String author, String year) {
        driver.findElement(TITLE_INPUT_BY).sendKeys(title);
        driver.findElement(AUTHOR_INPUT_BY).sendKeys(author);
        driver.findElement(YEAR_INPUT_BY).sendKeys(year);
        clickWhenReady(ADD_TITTLE_BUTTON_BY);
        waitForFogAnimatedToDisappear();
        waitForTitlesToLoad();
    }

    public void waitForTitlesToLoad() {
        waitForLoaderToDisappear(LOADER_BY, TITLES_LIST_BY);
    }

    public List<TitleRow> getAllTitles() {
        return driver.findElements(TITLES_LIST_ITEM_BY)
                .stream()
                .map(TitleRow::new)
                .toList();
    }

    public List<TitleDTO> getAllTitlesAsDTOs() {
        return getAllTitles().stream()
                .map(TitleRow::toDTO)
                .toList();
    }

    public boolean containsBook(String title, String author, int year) {
        return getAllTitlesAsDTOs().stream()
                .anyMatch(dto -> dto.getTitle().equalsIgnoreCase(title)
                        && dto.getAuthor().equalsIgnoreCase(author)
                        && dto.getYear() == year);
    }

    public TitleRow findTitleByName(String title) {
        return getAllTitles().stream()
                .filter(r -> r.getTitle().equalsIgnoreCase(title))
                .findFirst()
                .orElseThrow(() -> new NoSuchElementException(
                        "No title found with name: " + title
                ));
    }

    public void removeTitle(String title) {
        waitForFogAnimatedToDisappear();
        waitForPageToBeLoaded();
        findTitleByName(title).clickRemove();
        waitForFogAnimatedToDisappear();
    }

    public void editTitle(String title, String author, String year) {
        waitForPageToBeLoaded();
        waitForTitlesToLoad();
        findTitleByName(title).clickEdit();
        clearFormEditTitle();
        waitForElement(TITLE_INPUT_BY).sendKeys(title);
        waitForElement(AUTHOR_INPUT_BY).sendKeys(author);
        waitForElement(YEAR_INPUT_BY).sendKeys(year);
        clickWhenReady(EDIT_TITLE_BUTTON_BY);
        waitForTitlesToLoad();
    }

    public void clearFormEditTitle() {
        waitForElement(EDIT_TITLE_TITLE_BY).clear();
        waitForElement(EDIT_TITLE_AUTHOR_BY).clear();
        waitForElement(EDIT_TITLE_YEAR_BY).clear();
    }

    public ItemsPage showCopiesOfTitle(String title) {
        waitForFogAnimatedToDisappear();
        findTitleByName(title).clickShowCopies();

        wait.until(ExpectedConditions.urlContains("items"));

        ItemsPage itemsPage = new ItemsPage();
        itemsPage.waitForPageToBeLoaded();
        return itemsPage;
    }

    public ItemsPage addTitleAndOpenListOfCopiesOfThisTitle(String title, String author, String year) {
        clickAddNewTitleButton().addTitle(title, author, year);
        showCopiesOfTitle(title);

        wait.until(ExpectedConditions.urlContains("items"));

        ItemsPage itemsPage = new ItemsPage();
        itemsPage.waitForPageToBeLoaded();
        return itemsPage;
    }

    public String getErrorMessage() {
        return waitForElement(ERROR_MESSAGE_BY).getText();
    }

    @Override
    public void waitForPageToBeLoaded() {
        WebDriverWait customWait = new WebDriverWait(driver, WAITING_TIME.getDuration());
        customWait.until(ExpectedConditions.visibilityOfElementLocated(By.id(TITLES_ID_BY)));
        customWait.until(ExpectedConditions.textToBe(By.cssSelector(TEXT_ON_PAGE_CSS), "TITLES CATALOG"));
    }

    public boolean isLoaded() {
        return driver.findElement(TITLES_CATALOG).isDisplayed();
    }
}
