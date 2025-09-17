package com.ebook_app_render.service;

import com.ebook_app_render.dto.NewRentDTO;
import com.ebook_app_render.dto.RentDTO;
import com.ebook_app_render.tests.api.BaseApiTest;
import io.restassured.common.mapper.TypeRef;
import io.restassured.http.ContentType;
import io.restassured.response.Response;

import java.time.LocalDate;
import java.util.List;

import static io.restassured.RestAssured.given;

public class RentService extends BaseApiTest {

    public List<NewRentDTO> getRentsList() {
        return given()
                .when()
                .get("/rents/?userId=" + getUserId() + "&itemId=" + getFirstItemId())
                .then()
                .statusCode(200)
                .extract()
                .as(new TypeRef<List<NewRentDTO>>() {
                });
    }

    public int addRentAndGetId(RentDTO rentDTO) {
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

    public List<Integer> getAllRentsIds() {
        return given()
                .when()
                .get("/rents/?userId=" + getUserId() + "&itemId=" + getFirstItemId())
                .then()
                .log().all()
                .statusCode(200)
                .extract()
                .body()
                .jsonPath()
                .getList("id", Integer.class);
    }

    public void deleteRent(Integer id) {
        Response response = given()
                .when()
                .log().all()
                .delete("/rents/?userId=" + getUserId() + "&id=" + id)
                .then()
                .log().all().extract().response();

        int statusCode = response.getStatusCode();
        if (statusCode == 200 || statusCode == 204) {
            System.out.println("Deleted rent " + id);
        } else if (statusCode == 404) {
            System.out.println("Rent " + id + " not found.");
        } else {
            System.err.println("Failed to delete rental " + id + ". Status: " + statusCode +
                    " Body: " + response.getBody().asString());
        }
    }

    public void deleteAllRentHistory() {
        List<Integer> ids = getAllRentsIds();
        if (ids.isEmpty()) {
            System.out.println("No rental history");
        } else {
            System.out.println("Deleting " + ids.size() + " rents: " + ids);
            ids.forEach(this::deleteRent);
        }
    }

    public void deleteAllRentHistoryLessFirstOne() {
        List<Integer> ids = getAllRentsIds().stream().skip(1).toList();
        if (ids.isEmpty()) {
            System.out.println("No rental history");
        } else {
            System.out.println("Deleting " + ids.size() + " rents: " + ids);
            ids.forEach(this::deleteRent);
        }
    }
}
