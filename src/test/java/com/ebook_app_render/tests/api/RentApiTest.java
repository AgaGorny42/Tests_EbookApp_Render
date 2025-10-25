package com.ebook_app_render.tests.api;

import com.ebook_app_render.api.dto.NewRentDTO;
import org.testng.annotations.Test;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

public class RentApiTest extends BaseApiTest {

    @Test
    public void shouldRentOneItemAndVerifyId() {
        titleId = titleApi.createTitle(getTitleDTO());
        itemId = itemApi.createItem(getItemDTO());
        rentId = rentApi.createRent(getRentDTO());

        List<NewRentDTO> rents = rentApi.getRentsForItem(userId, itemId);
        int lastRent = rents.get(rents.size() - 1).getId();

        assertThat(lastRent, is(equalTo(rentId)));
    }

    @Test
    public void shouldMakeTwoRentalAndCompareIdsTest() {
        titleId = titleApi.createTitle(getTitleDTO());
        itemId = itemApi.createItem(getItemDTO());
        int firstRentId = rentApi.createRent(getRentDTO());
        int secondRentId = rentApi.createRent(getRentDTO());

        assertThat(firstRentId, not(equalTo(secondRentId)));
    }

    @Test
    public void shouldGetEmptyRentList() {
        deletionService.deleteTitlesWithDependencies(userId);

        List<NewRentDTO> rents = rentApi.getRentsForItem(userId, itemId);

        assertThat(rents, is(empty()));
    }

    @Test
    public void shouldValidateIdAndCustomerName() {
        titleId = titleApi.createTitle(getTitleDTO());
        itemId = itemApi.createItem(getItemDTO());
        rentId = rentApi.createRent(getRentDTO());

        List<NewRentDTO> rentalList = rentApi.getRentsForItem(userId, itemId);
        NewRentDTO lastRent = rentalList.get(rentalList.size() - 1);

        String actualCustomerName = lastRent.getCustomerName();
        String expectedCustomerName = "Default Customer";

        assertThat(rentId, is(notNullValue()));
        assertThat(actualCustomerName, is(equalTo(expectedCustomerName)));
    }

    @Test
    public void rentalDayShouldBeSetUpForActualDay() {
        titleId = titleApi.createTitle(getTitleDTO());
        itemId = itemApi.createItem(getItemDTO());
        rentId = rentApi.createRent(getRentDTO());

        List<NewRentDTO> rentalList = rentApi.getRentsForItem(userId, itemId);
        String actualRentDate = rentalList.get(rentalList.size() - 1).getRentDate();
        String expectedRentDate = LocalDate.now().format(DateTimeFormatter.ISO_LOCAL_DATE);

        assertThat(expectedRentDate, is(equalTo(actualRentDate)));
    }

    @Test
    public void theRentalTimeIsSetUpForThreeDays() {
        titleId = titleApi.createTitle(getTitleDTO());
        itemId = itemApi.createItem(getItemDTO());
        rentId = rentApi.createRent(getRentDTO());

        List<NewRentDTO> rentalList = rentApi.getRentsForItem(userId, itemId);
        LocalDate actualRentDate = LocalDate.parse(rentalList.get(rentalList.size() - 1).getRentDate());
        LocalDate expectedExpirationDate = LocalDate.parse(rentalList.get(rentalList.size() - 1).getExpirationDate());

        assertThat(expectedExpirationDate, is(actualRentDate.plusDays(3)));
    }

    @Test
    public void shouldChangeRentExpirationDateForFutureDateTest() {
        titleId = titleApi.createTitle(getTitleDTO());
        itemId = itemApi.createItem(getItemDTO());
        rentId = rentApi.createRent(getRentDTO());
        NewRentDTO rent = rentApi.getRentsForItem(userId, itemId).get(0);

        rent.setRentDate(LocalDate.now().toString());

        rentService.extendExpirationBy30Days(rent);

        LocalDate rentDate = LocalDate.parse(rent.getRentDate());
        LocalDate expectedExpirationDate = rentDate.plusDays(30);
        LocalDate actualExpirationDate = LocalDate.parse(rent.getExpirationDate());

        assertThat(actualExpirationDate, is(expectedExpirationDate));
    }

    @Test
    public void shouldBePossibleToDeleteAllRentHistory() {
        titleId = titleApi.createTitle(getTitleDTO());
        itemId = itemApi.createItem(getItemDTO());
        rentId = rentApi.createRent(getRentDTO());
        List<NewRentDTO> rentsBefore = rentApi.getRentsForItem(userId, itemId);
        assertThat(rentsBefore, is(not(empty())));

        rentsBefore.forEach(rent -> rentApi.deleteRent(userId, rent.getId()));

        List<NewRentDTO> rentsAfter = rentApi.getRentsForItem(userId, itemId);
        assertThat(rentsAfter, is(empty()));
    }
}
