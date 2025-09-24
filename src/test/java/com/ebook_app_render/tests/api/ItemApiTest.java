package com.ebook_app_render.tests.api;

import com.ebook_app_render.dto.*;
import org.junit.jupiter.api.Test;
import java.util.List;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

public class ItemApiTest extends BaseApiTest {

    @Test
    void shouldAddOneItemAndVerifyId() {
        titleId = titleApi.createTitle(getTitleDTO());
        itemId = itemApi.createItem(getItemDTO());

        assertThat(itemApi, is(notNullValue()));
    }

    @Test
    void shouldAddTwoItemsAndCompareIds() {
        titleId = titleApi.createTitle(getTitleDTO());
        int firstItemId = itemApi.createItem(getItemDTO());
        int secondItemId = itemApi.createItem(getItemDTO());

        assertThat(firstItemId, not(equalTo(secondItemId)));
    }

    @Test
    void shouldValidateItemResponseBody() {
        titleId = titleApi.createTitle(getTitleDTO());
        itemId = itemApi.createItem(getItemDTO());
        List<NewItemDTO> itemsList = itemApi.getItemsForTitle(userId, titleId);

        assertThat(itemsList, is(notNullValue()));
        assertThat(itemsList.size(), greaterThan(0));

        NewItemDTO lastItem = itemsList.getLast();
        String expectedPurchaseDate = "2025-02-10";

        assertThat(lastItem.getId(), is(notNullValue()));
        assertThat(lastItem.getPurchaseDate(), is(equalTo(expectedPurchaseDate)));
        assertThat(itemsList.getFirst().getStatus(), is(equalTo(Status.AVAILABLE)));
    }

    @Test
    void shouldBePossibleToDeleteNotRentedItem() {
        titleId = titleApi.createTitle(getTitleDTO());
        itemId = itemApi.createItem(getItemDTO());

        List<NewItemDTO> beforeList = itemApi.getItemsForTitle(userId, titleId);

        itemApi.deleteItem(userId, itemId);

        List<NewItemDTO> afterList = itemApi.getItemsForTitle(userId, titleId);
        List<Integer> afterListIds = afterList.stream().map(NewItemDTO::getId).toList();

        assertThat(afterList.size(), is(beforeList.size() - 1));
        assertThat(afterListIds, not(hasItem(itemId)));
    }

    @Test
    void shouldGetEmptyItemList() {
        deletionService.deleteTitlesWithDependencies(userId);

        List<NewItemDTO> items = itemApi.getItemsForTitle(userId, titleId);

        assertThat(items, is(empty()));
    }

    @Test
    void shouldNotBePossibleToDeleteItemWithRentHistory() {
        titleId = titleApi.createTitle(getTitleDTO());
        itemId = itemApi.createItem(getItemDTO());
        rentApi.createRent(getRentDTO());

        assertThatThrownBy(() -> itemApi.deleteItem(userId, itemId))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("500");

        List<NewItemDTO> items = itemApi.getItemsForTitle(userId, titleId);
        assertThat(items.stream().map(NewItemDTO::getId).toList(), hasItem(itemId));
    }
}
