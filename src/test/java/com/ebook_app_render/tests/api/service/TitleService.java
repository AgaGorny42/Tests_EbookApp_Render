package com.ebook_app_render.tests.api.service;

import com.ebook_app_render.api.dto.NewTitleDTO;
import com.ebook_app_render.api.dto.TitleDTO;
import com.ebook_app_render.api.service.TitleApi;
import io.restassured.common.mapper.TypeRef;
import io.restassured.http.ContentType;

import java.util.List;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.is;

public class TitleService implements TitleApi {

    @Override
    public List<NewTitleDTO> getAllTitles(int userId) {
        return given()
                .when()
                .get("/titles/?userId=" + userId)
                .then()
                .statusCode(200)
                .extract()
                .as(new TypeRef<List<NewTitleDTO>>() {
                });
    }

    @Override
    public int createTitle(TitleDTO request) {
        return given()
                .contentType(ContentType.JSON)
                .body(request)
                .when()
                .post("/titles/")
                .then()
                .statusCode(200)
                .extract()
                .as(Integer.class);
    }

    @Override
    public void deleteTitle(int userId, int titleId) {
        given()
                .when()
                .delete("/titles/?userId=" + userId + "&id=" + titleId)
                .then()
                .statusCode(is(200));
    }
}

