package com.projects.base;

import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.logevents.SelenideLogger;
import io.qameta.allure.selenide.AllureSelenide;
import org.junit.jupiter.api.*;
import org.openqa.selenium.chrome.ChromeOptions;

public class  BaseTest {

    @BeforeAll
    public static void setUp() {
        // Configurable via -D arguments or CI env vars
        Configuration.baseUrl = System.getProperty("baseUrl", "https://www.saucedemo.com");
        Configuration.browser = System.getProperty("browser", "chrome");
        Configuration.headless = Boolean.parseBoolean(System.getProperty("headless", "true"));
        Configuration.browserSize = System.getProperty("browserSize", "1366x768");
        Configuration.timeout = Long.parseLong(System.getProperty("timeout", "5000"));

        // Add safe defaults for Docker/GitHub Actions
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--no-sandbox");
        options.addArguments("--disable-dev-shm-usage");
        options.addArguments("--disable-gpu");
        options.addArguments("--disable-software-rasterizer");
        options.addArguments("--remote-debugging-port=9222");

        if (Configuration.headless) {
            options.addArguments("--headless=new");
        }

        Configuration.browserCapabilities = options;

        // Register Allure listener (attaches screenshots/page source on failures)
        SelenideLogger.addListener("AllureSelenide", new AllureSelenide()
                .screenshots(true)
                .savePageSource(true));
    }

    @AfterAll
    public static void tearDown() {
        SelenideLogger.removeListener("AllureSelenide");
    }
}
