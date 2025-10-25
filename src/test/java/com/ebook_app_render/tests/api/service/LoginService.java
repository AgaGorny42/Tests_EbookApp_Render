package com.ebook_app_render.tests.api.service;

import com.ebook_app_render.api.dto.LoginDTO;
import com.ebook_app_render.api.dto.RegisterDTO;
import com.ebook_app_render.tests.api.BaseApiTest;
import io.restassured.http.ContentType;
import static io.restassured.RestAssured.given;

public class LoginService extends BaseApiTest {

    public int loginAndGetUserId(LoginDTO loginDTO) {
        return given()
                .contentType(ContentType.JSON)
                .body(loginDTO)
                .when()
                .log().all()
                .post("/user/login")
                .then()
                .log().all()
                .statusCode(200)
                .extract()
                .as(Integer.class);
    }

    public RegisterDTO registerRegisteredUser() {
        LoginDTO login = new LoginDTO("test1", "test1");

        return given().contentType(ContentType.JSON)
                .and().body(login)
                .when()
                .post("/user/register")
                .then().statusCode(200)
                .extract()
                .as(RegisterDTO.class);
    }
}
