package com.ebook_app_render.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ItemDTO {
        private int userId;
        private int titleId;
        private int id;
        private String purchaseDate;
        private Status status;

    public ItemDTO() {
    }

    public ItemDTO(int userId, int titleId, String purchaseDate) {
        this.userId = userId;
        this.titleId = titleId;
        this.purchaseDate = purchaseDate;
    }
}


