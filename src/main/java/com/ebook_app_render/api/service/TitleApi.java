package com.ebook_app_render.api.service;

import com.ebook_app_render.api.dto.NewTitleDTO;
import com.ebook_app_render.api.dto.TitleDTO;

import java.util.List;

public interface TitleApi {
    List<NewTitleDTO> getAllTitles(int userId);
    int createTitle(TitleDTO request);
    void deleteTitle(int userId, int titleId);
}

