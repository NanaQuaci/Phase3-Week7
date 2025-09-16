package com.projects.base;

import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.logevents.SelenideLogger;
import io.qameta.allure.selenide.AllureSelenide;
import org.junit.jupiter.api.*;

public class BaseTest {
    @BeforeAll
    public static void setUp() {
        // configurable via -D arguments or CI env vars
        Configuration.baseUrl = System.getProperty("baseUrl", "https://www.saucedemo.com");
        Configuration.browser = System.getProperty("browser", "chrome");
        Configuration.headless = Boolean.parseBoolean(System.getProperty("headless", "true"));
        Configuration.browserSize = System.getProperty("browserSize", "1366x768");
        Configuration.timeout = Long.parseLong(System.getProperty("timeout", "5000"));

        // If using remote selenium (Selenium Grid / Selenoid), set:
        // System.setProperty("selenide.remote", "http://selenoid:4444/wd/hub");

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
