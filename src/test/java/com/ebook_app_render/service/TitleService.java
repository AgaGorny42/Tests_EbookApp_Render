package com.ebook_app_render.service;

import com.ebook_app_render.dto.NewTitleDTO;
import com.ebook_app_render.dto.TitleDTO;
import io.restassured.common.mapper.TypeRef;
import io.restassured.http.ContentType;
import java.util.List;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.is;

public class TitleService implements TitleApi{

    @Override
    public List<NewTitleDTO> getAllTitles(int userId) {
        return given()
                .when()
                .get("/titles/?userId=" + userId)
                .then()
                .statusCode(200)
                .extract()
                .as(new TypeRef<List<NewTitleDTO>>() {});
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
    public void deleteAllTitles(int userId) {
        List<NewTitleDTO> titles = getAllTitles(userId);
        for (NewTitleDTO title : titles) {
            deleteTitle(userId, title.getId());
        }
    }

    // 🔹 Usuwa wszystkie tytuły oprócz pierwszego
    public void deleteAllTitlesExceptFirst(int userId) {
        List<NewTitleDTO> titles = getAllTitles(userId);
        if (titles.size() <= 1) {
            return; // nic nie usuwać
        }

        // zaczynamy od drugiego
        for (int i = 1; i < titles.size(); i++) {
            deleteTitle(userId, titles.get(i).getId());
        }
    }
}

