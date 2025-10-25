package com.ebook_app_render.tests.api.service;

import com.ebook_app_render.api.dto.NewRentDTO;
import com.ebook_app_render.api.dto.RentDTO;
import com.ebook_app_render.api.service.RentApi;
import io.restassured.common.mapper.TypeRef;
import io.restassured.http.ContentType;

import java.time.LocalDate;
import java.util.List;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.is;

public class RentService implements RentApi {

    @Override
    public List<NewRentDTO> getRentsForItem(int userId, int itemId) {
        return given()
                .when()
                .get("/rents/?userId=" + userId + "&itemId=" + itemId)
                .then()
                .statusCode(200)
                .extract()
                .as(new TypeRef<List<NewRentDTO>>() {
                });
    }

    @Override
    public void deleteRent(int userId, int rentId) {
        given()
                .when()
                .delete("/rents/?userId=" + userId + "&id=" + rentId)
                .then()
                .statusCode(is(200));
    }

    @Override
    public int createRent(RentDTO rentDTO) {
        return given()
                .contentType(ContentType.JSON)
                .body(rentDTO)
                .when()
                .log().all()
                .post("/rents/")
                .then()
                .statusCode(200)
                .extract()
                .as(Integer.class);
    }

    public void extendExpirationBy30Days(NewRentDTO newRentDTO) {
        if (newRentDTO.getRentDate() == null) {
            throw new IllegalStateException("rental date cannot be null");
        }
        LocalDate rentDate = LocalDate.parse(newRentDTO.getRentDate());
        LocalDate newExpirationDate = rentDate.plusDays(30);

        newRentDTO.setExpirationDate(newExpirationDate.toString());
    }
}
