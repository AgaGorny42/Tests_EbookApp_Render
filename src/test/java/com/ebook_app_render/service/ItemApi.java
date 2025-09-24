package com.ebook_app_render.service;

import com.ebook_app_render.dto.ItemDTO;
import com.ebook_app_render.dto.NewItemDTO;

import java.util.List;

public interface ItemApi {
    List<NewItemDTO> getItemsForTitle(int userId, int titleId);
    int createItem(ItemDTO itemDTO);
    void deleteItem(int userId, int itemId);
}
