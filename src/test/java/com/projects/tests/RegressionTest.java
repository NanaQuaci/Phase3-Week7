package com.projects.tests;

import com.projects.base.BaseTest;
import com.projects.pages.*;
import com.projects.util.*;
import com.codeborne.selenide.junit5.ScreenShooterExtension;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith({ScreenShooterExtension.class})
public class RegressionTest extends BaseTest {

    @Test
    public void addToCartAndCheckout() {
        // Load test data from JSON
        User user = TestDataLoader.getUser("standard_user");
        String product = TestDataLoader.getProduct("backpack");
        CheckoutInfo checkoutInfo = TestDataLoader.getCheckoutInfo("valid");

        // Login
        LoginPage login = new LoginPage();
        login.openLogin();
        login.loginAs(user);

        // Add product and open cart
        ProductsPage products = new ProductsPage();
        products.addProductToCart(product);
        products.openCart();

        // Checkout
        CartPage cart = new CartPage();
        cart.checkout();

        CheckoutPage checkout = new CheckoutPage();
        checkout.fillInformation(
                checkoutInfo.getFirstName(),
                checkoutInfo.getLastName(),
                checkoutInfo.getPostalCode()
        );
        checkout.finish();
        checkout.assertOrderComplete();
    }
}
