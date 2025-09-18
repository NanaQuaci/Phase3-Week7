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
}
