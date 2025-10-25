package com.ebook_app_render.tests.api;

import com.ebook_app_render.api.dto.ItemDTO;
import com.ebook_app_render.api.dto.LoginDTO;
import com.ebook_app_render.api.dto.RentDTO;
import com.ebook_app_render.api.dto.TitleDTO;
import com.ebook_app_render.api.service.DeletionService;
import com.ebook_app_render.api.service.ItemApi;
import com.ebook_app_render.api.service.RentApi;
import com.ebook_app_render.api.service.TitleApi;
import com.ebook_app_render.api.config.ConfigProvider;
import com.ebook_app_render.tests.api.service.ItemService;
import com.ebook_app_render.tests.api.service.LoginService;
import com.ebook_app_render.tests.api.service.RentService;
import com.ebook_app_render.tests.api.service.TitleService;
import io.restassured.RestAssured;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;

import java.time.LocalDate;

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

    protected String author = "Default Author";
    protected String title = "Default Title";
    protected int year = 2025;
    protected String customer = "Default Customer";
    protected String purchaseDate = "2025-02-10";
    protected String today = LocalDate.now().toString();

    @BeforeClass(alwaysRun = true)
    public void setupAll() {
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
        return TitleDTO.builder()
                .userId(userId)
                .author(author)
                .title(title)
                .year(year)
                .build();
    }

    protected ItemDTO getItemDTO() {
        return ItemDTO.builder()
                .userId(userId)
                .titleId(titleId)
                .purchaseDate(purchaseDate)
                .build();
    }

    protected RentDTO getRentDTO() {
        return RentDTO.builder()
                .userId(userId)
                .itemId(itemId)
                .rentDate(today)
                .customerName(customer)
                .build();
    }

    @AfterMethod(alwaysRun = true)
    public void cleanupAfterEach() {
        deletionService.deleteAllTitlesWithDependencies(userId);
    }
}