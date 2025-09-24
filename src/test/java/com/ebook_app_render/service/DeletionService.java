package com.ebook_app_render.service;

import com.ebook_app_render.dto.NewItemDTO;
import com.ebook_app_render.dto.NewRentDTO;
import com.ebook_app_render.dto.NewTitleDTO;

import java.util.List;

public class DeletionService {

    private final TitleApi titleApi;
    private final ItemApi itemApi;
    private final RentApi rentApi;

    public DeletionService(TitleApi titleApi, ItemApi itemApi, RentApi rentApi) {
        this.titleApi = titleApi;
        this.itemApi = itemApi;
        this.rentApi = rentApi;
    }

    public void deleteAllTitlesWithDependencies(int userId) {
        List<NewTitleDTO> titles = titleApi.getAllTitles(userId);
        for (NewTitleDTO title : titles) {
            List<NewItemDTO> items = itemApi.getItemsForTitle(userId, title.getId());
            for (NewItemDTO item : items) {
                List<NewRentDTO> rents = rentApi.getRentsForItem(userId, item.getId());
                for (NewRentDTO rent : rents) {
                    rentApi.deleteRent(userId, rent.getId());
                }
                itemApi.deleteItem(userId, item.getId());
            }
            titleApi.deleteTitle(userId, title.getId());
        }
    }

    public void deleteTitlesWithDependencies(int userId) {
        List<NewTitleDTO> titles = titleApi.getAllTitles(userId);
        if (titles.isEmpty()) {
            return;
        }
        for (NewTitleDTO title : titles) {
            deleteItemsForTitle(userId, title.getId());
            titleApi.deleteTitle(userId, title.getId());
        }
    }

    public void deleteItemsForTitle(int userId, int titleId) {
        List<NewItemDTO> items = itemApi.getItemsForTitle(userId, titleId);
        if (items.isEmpty()) {
            return;
        }
        for (NewItemDTO item : items) {
            deleteRentsForItem(userId, item.getId());
            itemApi.deleteItem(userId, item.getId());
        }
    }

    public void deleteRentsForItem(int userId, int itemId) {
        List<NewRentDTO> rents = rentApi.getRentsForItem(userId, itemId);
        if (rents.isEmpty()) {
            return;
        }
        for (NewRentDTO rent : rents) {
            rentApi.deleteRent(userId, rent.getId());
        }
    }
}

