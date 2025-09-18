package com.projects.tests;

import com.projects.pages.LoginPage;
import com.projects.pages.ProductsPage;
import com.projects.util.TestDataLoader;
import com.projects.util.User;
import io.qameta.allure.*;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.junit.jupiter.api.Assertions.assertTrue;

@Epic("Swag Labs UI Tests")
@Feature("Products")
public class ProductsTest {

    private static final Logger log = LoggerFactory.getLogger(ProductsTest.class);

    private final LoginPage loginPage = new LoginPage();
    private final ProductsPage productsPage = new ProductsPage();

    @Test
    @Story("View Products")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Verify user can see a product after logging in")
    public void testViewProduct() {
        User user = TestDataLoader.getUser("standard_user");

        log.info("Opening login page for product view test");
        loginPage.openLogin();

        log.info("Logging in as user: {}", user.getUsername());
        loginPage.loginAs(user);

        String productName = "Sauce Labs Backpack";
        log.info("Checking if product '{}' is displayed on Products page", productName);

        productsPage.shouldHaveProduct(productName);

        assertTrue(productsPage.isProductDisplayed(productName),
                "Product '" + productName + "' should be visible on Products page");

        log.info("Product view test completed successfully");
    }

    @Test
    @Story("Add to Cart")
    @Severity(SeverityLevel.BLOCKER)
    @Description("Verify user can add a product to the cart")
    public void testAddProductToCart() {
        User user = TestDataLoader.getUser("standard_user");

        log.info("Opening login page for add-to-cart test");
        loginPage.openLogin();

        log.info("Logging in as user: {}", user.getUsername());
        loginPage.loginAs(user);

        String productName = "Sauce Labs Bike Light";
        log.info("Adding product '{}' to cart", productName);

        productsPage.addProductToCart(productName);

        assertTrue(productsPage.isProductInCart(productName),
                "Product '" + productName + "' should be marked as added to cart");

        log.info("Add-to-cart test completed successfully");
    }
}
