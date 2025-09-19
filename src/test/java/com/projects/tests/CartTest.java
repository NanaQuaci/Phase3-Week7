package com.projects.tests;

import com.codeborne.selenide.junit5.ScreenShooterExtension;
import com.projects.base.BaseTest;
import com.projects.pages.CartPage;
import com.projects.pages.LoginPage;
import com.projects.util.TestDataLoader;
import com.projects.util.User;
import io.qameta.allure.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.Test;

import java.util.stream.Stream;

import static com.codeborne.selenide.Selenide.$;
import static org.junit.jupiter.api.Assertions.*;

@Epic("Swag Labs UI Tests")
@Feature("Cart Functionality")
@ExtendWith({ScreenShooterExtension.class})
public class CartTest extends BaseTest {

    private static final Logger log = LoggerFactory.getLogger(CartTest.class);

    // --------------------- PARAMETERIZED TESTS ---------------------

    @ParameterizedTest(name = "Add and Remove Product: {0}")
    @MethodSource("cartProducts")
    @Story("Add and Remove Items")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Verify adding and removing different products updates the cart correctly")
    void testAddAndRemoveItemsParameterized(String productName) {
        User user = TestDataLoader.getUser("standard_user");
        log.info("Starting parameterized test: Add and Remove Product '{}'", productName);

        LoginPage loginPage = new LoginPage();
        loginPage.openLogin();
        loginPage.loginAs(user);

        CartPage cartPage = new CartPage();
        cartPage.addItemToCart(productName);
        assertTrue(cartPage.isItemInCart(productName), "Item should be in cart");

        cartPage.removeItemFromCart(productName);
        assertFalse(cartPage.isItemInCart(productName), "Item should be removed");

        log.info("Parameterized test completed for product '{}'", productName);
    }

    @ParameterizedTest(name = "Add Multiple Products: {0} and {1}")
    @MethodSource("cartMultipleProducts")
    @Story("Add Multiple Items")
    @Severity(SeverityLevel.NORMAL)
    @Description("Verify user can add multiple items and cart badge reflects correct count")
    void testAddMultipleItemsParameterized(String product1, String product2) {
        User user = TestDataLoader.getUser("standard_user");
        log.info("Starting parameterized test: Add Multiple Products '{}', '{}'", product1, product2);

        LoginPage loginPage = new LoginPage();
        loginPage.openLogin();
        loginPage.loginAs(user);

        CartPage cartPage = new CartPage();
        cartPage.addItemToCart(product1);
        cartPage.addItemToCart(product2);

        String badgeCount = cartPage.getCartBadgeCount();
        assertEquals("2", badgeCount, "Cart badge should show 2 after adding two items");

        log.info("Parameterized test completed for products '{}', '{}'", product1, product2);
    }

    static Stream<String> cartProducts() {
        return Stream.of("Sauce Labs Backpack", "Sauce Labs Bike Light", "Sauce Labs Bolt T-Shirt");
    }

    static Stream<Object[]> cartMultipleProducts() {
        return Stream.of(
                new Object[]{"Sauce Labs Backpack", "Sauce Labs Bike Light"},
                new Object[]{"Sauce Labs Bolt T-Shirt", "Sauce Labs Fleece Jacket"}
        );
    }

    // --------------------- SCENARIO-SPECIFIC TESTS ---------------------

    @Test
    @Story("Add Same Item Twice")
    @Severity(SeverityLevel.NORMAL)
    @Description("Verify adding the same item twice does not duplicate in cart")
    void testAddSameItemTwice() {
        User user = TestDataLoader.getUser("standard_user");
        log.info("Starting test: Add Same Item Twice");

        LoginPage loginPage = new LoginPage();
        loginPage.openLogin();
        loginPage.loginAs(user);

        CartPage cartPage = new CartPage();
        cartPage.addItemToCart("Sauce Labs Backpack");
        cartPage.addItemToCart("Sauce Labs Backpack"); // clicking twice

        assertEquals("1", cartPage.getCartBadgeCount(), "Cart badge should not increase when adding the same item again");

        log.info("Test completed: Add Same Item Twice");
    }

    @Test
    @Story("Remove Nonexistent Item")
    @Severity(SeverityLevel.MINOR)
    @Description("Verify removing an item that was never added does not break the cart")
    void testRemoveNonexistentItem() {
        User user = TestDataLoader.getUser("standard_user");
        log.info("Starting test: Remove Nonexistent Item");

        LoginPage loginPage = new LoginPage();
        loginPage.openLogin();
        loginPage.loginAs(user);

        CartPage cartPage = new CartPage();
        cartPage.removeItemFromCart("Sauce Labs Bolt T-Shirt");

        assertTrue(cartPage.isCartBadgeGone(), "Cart badge should not exist when no items were added");

        log.info("Test completed: Remove Nonexistent Item");
    }

    @Test
    @Story("Cart Persistence After Logout")
    @Severity(SeverityLevel.NORMAL)
    @Description("Verify cart does not persist after logout and login again")
    void testCartDoesNotPersistAfterLogout() {
        User user = TestDataLoader.getUser("standard_user");
        log.info("Starting test: Cart Persistence After Logout");

        LoginPage loginPage = new LoginPage();
        loginPage.openLogin();
        loginPage.loginAs(user);

        CartPage cartPage = new CartPage();
        cartPage.addItemToCart("Sauce Labs Backpack");

        // Logout
        $("#react-burger-menu-btn").click();
        $("#logout_sidebar_link").click();

        loginPage.loginAs(user);

        assertTrue(cartPage.isCartBadgeGone(), "Cart should be reset after logout and login");

        log.info("Test completed: Cart Persistence After Logout");
    }
}
