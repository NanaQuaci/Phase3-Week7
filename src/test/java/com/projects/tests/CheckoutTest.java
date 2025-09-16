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

import static org.junit.jupiter.api.Assertions.assertEquals;

@Epic("Swag Labs UI Tests")
@Feature("Checkout Functionality")
@ExtendWith({ScreenShooterExtension.class})
public class CheckoutTest extends BaseTest {

    @Test
    @Story("Successful Checkout")
    @Severity(SeverityLevel.BLOCKER)
    @Description("Verify user can complete checkout with valid information")
    public void testSuccessfulCheckout() {
        User user = TestDataLoader.getUser("standard_user");
        String product = TestDataLoader.getProduct("backpack");
        CheckoutInfo checkoutInfo = TestDataLoader.getCheckoutInfo("valid");

        new LoginPage().openLogin();
        new LoginPage().loginAs(user);

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

        new LoginPage().openLogin();
        new LoginPage().loginAs(user);

        ProductsPage products = new ProductsPage();
        products.addProductToCart(product);
        products.openCart();

        CartPage cart = new CartPage();
        cart.checkout();

        CheckoutPage checkout = new CheckoutPage();
        checkout.fillInformation(checkoutInfo.getFirstName(),
                checkoutInfo.getLastName(),
                checkoutInfo.getPostalCode());
        checkout.shouldSeeError(expectedError);
    }

}
