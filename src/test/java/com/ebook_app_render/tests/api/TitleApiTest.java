package com.ebook_app_render.tests.api;

import com.ebook_app_render.dto.TitleDTO;
import com.ebook_app_render.service.TitleService;
import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

public class TitleApiTest extends BaseApiTest {

    TitleDTO newTitle = new TitleDTO(getUserId(), "test11", "test11", 2011);
    TitleService titleService = new TitleService();

    @Test
    void shouldGetTitleListWithAtLeastOneTitle(){
        titleService.deleteAllTitlesLessFirstOne();
        assertTrue((titleService.getTitleList().size()) <= 1);
    }

    @Test
    void shouldAddTitleAndReturnId() {
        int titleId = new TitleService().addTitleAndGetId(newTitle);

        assertThat(titleId, is(notNullValue()));
    }

    @Test
    void shouldAddTwoTitlesAndCompareIds() {
        int firstTitleId = new TitleService().addTitleAndGetId(newTitle);
        int secondTitleId = new TitleService().addTitleAndGetId(newTitle);

        assertThat(firstTitleId, not(equalTo(secondTitleId)));
    }

    @Test
    void shouldValidateTitleResponseBody() {
        List<TitleDTO> titles = titleService.getTitleList();

        assertThat(titles, is(notNullValue()));

        assertThat(titles.getFirst().getId(), is(notNullValue()));
        assertThat(titles.getFirst().getAuthor(), is(equalTo("test11")));
        assertThat(titles.getFirst().getTitle(), is(notNullValue()));
        assertThat(titles.getFirst().getYear(), is(notNullValue()));
    }

    @Test
    void shouldBePossibleToDeleteTitleWithNoItemsAdded(){
        TitleDTO titleDTO = new TitleDTO(getUserId(),"title", "author", 2025);
        int titleId = titleService.addTitleAndGetId(titleDTO);
        List<TitleDTO> beforeList = titleService.getTitleList();

        titleService.deleteTitle(titleId);
        List<TitleDTO> afterList = titleService.getTitleList();
        List<Integer>afterListIds = afterList.stream().map(TitleDTO::getId).toList();

        assertThat(afterList.size(), is(beforeList.size() -1));
        assertThat(afterListIds, not(hasItem(titleId)));
    }
}
