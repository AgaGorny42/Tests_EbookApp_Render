package com.ebook_app_render.ui.pages;

import org.openqa.selenium.*;
import org.openqa.selenium.WebElement;

public class ItemRow extends BaseComponentPage {

    private final By PURCHASE_DATE_BY = By.cssSelector(".items-list__item__purchase-date");
    private final By STATUS_BY = By.cssSelector(".items-list__item__status");
    private final By SHOW_RENTS_BUTTON_BY = By.cssSelector(".show-rents-btn");
    private final By EDIT_BUTTON_BY = By.cssSelector(".edit-btn");
    private final By REMOVE_BUTTON_BY = By.cssSelector(".remove-btn");

    public ItemRow(WebElement rootElement) {
        super(rootElement);
    }

    public String getPurchaseDate() {
        return rootElement.findElement(PURCHASE_DATE_BY).getText();
    }

    public String getStatus() {
        return rootElement.findElement(STATUS_BY).getText();
    }

    public void clickShowHistory() {
        WebElement showHistoryButton = rootElement.findElement(SHOW_RENTS_BUTTON_BY);
        clickWhenReady(showHistoryButton);
    }

    public void clickEdit() {
        WebElement editButton = rootElement.findElement(EDIT_BUTTON_BY);
        clickWhenReady(editButton);
    }

    public void clickRemove() {
        WebElement removeButton = rootElement.findElement(REMOVE_BUTTON_BY);
        clickWhenReady(removeButton);
    }
}

