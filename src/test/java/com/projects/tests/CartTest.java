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

import static org.junit.jupiter.api.Assertions.*;

@Epic("Swag Labs UI Tests")
@Feature("Cart Functionality")
@ExtendWith({ScreenShooterExtension.class})
public class CartTest extends BaseTest {

    @Test
    @Story("Add Item to Cart Only")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Verify that an item can be added to the cart and cart badge updates")
    public void testAddItemToCartOnly() {
        User user = TestDataLoader.getUser("standard_user");

        LoginPage loginPage = new LoginPage();
        loginPage.openLogin();
        loginPage.loginAs(user);

        CartPage cartPage = new CartPage();
        cartPage.addItemToCart("Sauce Labs Backpack");

        String badgeCount = cartPage.getCartBadgeCount();
        assertEquals("1", badgeCount, "Cart badge should reflect 1 item after adding");
    }

    @Test
    @Story("Add and Remove Item")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Test adding and removing item from cart updates the cart badge and contents")
    public void testAddAndRemoveItemFromCart() {
        User user = TestDataLoader.getUser("standard_user");

        LoginPage loginPage = new LoginPage();
        loginPage.openLogin();
        loginPage.loginAs(user);

        CartPage cartPage = new CartPage();
        cartPage.addItemToCart("Sauce Labs Backpack");
        assertEquals("1", cartPage.getCartBadgeCount());

        cartPage.goToCart();
        assertTrue(cartPage.isItemInCart("Sauce Labs Backpack"));

        cartPage.removeItemFromCart("Sauce Labs Backpack");
        assertFalse(cartPage.isItemInCart("Sauce Labs Backpack"));
    }

    @Test
    @Story("Remove Item Without Navigating to Cart")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Verify user can remove item directly from product page after adding it")
    public void testRemoveItemWithoutOpeningCart() {
        User user = TestDataLoader.getUser("standard_user");

        LoginPage loginPage = new LoginPage();
        loginPage.openLogin();
        loginPage.loginAs(user);

        CartPage cartPage = new CartPage();
        cartPage.addItemToCart("Sauce Labs Backpack");
        assertEquals("1", cartPage.getCartBadgeCount());

        cartPage.removeItemFromCart("Sauce Labs Backpack");
        assertTrue(cartPage.isCartBadgeGone());
    }
}
