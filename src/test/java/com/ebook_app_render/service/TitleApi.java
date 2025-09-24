package com.ebook_app_render.service;

import com.ebook_app_render.dto.NewTitleDTO;
import com.ebook_app_render.dto.TitleDTO;

import java.util.List;

public interface TitleApi {
    List<NewTitleDTO> getAllTitles(int userId);
    int createTitle(TitleDTO request);
    void deleteTitle(int userId, int titleId);
}

