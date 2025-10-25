package com.ebook_app_render.tests.api;

import com.ebook_app_render.api.dto.NewTitleDTO;
import com.ebook_app_render.api.dto.TitleDTO;
import org.testng.annotations.Test;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

public class TitleApiTest extends BaseApiTest {

    @Test
    public void shouldAddTitleAndReturnId() {
        int createdTitleId = titleApi.createTitle(getTitleDTO());

        assertThat(createdTitleId, is(notNullValue()));
    }

    @Test
    public void shouldAddTwoTitlesAndCompareIds() {
        TitleDTO newTitle2 = new TitleDTO(userId, "title2", "author2", 2026);

        int firstTitleId = titleApi.createTitle(getTitleDTO());
        int secondTitleId = titleApi.createTitle(newTitle2);

        assertThat(firstTitleId, not(equalTo(secondTitleId)));
    }

    @Test
    public void shouldGetEmptyTitleList() {
        deletionService.deleteTitlesWithDependencies(userId);

        List<NewTitleDTO> titles = titleApi.getAllTitles(userId);

        assertThat(titles, is(empty()));
    }

    @Test
    public void shouldValidateTitleResponseBody() {
        titleApi.createTitle(getTitleDTO());
        List<NewTitleDTO> titles = titleApi.getAllTitles(userId);

        assertThat(titles, is(notNullValue()));
        assertThat(titles.size(), greaterThan(0));

        NewTitleDTO firstTitle = titles.get(0);

        assertThat(firstTitle.getId(), is(notNullValue()));
        assertThat(firstTitle.getAuthor(), is(equalTo("Default Author")));
        assertThat(firstTitle.getTitle(), is("Default Title"));
        assertThat(firstTitle.getYear(), is(2025));
    }

    @Test
    public void shouldBePossibleToDeleteTitleWithNoItemsAdded() {
        int titleId = titleApi.createTitle(getTitleDTO());
        List<NewTitleDTO> beforeList = titleApi.getAllTitles(userId);

        titleApi.deleteTitle(userId, titleId);

        List<NewTitleDTO> afterList = titleApi.getAllTitles(userId);
        List<Integer> afterListIds = afterList.stream().map(NewTitleDTO::getId).toList();

        assertThat(afterList.size(), is(beforeList.size() - 1));
        assertThat(afterListIds, not(hasItem(titleId)));
    }

    @Test
    public void shouldDeleteAllTitlesWithDependencies() {
        titleId = titleApi.createTitle(getTitleDTO());
        itemId = itemApi.createItem(getItemDTO());
        rentId = rentApi.createRent(getRentDTO());
        List<NewTitleDTO> titlesBefore = titleApi.getAllTitles(userId);
        System.out.println("Titles before deletion: " + titlesBefore.size());

        deletionService.deleteTitlesWithDependencies(userId);

        List<NewTitleDTO> titlesAfter = titleApi.getAllTitles(userId);
        System.out.println("Titles after deletion: " + titlesAfter.size());
        assertThat(titlesAfter, is(empty()));
    }
}
