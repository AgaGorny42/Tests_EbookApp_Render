package com.ebook_app_render.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NewTitleDTO {

    private int id;
    private String author;
    private String title;
    private int year;
}
