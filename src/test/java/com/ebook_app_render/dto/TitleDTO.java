package com.ebook_app_render.dto;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TitleDTO {

    private int userId;
    private String author;
    private String title;
    private int year;
}

