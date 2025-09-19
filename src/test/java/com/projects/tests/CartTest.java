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

import static com.codeborne.selenide.Selenide.$;
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

    @Test
    @Story("Add Multiple Items")
    @Severity(SeverityLevel.NORMAL)
    @Description("Verify user can add multiple items and cart badge reflects correct count")
    public void testAddMultipleItems() {
        User user = TestDataLoader.getUser("standard_user");
        log.info("Starting test: Add Multiple Items");

        LoginPage loginPage = new LoginPage();
        loginPage.openLogin();
        loginPage.loginAs(user);

        CartPage cartPage = new CartPage();
        cartPage.addItemToCart("Sauce Labs Backpack");
        cartPage.addItemToCart("Sauce Labs Bike Light");

        log.info("Verifying cart badge shows 2 items");
        assertEquals("2", cartPage.getCartBadgeCount(), "Cart badge should show 2 after adding two items");

        log.info("Test completed: Add Multiple Items");
    }

    @Test
    @Story("Add Same Item Twice")
    @Severity(SeverityLevel.NORMAL)
    @Description("Verify adding the same item twice does not duplicate in cart")
    public void testAddSameItemTwice() {
        User user = TestDataLoader.getUser("standard_user");
        log.info("Starting test: Add Same Item Twice");

        LoginPage loginPage = new LoginPage();
        loginPage.openLogin();
        loginPage.loginAs(user);

        CartPage cartPage = new CartPage();
        cartPage.addItemToCart("Sauce Labs Backpack");
        cartPage.addItemToCart("Sauce Labs Backpack"); // clicking twice

        log.info("Verifying cart badge is still 1 (no duplicates)");
        assertEquals("1", cartPage.getCartBadgeCount(), "Cart badge should not increase when adding the same item again");

        log.info("Test completed: Add Same Item Twice");
    }

    @Test
    @Story("Remove Nonexistent Item")
    @Severity(SeverityLevel.MINOR)
    @Description("Verify removing an item that was never added does not break the cart")
    public void testRemoveNonexistentItem() {
        User user = TestDataLoader.getUser("standard_user");
        log.info("Starting test: Remove Nonexistent Item");

        LoginPage loginPage = new LoginPage();
        loginPage.openLogin();
        loginPage.loginAs(user);

        CartPage cartPage = new CartPage();
        log.info("Attempting to remove an item not in cart: Sauce Labs Bolt T-Shirt");
        cartPage.removeItemFromCart("Sauce Labs Bolt T-Shirt");

        log.info("Verifying cart badge is gone since nothing was added");
        assertTrue(cartPage.isCartBadgeGone(), "Cart badge should not exist when no items were added");

        log.info("Test completed: Remove Nonexistent Item");
    }

    @Test
    @Story("Cart Persistence After Logout")
    @Severity(SeverityLevel.NORMAL)
    @Description("Verify cart does not persist after logout and login again")
    public void testCartDoesNotPersistAfterLogout() {
        User user = TestDataLoader.getUser("standard_user");
        log.info("Starting test: Cart Persistence After Logout");

        LoginPage loginPage = new LoginPage();
        loginPage.openLogin();
        loginPage.loginAs(user);

        CartPage cartPage = new CartPage();
        log.info("Adding item to cart: Sauce Labs Backpack");
        cartPage.addItemToCart("Sauce Labs Backpack");

        log.info("Logging out");
        $("#react-burger-menu-btn").click();
        $("#logout_sidebar_link").click();

        log.info("Logging in again with same user");
        loginPage.loginAs(user);

        log.info("Verifying cart badge is gone after re-login");
        assertTrue(cartPage.isCartBadgeGone(), "Cart should be reset after logout and login");

        log.info("Test completed: Cart Persistence After Logout");
    }
}
