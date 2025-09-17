package com.ebook_app_render.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TitleDTO {

    private int userId;
    private int id;
    private String author;
    private String title;
    private int year;

    public TitleDTO() {}

    public TitleDTO(int userId, String title, String author, int year) {
        this.userId = userId;
        this.title = title;
        this.author = author;
        this.year = year;
    }
}

