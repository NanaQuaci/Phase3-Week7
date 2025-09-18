package com.projects.tests;

import com.projects.base.BaseTest;
import com.projects.pages.*;
import com.projects.util.*;
import com.codeborne.selenide.junit5.ScreenShooterExtension;
import io.qameta.allure.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.junit.jupiter.api.Assertions.assertTrue;

@Epic("Swag Labs UI Tests")
@Feature("Regression Suite")
@ExtendWith({ScreenShooterExtension.class})
public class RegressionTest extends BaseTest {

    private static final Logger log = LoggerFactory.getLogger(RegressionTest.class);

    @Test
    @Story("Full Purchase Flow")
    @Severity(SeverityLevel.BLOCKER)
    @Description("Verify user can log in, add a product to cart, and complete checkout successfully")
    public void addToCartAndCheckout() {
        // Load test data from JSON
        User user = TestDataLoader.getUser("standard_user");
        String product = TestDataLoader.getProduct("backpack");
        CheckoutInfo checkoutInfo = TestDataLoader.getCheckoutInfo("valid");

        log.info("Starting full regression test: add to cart and checkout");

        // Login
        LoginPage login = new LoginPage();
        log.info("Opening login page");
        login.openLogin();

        log.info("Logging in as user: {}", user.getUsername());
        login.loginAs(user);

        assertTrue(login.isProductsPageDisplayed(),
                "User should be redirected to Products page after login");
        log.info("Login successful, Products page displayed");

        // Add product and open cart
        ProductsPage products = new ProductsPage();
        log.info("Adding product '{}' to cart", product);
        products.addProductToCart(product);

        assertTrue(products.isProductInCart(product),
                "Product '" + product + "' should be added to cart");
        log.info("Product '{}' successfully added to cart", product);

        log.info("Opening cart");
        products.openCart();

        // Checkout
        CartPage cart = new CartPage();
        log.info("Proceeding to checkout");
        cart.checkout();

        CheckoutPage checkout = new CheckoutPage();
        log.info("Filling in checkout information: {}, {}, {}",
                checkoutInfo.getFirstName(),
                checkoutInfo.getLastName(),
                checkoutInfo.getPostalCode());

        checkout.fillInformation(
                checkoutInfo.getFirstName(),
                checkoutInfo.getLastName(),
                checkoutInfo.getPostalCode()
        );

        log.info("Finishing checkout");
        checkout.finish();

        assertTrue(checkout.isOrderComplete(),
                "Order should be completed successfully");
        log.info("Checkout complete, order placed successfully");

        log.info("Full regression test completed successfully");
    }
}
