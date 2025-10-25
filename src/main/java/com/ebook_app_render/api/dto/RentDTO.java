package com.ebook_app_render.api.dto;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RentDTO {

    private int userId;
    private int itemId;
    public String rentDate;
    private String customerName;
}
