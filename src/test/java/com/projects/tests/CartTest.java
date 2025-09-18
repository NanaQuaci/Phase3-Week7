package com.projects.tests;

import com.codeborne.selenide.junit5.ScreenShooterExtension;
import com.projects.base.BaseTest;
import com.projects.pages.CartPage;
import com.projects.pages.LoginPage;
import com.projects.util.TestDataLoader;
import com.projects.util.User;
import io.qameta.allure.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.junit.jupiter.api.Assertions.*;

@Epic("Swag Labs UI Tests")
@Feature("Cart Functionality")
@ExtendWith({ScreenShooterExtension.class})
public class CartTest extends BaseTest {

    private static final Logger log = LoggerFactory.getLogger(CartTest.class);

    @Test
    @Story("Add Item to Cart Only")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Verify that an item can be added to the cart and cart badge updates")
    public void testAddItemToCartOnly() {
        User user = TestDataLoader.getUser("standard_user");
        log.info("Starting test: Add Item to Cart Only");

        LoginPage loginPage = new LoginPage();
        log.info("Opening login page and logging in as {}", user.getUsername());
        loginPage.openLogin();
        loginPage.loginAs(user);

        CartPage cartPage = new CartPage();
        log.info("Adding item to cart: Sauce Labs Backpack");
        cartPage.addItemToCart("Sauce Labs Backpack");

        String badgeCount = cartPage.getCartBadgeCount();
        log.info("Verifying cart badge count is updated to 1");
        assertEquals("1", badgeCount, "Cart badge should reflect 1 item after adding");

        log.info("Test completed: Add Item to Cart Only");
    }

    @Test
    @Story("Add and Remove Item")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Test adding and removing item from cart updates the cart badge and contents")
    public void testAddAndRemoveItemFromCart() {
        User user = TestDataLoader.getUser("standard_user");
        log.info("Starting test: Add and Remove Item");

        LoginPage loginPage = new LoginPage();
        log.info("Opening login page and logging in as {}", user.getUsername());
        loginPage.openLogin();
        loginPage.loginAs(user);

        CartPage cartPage = new CartPage();
        log.info("Adding item to cart: Sauce Labs Backpack");
        cartPage.addItemToCart("Sauce Labs Backpack");

        log.info("Verifying cart badge shows 1 item");
        assertEquals("1", cartPage.getCartBadgeCount(), "Cart badge should show 1 after adding item");

        log.info("Navigating to cart and verifying item is present");
        cartPage.goToCart();
        assertTrue(cartPage.isItemInCart("Sauce Labs Backpack"), "Item should be present in cart");

        log.info("Removing item from cart and verifying it is gone");
        cartPage.removeItemFromCart("Sauce Labs Backpack");
        assertFalse(cartPage.isItemInCart("Sauce Labs Backpack"), "Item should be removed from cart");

        log.info("Test completed: Add and Remove Item");
    }

    @Test
    @Story("Remove Item Without Navigating to Cart")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Verify user can remove item directly from product page after adding it")
    public void testRemoveItemWithoutOpeningCart() {
        User user = TestDataLoader.getUser("standard_user");
        log.info("Starting test: Remove Item Without Navigating to Cart");

        LoginPage loginPage = new LoginPage();
        log.info("Opening login page and logging in as {}", user.getUsername());
        loginPage.openLogin();
        loginPage.loginAs(user);

        CartPage cartPage = new CartPage();
        log.info("Adding item to cart: Sauce Labs Backpack");
        cartPage.addItemToCart("Sauce Labs Backpack");

        log.info("Verifying cart badge shows 1 item");
        assertEquals("1", cartPage.getCartBadgeCount(), "Cart badge should show 1 after adding item");

        log.info("Removing item directly from product page");
        cartPage.removeItemFromCart("Sauce Labs Backpack");

        log.info("Verifying cart badge disappears after removal");
        assertTrue(cartPage.isCartBadgeGone(), "Cart badge should not be visible after removing last item");

        log.info("Test completed: Remove Item Without Navigating to Cart");
    }
}
