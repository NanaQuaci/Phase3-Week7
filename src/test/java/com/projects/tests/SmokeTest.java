package com.projects.tests;

import com.projects.base.BaseTest;
import com.projects.pages.LoginPage;
import com.projects.pages.ProductsPage;
import com.projects.util.TestDataLoader;
import com.projects.util.User;
import com.codeborne.selenide.junit5.ScreenShooterExtension;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.Test;

@ExtendWith({ScreenShooterExtension.class})
public class SmokeTest extends BaseTest {
    private final LoginPage login = new LoginPage();
    private final ProductsPage products = new ProductsPage();

    @Test
    public void loginAndSeeProducts() {
        // Load data from JSON
        User user = TestDataLoader.getUser("standard_user");
        String product = TestDataLoader.getProduct("backpack");

        // Use data
        login.openLogin();
        login.loginAs(user);
        login.shouldSeeProductsPage();

        products.shouldHaveProduct(product);
    }
}
