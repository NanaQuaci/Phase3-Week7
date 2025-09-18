package com.projects.tests;

import com.projects.base.BaseTest;
import com.projects.pages.LoginPage;
import com.projects.pages.ProductsPage;
import com.projects.util.TestDataLoader;
import com.projects.util.User;
import com.codeborne.selenide.junit5.ScreenShooterExtension;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith({ScreenShooterExtension.class})
public class SmokeTest extends BaseTest {

    private static final Logger log = LoggerFactory.getLogger(SmokeTest.class);

    private final LoginPage login = new LoginPage();
    private final ProductsPage products = new ProductsPage();

    @Test
    public void loginAndSeeProducts() {
        log.info("Starting Smoke Test: login and verify products page");

        // Load data from JSON
        User user = TestDataLoader.getUser("standard_user");
        String product = TestDataLoader.getProduct("backpack");
        log.info("Loaded test data: username={}, product={}", user.getUsername(), product);

        // Use data
        log.info("Opening login page");
        login.openLogin();

        log.info("Logging in with valid credentials");
        login.loginAs(user);

        log.info("Verifying Products page is displayed after login");
        assertTrue(login.isProductsPageDisplayed(), "Products page should be visible after login");

        log.info("Checking if product '{}' is listed on the products page", product);
        products.shouldHaveProduct(product);

        log.info("Smoke Test completed successfully");
    }
}
