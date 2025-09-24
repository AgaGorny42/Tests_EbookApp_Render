package com.ebook_app_render.tests.api;

import com.ebook_app_render.dto.RegisterDTO;
import com.ebook_app_render.service.LoginService;
import org.hamcrest.Matchers;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class LoginApiTest extends BaseApiTest{

    @Test
    void shouldLoginAndReturnUserId() {
        assertThat(userId, Matchers.is(notNullValue()));
        assertThat(userId, is(1145));
    }

    @Test
    void shouldNotBePossibleToRegisterRegisteredUserTest(){
        RegisterDTO response = new LoginService().registerRegisteredUser();

        assertThat(response.isNew(), is(false));
        Assertions.assertEquals(708, response.getId());
    }
}
