package com.ebook_app_render.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NewItemDTO {

    private int id;
    private String purchaseDate;
    private Status status;
}
