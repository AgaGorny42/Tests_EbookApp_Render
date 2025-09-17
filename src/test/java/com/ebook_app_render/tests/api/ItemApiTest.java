package com.ebook_app_render.tests.api;

import com.ebook_app_render.dto.ItemDTO;
import com.ebook_app_render.dto.Status;
import com.ebook_app_render.service.ItemService;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertTrue;


public class ItemApiTest extends BaseApiTest{

    ItemService itemService = new ItemService();
    ItemDTO itemDTo = new ItemDTO(getUserId(), getFirstTitleId(), "2025-02-10");

    @Test
    void shouldAddOneItemAndVerifyId(){
        int itemId = itemService.addItemAndGetId(itemDTo);

        assertThat(itemId, is(notNullValue()));
    }

    @Test
    void shouldAddTwoItemsAndCompareIds(){
        int firstItemId = itemService.addItemAndGetId(itemDTo);
        int secondItemId = itemService.addItemAndGetId(itemDTo);

        assertThat(firstItemId, not(equalTo(secondItemId)));
    }

    @Test
    void shouldValidateItemResponseBody(){
        List<ItemDTO> itemsList = itemService.getItemsList();
        String expectedPurchaseDate = "2025-02-10";
        String actualPurchaseDate = itemsList.getLast().getPurchaseDate();

        assertThat(itemsList, is(notNullValue()));

        assertThat(itemsList.getLast().getId(), is(notNullValue()));
        assertThat(expectedPurchaseDate, is(equalTo(actualPurchaseDate)));
        assertThat(itemsList.getFirst().getStatus(), is(equalTo(Status.AVAILABLE)));
    }

    @Test
    void shouldBePossibleToDeleteNotRentedItem(){
        int newItemId = itemService.addItemAndGetId(itemDTo);
        List<ItemDTO> beforeList = itemService.getItemsList();

        itemService.deleteItem(newItemId);
        List<ItemDTO> afterlist = itemService.getItemsList();
        List<Integer> afterListIds = afterlist.stream().map(ItemDTO::getId).toList();

        assertThat(afterlist.size(), is(beforeList.size() -1));
        assertThat(afterListIds, not(hasItem(newItemId)));
    }

    @Test
    void shouldGetItemListWithAtLeastOneItem(){
        itemService.deleteAllItemsLessFirst();
        assertTrue((itemService.getItemsList().size()) <= 1);
    }

    @Test
    void shouldNotBePossibleToDeleteItemWithRentHistory(){

    }

    @Test
    void shouldBePossibleToDeleteItemWithoutRentHistory(){

    }
    @Test
    void itShouldNotBePossibleToChooseOrChangeItemPurchaseDateToAnyDateTest() {

    }
}
