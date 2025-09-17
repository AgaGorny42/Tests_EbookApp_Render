package com.ebook_app_render.service;

import com.ebook_app_render.dto.TitleDTO;
import com.ebook_app_render.tests.api.BaseApiTest;
import io.restassured.common.mapper.TypeRef;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import java.util.List;
import static io.restassured.RestAssured.given;

public class TitleService extends BaseApiTest {

    public List<TitleDTO> getTitleList() {
        return given()
                .when()
                .get("/titles/?userId=" + getUserId())
                .then().statusCode(200)
                .extract()
                .as(new TypeRef<List<TitleDTO>>() {});
    }

    public int addTitleAndGetId(TitleDTO titleDTO) {
        return given()
                .contentType(ContentType.JSON)
                .body(titleDTO)
                .when()
                .post("/titles/")
                .then()
                .statusCode(200)
                .extract()
                .as(Integer.class);
    }

    public List<Integer> getAllTitleIds() {
        return given()
                .when()
                .get("/titles/?userId=" + getUserId())
                .then()
                .log().all()
                .statusCode(200)
                .extract()
                .jsonPath().getList("id", Integer.class);
    }

    public void deleteTitle(Integer id) {
        Response response = given()
                .when()
                .log().all()
                .delete("/titles/?userId=" + getUserId() + "&id=" + id)
                .then()
                .log().all().extract().response();

        int statusCode = response.getStatusCode();
        if (statusCode == 200 || statusCode == 204) {
            System.out.println("Deleted title " + id);
        } else if (statusCode == 404) {
            System.out.println("Title " + id + " not found.");
        } else {
            System.err.println("Failed to delete title " + id + ". Status: " + statusCode +
                    " Body: " + response.getBody().asString());
        }
    }

    public void deleteAllTitles() {
        List<Integer> ids = getAllTitleIds();
        if (ids.isEmpty()) {
            System.out.println("No title history");
        } else {
            ids.forEach(this::deleteTitle);
            System.out.println("Deleting " + ids.size() + " rents: " + ids);
        }
    }

    public void deleteAllTitlesLessFirstOne() {
        List<Integer> ids = getAllTitleIds().stream().skip(1).toList();
        if (ids.isEmpty()) {
            System.out.println("No title history");
        } else {
            ids.forEach(this::deleteTitle);
            System.out.println("Deleting " + ids.size() + " rents: " + ids);
        }
    }
}

