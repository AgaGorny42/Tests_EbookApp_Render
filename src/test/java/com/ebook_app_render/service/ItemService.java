package com.ebook_app_render.service;

import com.ebook_app_render.dto.ItemDTO;
import com.ebook_app_render.dto.NewItemDTO;
import io.restassured.common.mapper.TypeRef;
import io.restassured.http.ContentType;

import java.util.List;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.is;

public class ItemService implements ItemApi {

    @Override
    public List<NewItemDTO> getItemsForTitle(int userId, int titleId) {
        return given()
                .when()
                .get("/items/?userId=" + userId + "&titleId=" + titleId)
                .then()
                .statusCode(200)
                .extract()
                .as(new TypeRef<List<NewItemDTO>>() {
                });
    }

    @Override
    public int createItem(ItemDTO itemDTO) {
        return given()
                .contentType(ContentType.JSON)
                .body(itemDTO)
                .log().all()
                .when()
                .post("/items/")
                .then()
                .log().all()
                .statusCode(200)
                .extract()
                .as(Integer.class);
    }

    @Override
    public void deleteItem(int userId, int itemId) {
        given()
                .when()
                .delete("/items/?userId=" + userId + "&id=" + itemId)
                .then()
                .statusCode(is(200));
    }
}
