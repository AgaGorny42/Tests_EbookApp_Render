package com.ebook_app_render.dto;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class LoginDTO {
    private String login;
    private String password;

    public LoginDTO(String login, String password) {
        this.login = login;
        this.password = password;
    }
}
