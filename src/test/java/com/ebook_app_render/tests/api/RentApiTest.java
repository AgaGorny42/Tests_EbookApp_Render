package com.ebook_app_render.tests.api;

import com.ebook_app_render.dto.NewRentDTO;
import org.junit.jupiter.api.*;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class RentApiTest extends BaseApiTest {

    @Test
    void shouldRentOneItemAndVerifyId() {
        titleId = titleApi.createTitle(getTitleDTO());
        itemId = itemApi.createItem(getItemDTO());
        rentId = rentApi.createRent(getRentDTO());

        List<NewRentDTO> rents = rentApi.getRentsForItem(userId, itemId);
        int lastRent = rents.getLast().getId();

        assertThat(lastRent, is(equalTo(rentId)));
    }

    @Test
    void shouldMakeTwoRentalAndCompareIdsTest() {
        titleId = titleApi.createTitle(getTitleDTO());
        itemId = itemApi.createItem(getItemDTO());
        int firstRentId = rentApi.createRent(getRentDTO());
        int secondRentId = rentApi.createRent(getRentDTO());

        assertThat(firstRentId, not(equalTo(secondRentId)));
    }

    @Test
    void shouldGetEmptyRentList() {
        deletionService.deleteTitlesWithDependencies(userId);

        List<NewRentDTO> rents = rentApi.getRentsForItem(userId, itemId);

        assertThat(rents, is(empty()));
    }

    @Test
    void shouldValidateIdAndCustomerName() {
        titleId = titleApi.createTitle(getTitleDTO());
        itemId = itemApi.createItem(getItemDTO());
        rentId = rentApi.createRent(getRentDTO());

        List<NewRentDTO> rentalList = rentApi.getRentsForItem(userId, itemId);

        NewRentDTO lastRent = rentalList.getLast();

        String actualCustomerName = lastRent.getCustomerName();
        String expectedCustomerName = "Default Customer";

        assertThat(rentId, is(notNullValue()));
        assertThat(actualCustomerName, is(equalTo(expectedCustomerName)));
    }

    @Test
    void rentalDayShouldBeSetUpForActualDay() {
        titleId = titleApi.createTitle(getTitleDTO());
        itemId = itemApi.createItem(getItemDTO());
        rentId = rentApi.createRent(getRentDTO());

        List<NewRentDTO> rentalList = rentApi.getRentsForItem(userId, itemId);
        String actualRentDate = rentalList.getLast().getRentDate();
        String expectedRentDate = LocalDate.now().format(DateTimeFormatter.ISO_LOCAL_DATE);

        assertThat(expectedRentDate, is(equalTo(actualRentDate)));
    }

    @Test
    void theRentalTimeIsSetUpForThreeDays() {
        titleId = titleApi.createTitle(getTitleDTO());
        itemId = itemApi.createItem(getItemDTO());
        rentId = rentApi.createRent(getRentDTO());

        List<NewRentDTO> rentalList = rentApi.getRentsForItem(userId, itemId);
        LocalDate actualRentDate = LocalDate.parse(rentalList.getLast().getRentDate());
        LocalDate expectedExpirationDate = LocalDate.parse(rentalList.getLast().getExpirationDate());

        assertThat(expectedExpirationDate, is(actualRentDate.plusDays(3)));
    }

    @Test
    void shouldChangeRentExpirationDateForFutureDateTest() {
        titleId = titleApi.createTitle(getTitleDTO());
        itemId = itemApi.createItem(getItemDTO());
        rentId = rentApi.createRent(getRentDTO());
        NewRentDTO rent = rentApi.getRentsForItem(userId, itemId).getFirst();

        rent.setRentDate(LocalDate.now().toString());

        rentService.extendExpirationBy30Days(rent);

        LocalDate rentDate = LocalDate.parse(rent.getRentDate());
        LocalDate expectedExpirationDate = rentDate.plusDays(30);
        LocalDate actualExpirationDate = LocalDate.parse(rent.getExpirationDate());

        assertThat(actualExpirationDate, is(expectedExpirationDate));
    }

    @Test
    void shouldBePossibleToDeleteAllRentHistory() {
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

