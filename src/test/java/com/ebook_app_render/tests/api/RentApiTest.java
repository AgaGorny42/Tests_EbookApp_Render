package com.ebook_app_render.tests.api;

import com.ebook_app_render.dto.NewRentDTO;
import com.ebook_app_render.dto.RentDTO;
import com.ebook_app_render.service.RentService;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

public class RentApiTest extends BaseApiTest{

    RentService rentService = new RentService();
    String rentDate = LocalDate.now().format(DateTimeFormatter.ISO_LOCAL_DATE);
    RentDTO rentDTO = new RentDTO(getUserId(), getFirstItemId(), rentDate, "Jan Kowalski");

    @Test
    void shouldRentOneItemAndVerifyId(){
        int rentId = rentService.addRentAndGetId(rentDTO);
        List<NewRentDTO> rentalList = rentService.getRentsList();
        int lastRentId = rentalList.getLast().getId();

        assertThat(rentId, is(equalTo(lastRentId)));
    }

    @Test
    void shouldMakeTwoRentalAndCompareIds(){
        int firstRentId = rentService.addRentAndGetId(rentDTO);
        int secondRentId = rentService.addRentAndGetId(rentDTO);

        assertThat(firstRentId, not(equalTo(secondRentId)));
    }

    @Test
    void shouldValidateIdAndCustomerName(){
        rentService.addRentAndGetId(rentDTO);
        List<NewRentDTO> rentalList = rentService.getRentsList();

        String actualCustomerName = rentalList.getLast().getCustomerName();
        String expectedCustomerName = "Jan Kowalski";

        assertThat(getFirstRentId(), is(notNullValue()));
        assertThat(expectedCustomerName, is(equalTo(actualCustomerName)));
    }

    @Test
    void rentalDayShouldBeSetUpForActualDay(){
        List<NewRentDTO> rentalList = rentService.getRentsList();

        String actualRentDate = rentalList.getLast().getRentDate();
        String expectedRentDate = LocalDate.now().format(DateTimeFormatter.ISO_LOCAL_DATE);

        assertThat(expectedRentDate, is(equalTo(actualRentDate)));
    }

    @Test
    void theRentalTimeIsSetUpForTreeDays(){
        rentService.addRentAndGetId(rentDTO);
        List<NewRentDTO> rentalList = rentService.getRentsList();

        LocalDate actualRentDate = LocalDate.parse(rentalList.getLast().getRentDate());
        String actualExpirationDate = rentalList.getLast().getExpirationDate();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate expectedExpirationDate = LocalDate.parse(actualExpirationDate, formatter);

        assertThat(expectedExpirationDate, is(actualRentDate.plusDays(3)));
    }

    @Test //no specification whether it should be possible to change rental date
          // and what is maximum rental time
    void shouldChangeRentExpirationDateForFutureDateTest() {
        NewRentDTO newRentDTO = new NewRentDTO();

        newRentDTO.setRentDate(rentDate); //is set up for actual day
        rentService.extendExpirationBy30Days(newRentDTO);

        LocalDate rentDate = LocalDate.parse(newRentDTO.getRentDate());
        LocalDate expectedExpirationDate = LocalDate.parse(newRentDTO.getExpirationDate());

        assertThat(expectedExpirationDate, is(rentDate.plusDays(30)));
    }

    @Test
    void shouldBePossibleToDeleteAllRentHistory(){

    }

}

