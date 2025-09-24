package com.ebook_app_render.service;

import com.ebook_app_render.dto.NewRentDTO;
import com.ebook_app_render.dto.RentDTO;

import java.util.List;

public interface RentApi {
    List<NewRentDTO> getRentsForItem(int userId, int itemId);

    int createRent(RentDTO rentDTO);

    void deleteRent(int userId, int rentId);
}

