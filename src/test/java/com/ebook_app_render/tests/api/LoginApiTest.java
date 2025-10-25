package com.ebook_app_render.tests.api;

import com.ebook_app_render.api.dto.RegisterDTO;
import com.ebook_app_render.tests.api.service.LoginService;
import org.hamcrest.Matchers;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

import org.testng.Assert;
import org.testng.annotations.Test;

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
        Assert.assertEquals(response.getId(), 708);
    }
}
