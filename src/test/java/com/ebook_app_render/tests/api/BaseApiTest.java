package com.ebook_app_render.tests.api;

import com.ebook_app_render.config.ConfigProvider;
import com.ebook_app_render.dto.*;
import com.ebook_app_render.service.*;
import io.restassured.RestAssured;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.TestInstance;

import java.time.LocalDate;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public abstract class BaseApiTest {

    protected TitleApi titleApi;
    protected ItemApi itemApi;
    protected RentApi rentApi;
    protected DeletionService deletionService;
    protected TitleService titleService;
    protected ItemService itemService;
    protected RentService rentService;

    protected int userId;
    protected int titleId;
    protected int itemId;
    protected int rentId;

    String author = "Default Author";
    String title = "Default Title";
    int year = 2025;
    String customer = "Default Customer";
    String purchaseDate = "2025-02-10";
    String today = LocalDate.now().toString();

    @BeforeAll
    void setupAll() {
        RestAssured.baseURI = ConfigProvider.get("api.baseUrl");

        titleApi = new TitleService();
        itemApi = new ItemService();
        rentApi = new RentService();
        titleService = new TitleService();
        itemService = new ItemService();
        rentService = new RentService();
        deletionService = new DeletionService(titleApi, itemApi, rentApi);

        userId = getUserId();
    }

    protected int getUserId() {
        LoginDTO loginDTO = new LoginDTO(
                ConfigProvider.get("api.login"),
                ConfigProvider.get("api.password")
        );
        return new LoginService().loginAndGetUserId(loginDTO);
    }

    protected TitleDTO getTitleDTO() {
        return TitleDTO.builder().userId(userId).author(author).title(title).year(year).build();
    }

    protected ItemDTO getItemDTO() {
        return ItemDTO.builder().userId(userId).titleId(titleId).purchaseDate(purchaseDate).build();
    }

    protected RentDTO getRentDTO() {
        return RentDTO.builder().userId(userId).itemId(itemId).rentDate(today).customerName(customer).build();
    }

    @AfterEach
    void cleanupAfterEach() {
        deletionService.deleteAllTitlesWithDependencies(userId);
    }
}