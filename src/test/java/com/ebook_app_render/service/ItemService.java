package com.ebook_app_render.service;

import com.ebook_app_render.dto.ItemDTO;
import com.ebook_app_render.tests.api.BaseApiTest;
import io.restassured.common.mapper.TypeRef;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import java.util.List;
import static io.restassured.RestAssured.given;

public class ItemService extends BaseApiTest {

    public List<ItemDTO> getItemsList() {
        return given()
                .when()
                .get("/items/?userId="+ getUserId() + "&titleId="+ getFirstTitleId())
                .then()
                .statusCode(200)
                .extract()
                .as(new TypeRef<List<ItemDTO>>() {});
    }

    public int addItemAndGetId(ItemDTO itemDTO) {
        return given()
                .contentType(ContentType.JSON)
                .body(itemDTO)
                .when()
                .post("/items/")
                .then()
                .statusCode(200)
                .extract()
                .as(Integer.class);
    }

    public List<Integer> getAllItemsIds() {
        return given()
                .when()
                .get("/items/?userId="+ getUserId()+ "&titleId="+ getFirstTitleId())
                .then()
                .log().all()
                .statusCode(200)
                .extract()
                .body()
                .jsonPath().getList("id", Integer.class);
    }

    public void deleteItem(Integer id) {
        Response response = given()
                .when()
                .log().all()
                .delete("/items/?userId=" + getUserId() + "&id=" + id)
                .then()
                .log().all().extract().response();

        int statusCode = response.getStatusCode();
        if (statusCode == 200 || statusCode == 204) {
            System.out.println("Deleted item " + id);
        } else if (statusCode == 404) {
            System.out.println("Item " + id + " not found.");
        } else {
            System.err.println("Failed to delete item " + id + ". Status: " + statusCode +
                    " Body: " + response.getBody().asString());
        }
    }

    public void deleteAllItems() {
        List<Integer> ids = getAllItemsIds();
        if (ids.isEmpty()) {
            System.out.println("No item history");
        } else {
            System.out.println("Deleting " + ids.size() + " items: " + ids);
            ids.forEach(this::deleteItem);
        }
    }

    public void deleteAllItemsLessFirst() {
        List<Integer> ids = getAllItemsIds().stream().skip(1).toList();
        if (ids.isEmpty()) {
            System.out.println("No item history");
        } else {
            System.out.println("Deleting " + ids.size() + " items: " + ids);
            ids.forEach(this::deleteItem);
        }
    }
}
