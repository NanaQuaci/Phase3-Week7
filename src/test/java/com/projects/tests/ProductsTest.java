package com.projects.tests;

import com.projects.pages.*;
import com.projects.util.TestDataLoader;
import com.projects.util.User;
import io.qameta.allure.*;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Selenide.$$;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;

@Epic("Swag Labs UI Tests")
@Feature("Products")
public class ProductsTest {

    private static final Logger log = LoggerFactory.getLogger(ProductsTest.class);

    private final LoginPage loginPage = new LoginPage();
    private final ProductsPage productsPage = new ProductsPage();
    private final ProductDetailsPage detailsPage = new ProductDetailsPage();
    private final CartPage cartPage = new CartPage();

    @Test
    @Story("View Products")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Verify user can see a product after logging in")
    public void testViewProduct() {
        User user = TestDataLoader.getUser("standard_user");

        log.info("Opening login page for product view test");
        loginPage.openLogin();
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
        loginPage.loginAs(user);

        String productName = "Sauce Labs Bike Light";
        log.info("Adding product '{}' to cart", productName);

        productsPage.addProductToCart(productName);

        assertTrue(productsPage.isProductInCart(productName),
                "Product '" + productName + "' should be marked as added to cart");

        log.info("Add-to-cart test completed successfully");
    }

    @Test
    @Story("View Product Details")
    @Severity(SeverityLevel.NORMAL)
    @Description("Verify user can open a product detail page and go back to products")
    public void testViewProductDetailsAndBack() {
        User user = TestDataLoader.getUser("standard_user");

        log.info("Opening login page for product details test");
        loginPage.openLogin();
        loginPage.loginAs(user);

        String productName = "Sauce Labs Backpack";
        log.info("Opening product details for '{}'", productName);

        // Click product to open details
        $$(".inventory_item_name").findBy(text(productName)).click();

        detailsPage.shouldSeeProductTitle(productName);
        log.info("Verified product details page shows product title: {}", productName);

        detailsPage.backToProducts();
        productsPage.shouldBeVisible();

        log.info("Successfully navigated back to products page");
    }

    @Test
    @Story("Remove Product from Cart")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Verify user can remove a product from the cart")
    public void testRemoveProductFromCart() {
        User user = TestDataLoader.getUser("standard_user");

        log.info("Opening login page for remove-from-cart test");
        loginPage.openLogin();
        loginPage.loginAs(user);

        String productName = "Sauce Labs Fleece Jacket";
        log.info("Adding product '{}' to cart, then removing it", productName);

        productsPage.addProductToCart(productName);
        assertTrue(productsPage.isProductInCart(productName),
                "Product should first be in cart");

        cartPage.removeItemFromCart(productName);
        assertTrue(cartPage.isCartBadgeGone(),
                "Cart badge should disappear after removing product");

        log.info("Remove-from-cart test completed successfully");
    }

    @Test
    @Story("Multiple Items in Cart")
    @Severity(SeverityLevel.NORMAL)
    @Description("Verify user can add multiple products and cart badge updates correctly")
    public void testAddMultipleProductsToCart() {
        User user = TestDataLoader.getUser("standard_user");

        log.info("Opening login page for multiple-items test");
        loginPage.openLogin();
        loginPage.loginAs(user);

        String product1 = "Sauce Labs Backpack";
        String product2 = "Sauce Labs Bolt T-Shirt";

        log.info("Adding two products '{}' and '{}' to cart", product1, product2);

        productsPage.addProductToCart(product1);
        productsPage.addProductToCart(product2);

        String badgeCount = cartPage.getCartBadgeCount();
        assertEquals("2", badgeCount,
                "Cart badge should show 2 after adding two products");

        log.info("Multiple-items test completed successfully");
    }
}
