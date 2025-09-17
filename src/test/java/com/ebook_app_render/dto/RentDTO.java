package com.ebook_app_render.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Getter
@Setter
public class RentDTO {

    private int userId;
    private int itemId;
    public String rentDate;
    private String customerName;

    public RentDTO() {
    }

    public RentDTO(int userId, int itemId, String rentDate, String customerName) {
        this.userId = userId;
        this.itemId = itemId;
        this.rentDate = rentDate;
        this.customerName = customerName;
    }
}
