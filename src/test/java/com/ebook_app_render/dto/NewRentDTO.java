package com.ebook_app_render.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class NewRentDTO {

    private int id;
    private String customerName;
    private String rentDate;
    private String expirationDate;

    public NewRentDTO() {
    }

    public NewRentDTO(int id, String rentDate, String expirationDate, String customerName) {
        this.id = id;
        this.rentDate = rentDate;
        this.expirationDate = expirationDate;
        this.customerName = customerName;
    }
}
