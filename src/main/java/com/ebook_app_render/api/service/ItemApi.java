package com.ebook_app_render.api.service;

import com.ebook_app_render.api.dto.ItemDTO;
import com.ebook_app_render.api.dto.NewItemDTO;

import java.util.List;

public interface ItemApi {
    List<NewItemDTO> getItemsForTitle(int userId, int titleId);
    int createItem(ItemDTO itemDTO);
    void deleteItem(int userId, int itemId);
}
