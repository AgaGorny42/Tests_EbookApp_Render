package com.ebook_app_render.ui.pages;

import com.ebook_app_render.api.dto.TitleDTO;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

public class TitleRow extends BaseComponentPage {

    private final By TITLE_BY = By.cssSelector(".titles-list__item__title");
    private final By AUTHOR_BY = By.cssSelector(".titles-list__item__author");
    private final By YEAR_BY = By.cssSelector(".titles-list__item__year");
    private final By SHOW_COPIES_BUTTON_BY = By.cssSelector(".show-copies-btn");
    private final By EDIT_BUTTON_BY = By.cssSelector(".edit-btn");
    private final By REMOVE_BUTTON_BY = By.cssSelector(".remove-btn");

    public TitleRow(WebElement rootElement) {
        super(rootElement);
    }

    public TitleDTO toDTO() {
        return TitleDTO.builder()
                .title(getTitle())
                .author(getAuthor().replace("by ", ""))
                .year(Integer.parseInt(getYear().replaceAll("[()]", "")))
                .build();
    }

    public String getTitle() {
        return rootElement.findElement(TITLE_BY).getText();
    }

    public String getAuthor() {return rootElement.findElement(AUTHOR_BY).getText();}

    public String getYear() {
        return rootElement.findElement(YEAR_BY).getText();
    }

    public void clickShowCopies() {
        WebElement showCopiesButton = waitForElement(SHOW_COPIES_BUTTON_BY);
        clickWhenReady(showCopiesButton);
    }

    public void clickEdit() {
        WebElement editButton = waitForElement(EDIT_BUTTON_BY);
        clickWhenReady(editButton);
    }

    public void clickRemove() {
        WebElement removeButton = waitForElement(REMOVE_BUTTON_BY);
        clickWhenReady(removeButton);
    }
}

