package com.ebook_app_render.ui.pages;

import com.ebook_app_render.api.dto.NewRentDTO;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

public class RentRow extends BaseComponentPage{

    private final By RENT_DATE_ROW_BY = By.xpath("//div[@class='rents-list__rent__rent-date list__item__col' and not(contains(text(), 'expiration'))]");
    private final By EXPIRATION_DATE_ROW_BY = By.xpath("//div[@class='rents-list__rent__rent-date list__item__col' and contains(text(), 'expiration')]\n");
    private final By CUSTOMER_NAME_ROW_BY = By.cssSelector(".rents-list__rent__customer-name.list__item__col.list__item__col--primary");
    private final By EDIT_BUTTON_BY = By.cssSelector(".edit-btn.btn--small.btn.btn--warning");
    private final By REMOVE_BUTTON_BY = By.cssSelector(".remove-btn.btn--small.btn.btn--error");

    public RentRow(WebElement rootElement) {
        super(rootElement);
    }

    public NewRentDTO toDTO() {
        return NewRentDTO.builder()
                .customerName(normalizeName(getCustomerName()))
                .rentDate(formatDate(getRentDate()))
                .expirationDate(extractDate(getExpirationDate()))
                .build();
    }

    private String normalizeName(String name) {
        if (name == null) return null;
        return name.trim().toLowerCase();
    }

    private String formatDate(Object date) {
        if (date == null) return null;
        if (date instanceof java.time.LocalDate) {
            return ((java.time.LocalDate) date).format(java.time.format.DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        }
        return date.toString().trim();
    }

    private String extractDate(String raw) {
        if (raw == null) return null;
        java.util.regex.Matcher m = java.util.regex.Pattern.compile("\\d{4}-\\d{2}-\\d{2}").matcher(raw);
        if (m.find()) {
            return m.group();
        }
        return raw;
    }

    public String getRentDate() {
        return waitForElement(RENT_DATE_ROW_BY).getText();
    }

    public String getExpirationDate() {
        return waitForElement(EXPIRATION_DATE_ROW_BY).getText();
    }

    public String getCustomerName() {
        return waitForElement(CUSTOMER_NAME_ROW_BY).getText();
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
