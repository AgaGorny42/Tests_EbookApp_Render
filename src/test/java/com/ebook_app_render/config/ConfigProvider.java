package com.ebook_app_render.config;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class ConfigProvider {

    private static final Properties properties = new Properties();

    static {
        try (InputStream input = ConfigProvider.class.getClassLoader()
                .getResourceAsStream("application.properties")) {
            if (input == null) {
                throw new RuntimeException("File application.properties not found in resources.");
            }
            properties.load(input);
        } catch (IOException e) {
            throw new RuntimeException("Error while loading application.properties", e);
        }
    }

    public static String get(String key) {
        String value = properties.getProperty(key);
        if (value == null) {
            throw new IllegalArgumentException("Missing key in application.properties: " + key);
        }
        return value;
    }

    public static int getInt(String key) {
        return Integer.parseInt(get(key));
    }

    public static boolean getBoolean(String key) {
        return Boolean.parseBoolean(get(key));
    }
}
