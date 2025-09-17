package com.ebook_app_render.tests.api;

import com.ebook_app_render.config.ConfigProvider;
import com.ebook_app_render.dto.LoginDTO;
import com.ebook_app_render.dto.NewRentDTO;
import com.ebook_app_render.service.ItemService;
import com.ebook_app_render.service.LoginService;
import com.ebook_app_render.service.RentService;
import com.ebook_app_render.service.TitleService;
import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeAll;

import java.util.List;

public abstract class BaseApiTest {

    @BeforeAll
    static void setup() {
        RestAssured.baseURI = ConfigProvider.get("api.baseUrl");
    }

    protected int getUserId() {
        LoginDTO loginDTO = new LoginDTO(
                ConfigProvider.get("api.login"),
                ConfigProvider.get("api.password")
        );
        LoginService loginService = new LoginService();
        return loginService.loginAndGetUserId(loginDTO);
    }

    protected int getFirstTitleId() {
        TitleService titleService = new TitleService();
        int titleId = titleService.getTitleList().getFirst().getId();
        if(titleId == 0) {
            System.out.println("No titles.");
        }else
            System.out.println("First title id is: " + titleId);
        return titleId;
    }

    protected int getFirstItemId(){
        ItemService itemService = new ItemService();
        int itemId = itemService.getItemsList().getFirst().getId();
        if(itemId == 0) {
            System.out.println("No items.");
        }else
            System.out.println("First item id is: " + itemId);
        return itemId;
    }

    protected int getFirstRentId(){
        RentService rentService = new RentService();
        List<NewRentDTO> rentalList = rentService.getRentsList();
        int rentId = rentalList.getFirst().getId();
        if(rentId == 0) {
            System.out.println("No rentals.");
        }else
            System.out.println("First rental id is: " + rentId);
        return rentId;
    }
}

