package com.ebook_app_render.ui.utils;

public class TestDataGenerator {

    public static String randomLogin() {
        return "Log" + System.currentTimeMillis();
    }

    public static String randomPassword() {
        return "Pass" + System.currentTimeMillis();
    }
}

