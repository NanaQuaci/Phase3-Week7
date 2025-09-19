package com.projects.tests;

import com.codeborne.selenide.junit5.ScreenShooterExtension;
import com.projects.base.BaseTest;
import com.projects.pages.*;
import com.projects.util.CheckoutInfo;
import com.projects.util.TestDataLoader;
import com.projects.util.User;
import io.qameta.allure.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;

@Epic("Swag Labs UI Tests")
@Feature("Checkout Functionality")
@ExtendWith({ScreenShooterExtension.class})
public class CheckoutTest extends BaseTest {

    private static final Logger log = LoggerFactory.getLogger(CheckoutTest.class);

    @Test
    @Story("Successful Checkout")
    @Severity(SeverityLevel.BLOCKER)
    @Description("Verify user can complete checkout with valid information")
    public void testSuccessfulCheckout() {
        User user = TestDataLoader.getUser("standard_user");
        String product = TestDataLoader.getProduct("backpack");
        CheckoutInfo checkoutInfo = TestDataLoader.getCheckoutInfo("valid");

        log.info("Starting test: Successful Checkout with user {}", user.getUsername());
        LoginPage loginPage = new LoginPage();
        loginPage.openLogin();
        loginPage.loginAs(user);

        log.info("Adding product to cart: {}", product);
        ProductsPage products = new ProductsPage();
        products.addProductToCart(product);
        products.openCart();

        log.info("Proceeding to checkout");
        CartPage cart = new CartPage();
        cart.checkout();

        log.info("Filling in checkout information: {}, {}, {}",
                checkoutInfo.getFirstName(), checkoutInfo.getLastName(), checkoutInfo.getPostalCode());
        CheckoutPage checkout = new CheckoutPage();
        checkout.fillInformation(checkoutInfo.getFirstName(),
                checkoutInfo.getLastName(),
                checkoutInfo.getPostalCode());

        log.info("Finishing checkout process");
        checkout.finish();

        log.info("Verifying order completion message");
        checkout.assertOrderComplete();
        assertTrue(checkout.isOrderCompleteDisplayed(), "Order completion message should be displayed");

        log.info("Test completed: Successful Checkout");
    }

    @Test
    @Story("Missing Information Validation")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Verify checkout shows error when mandatory fields are missing")
    public void testCheckoutWithMissingInfo() {
        User user = TestDataLoader.getUser("standard_user");
        String product = TestDataLoader.getProduct("backpack");
        CheckoutInfo checkoutInfo = TestDataLoader.getCheckoutInfo("invalid");
        String expectedError = TestDataLoader.getErrorMessage("missingFirstName");

        log.info("Starting test: Checkout With Missing Info for user {}", user.getUsername());
        LoginPage loginPage = new LoginPage();
        loginPage.openLogin();
        loginPage.loginAs(user);

        log.info("Adding product to cart: {}", product);
        ProductsPage products = new ProductsPage();
        products.addProductToCart(product);
        products.openCart();

        log.info("Proceeding to checkout");
        CartPage cart = new CartPage();
        cart.checkout();

        log.info("Filling in incomplete checkout information: {}, {}, {}",
                checkoutInfo.getFirstName(), checkoutInfo.getLastName(), checkoutInfo.getPostalCode());
        CheckoutPage checkout = new CheckoutPage();
        checkout.fillInformation(checkoutInfo.getFirstName(),
                checkoutInfo.getLastName(),
                checkoutInfo.getPostalCode());

        log.info("Verifying error message for missing information");
        checkout.shouldSeeError(expectedError);
        assertTrue(checkout.isErrorDisplayed(expectedError),
                "Expected error message should be displayed: " + expectedError);

        log.info("Test completed: Checkout With Missing Info");
    }

    @Test
    @Story("Checkout Cancel Flow")
    @Severity(SeverityLevel.NORMAL)
    @Description("Verify that user can cancel checkout and return to product listing")
    public void testCheckoutCancelFlow() {
        User user = TestDataLoader.getUser("standard_user");
        String product = TestDataLoader.getProduct("bikeLight");

        log.info("Starting test: Checkout Cancel Flow for user {}", user.getUsername());
        LoginPage loginPage = new LoginPage();
        loginPage.openLogin();
        loginPage.loginAs(user);

        log.info("Adding product to cart: {}", product);
        ProductsPage products = new ProductsPage();
        products.addProductToCart(product);
        products.openCart();

        log.info("Proceeding to checkout");
        CartPage cart = new CartPage();
        cart.checkout();

        log.info("Canceling checkout");
        CheckoutPage checkout = new CheckoutPage();
        checkout.cancel();

        log.info("Verifying that user is redirected back to products page");
        products.shouldBeVisible();

        log.info("Test completed: Checkout Cancel Flow");
    }


    @Test
    @Story("Checkout with Multiple Products")
    @Severity(SeverityLevel.BLOCKER)
    @Description("Verify user can checkout successfully with multiple items in cart")
    public void testCheckoutWithMultipleProducts() {
        User user = TestDataLoader.getUser("standard_user");
        String product1 = TestDataLoader.getProduct("backpack");
        String product2 = TestDataLoader.getProduct("bikeLight");
        CheckoutInfo checkoutInfo = TestDataLoader.getCheckoutInfo("valid");

        log.info("Starting test: Checkout with Multiple Products for user {}", user.getUsername());
        LoginPage loginPage = new LoginPage();
        loginPage.openLogin();
        loginPage.loginAs(user);

        log.info("Adding products to cart: {}, {}", product1, product2);
        ProductsPage products = new ProductsPage();
        products.addProductToCart(product1);
        products.addProductToCart(product2);
        products.openCart();

        CartPage cart = new CartPage();
        cart.checkout();

        CheckoutPage checkout = new CheckoutPage();
        checkout.fillInformation(checkoutInfo.getFirstName(),
                checkoutInfo.getLastName(),
                checkoutInfo.getPostalCode());
        checkout.finish();

        log.info("Verifying order completion");
        checkout.assertOrderComplete();
        assertTrue(checkout.isOrderCompleteDisplayed(), "Order completion message should be displayed");

        log.info("Test completed: Checkout with Multiple Products");
    }


    @Test
    @Story("Checkout Blocked for Locked Out User")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Verify locked_out_user cannot proceed to checkout")
    public void testLockedOutUserCheckoutBlocked() {
        User user = TestDataLoader.getUser("locked_out_user");
        String expectedError = TestDataLoader.getErrorMessage("lockedOut");

        log.info("Starting test: Locked Out User Checkout Blocked for {}", user.getUsername());
        LoginPage loginPage = new LoginPage();
        loginPage.openLogin();
        loginPage.loginAs(user);

        log.info("Verifying error message for locked out user");
        loginPage.shouldSeeError(expectedError);
        assertTrue(loginPage.isErrorDisplayed(expectedError), "Expected error message should be displayed");

        log.info("Test completed: Locked Out User Checkout Blocked");
    }


    @Test
    @Story("Checkout with Performance Glitch User")
    @Severity(SeverityLevel.MINOR)
    @Description("Verify performance_glitch_user can still complete checkout despite delays")
    public void testCheckoutWithPerformanceGlitchUser() {
        User user = TestDataLoader.getUser("performance_glitch_user");
        String product = TestDataLoader.getProduct("backpack");
        CheckoutInfo checkoutInfo = TestDataLoader.getCheckoutInfo("valid");

        log.info("Starting test: Checkout With Performance Glitch User {}", user.getUsername());
        LoginPage loginPage = new LoginPage();
        loginPage.openLogin();
        loginPage.loginAs(user);

        ProductsPage products = new ProductsPage();
        products.addProductToCart(product);
        products.openCart();

        CartPage cart = new CartPage();
        cart.checkout();

        CheckoutPage checkout = new CheckoutPage();
        checkout.fillInformation(checkoutInfo.getFirstName(),
                checkoutInfo.getLastName(),
                checkoutInfo.getPostalCode());
        checkout.finish();

        checkout.assertOrderComplete();
        assertTrue(checkout.isOrderCompleteDisplayed(), "Order completion message should be displayed");

        log.info("Test completed: Checkout With Performance Glitch User");
    }


}
