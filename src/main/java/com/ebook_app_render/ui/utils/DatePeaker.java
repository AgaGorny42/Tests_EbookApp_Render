package com.ebook_app_render.ui.utils;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.Duration;
import java.time.LocalDate;
import java.time.Month;
import java.util.List;
import java.util.Locale;

public class DatePeaker {

    private final By CALENDAR_BY = By.cssSelector(".vdp-datepicker__calendar");
    private final By HEADER_MONTH_YEAR_BY = By.cssSelector("header .day__month_btn.up");
    private final By NEXT_BUTTON_BY = By.cssSelector("header .next");
    private final By PREV_BUTTON_BY = By.cssSelector("header .prev");
    private final By DAY_CELL_BY = By.cssSelector(".cell.day:not(.blank):not(.disabled)");

    protected WebDriver driver;
    protected WebDriverWait wait;

    public DatePeaker() {
        this.driver = DriverSingleton.getDriver();
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        PageFactory.initElements(driver, this);
    }

    public void setDate(String dateString) {
        LocalDate date = LocalDate.parse(dateString);
        setDate(date);
    }

    public void setDate(LocalDate date) {
        WebElement calendar = wait.until(d -> d.findElements(CALENDAR_BY).stream()
                .filter(WebElement::isDisplayed).findFirst()
                .orElse(null));

        int targetYear = date.getYear();

        while (true) {
            WebElement header = calendar.findElement(HEADER_MONTH_YEAR_BY);
            String[] parts = header.getText().trim().split(" ");
            Month headerMonth = parseMonth(parts[0]);
            int headerYear = Integer.parseInt(parts[1]);

            if (headerMonth == date.getMonth() && headerYear == targetYear) break;

            if (headerYear < targetYear || (headerYear == targetYear && headerMonth.compareTo(date.getMonth()) < 0)) {
                calendar.findElement(NEXT_BUTTON_BY).click();
            } else {
                calendar.findElement(PREV_BUTTON_BY).click();
            }

            wait.until(_ -> calendar.findElements(DAY_CELL_BY).size() >= 28);
        }
        int targetDay = date.getDayOfMonth();
        List<WebElement> days = calendar.findElements(DAY_CELL_BY);
        List<WebElement> validDays = days.stream() // pick the cells with numbers of the days only
                .filter(WebElement::isDisplayed)
                .filter(d -> d.getText().trim().matches("\\d+"))
                .toList();

        for (WebElement d : validDays) {
            if (d.getText().trim().equals(String.valueOf(targetDay))) {
                d.click();
                break;
            }
        }
    }

    private Month parseMonth(String monthAbbreviation) {
        return switch (monthAbbreviation.trim().substring(0, 3).toLowerCase(Locale.ENGLISH)) {
            case "jan" -> Month.JANUARY;
            case "feb" -> Month.FEBRUARY;
            case "mar" -> Month.MARCH;
            case "apr" -> Month.APRIL;
            case "may" -> Month.MAY;
            case "jun" -> Month.JUNE;
            case "jul" -> Month.JULY;
            case "aug" -> Month.AUGUST;
            case "sep" -> Month.SEPTEMBER;
            case "oct" -> Month.OCTOBER;
            case "nov" -> Month.NOVEMBER;
            case "dec" -> Month.DECEMBER;
            default -> throw new IllegalArgumentException("Unknown month: " + monthAbbreviation);
        };
    }
}
