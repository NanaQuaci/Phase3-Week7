package com.projects.tests;

import com.codeborne.selenide.junit5.ScreenShooterExtension;
import com.projects.base.BaseTest;
import com.projects.pages.*;
import com.projects.util.CheckoutInfo;
import com.projects.util.TestDataLoader;
import com.projects.util.User;
import io.qameta.allure.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertTrue;

@Epic("Swag Labs UI Tests")
@Feature("Checkout Functionality")
@ExtendWith({ScreenShooterExtension.class})
public class CheckoutTest extends BaseTest {

    private static final Logger log = LoggerFactory.getLogger(CheckoutTest.class);

    // -----------------------
    // Parameterized test data
    // -----------------------
    static Stream<CheckoutScenario> checkoutScenarios() {
        return Stream.of(
                new CheckoutScenario("standard_user", new String[]{"backpack"}, "valid", true),
                new CheckoutScenario("standard_user", new String[]{"backpack", "bike_light"}, "valid", true),
                new CheckoutScenario("standard_user", new String[]{"backpack"}, "invalid", false),
                new CheckoutScenario("locked_out_user", new String[]{"backpack"}, "valid", false),
                new CheckoutScenario("performance_glitch_user", new String[]{"backpack"}, "valid", true)
        );
    }

    // -----------------------
    // Parameterized Test
    // -----------------------
    @ParameterizedTest(name = "{index} => user={0}")
    @MethodSource("checkoutScenarios")
    @Story("Checkout Scenarios")
    @Severity(SeverityLevel.BLOCKER)
    @Description("Run multiple checkout scenarios with different users, products, and data")
    void testCheckoutScenarios(CheckoutScenario scenario) {
        User user = TestDataLoader.getUser(scenario.username);

        log.info("Starting checkout test for user: {}", user.getUsername());
        LoginPage loginPage = new LoginPage();
        loginPage.openLogin();
        loginPage.loginAs(user);

        ProductsPage productsPage = new ProductsPage();
        for (String productKey : scenario.products) {
            String product = TestDataLoader.getProduct(productKey);
            log.info("Adding product to cart: {}", product);
            productsPage.addProductToCart(product);
        }
        productsPage.openCart();

        CartPage cartPage = new CartPage();
        cartPage.checkout();

        CheckoutInfo checkoutInfo = TestDataLoader.getCheckoutInfo(scenario.checkoutDataKey);
        CheckoutPage checkoutPage = new CheckoutPage();
        checkoutPage.fillInformation(checkoutInfo.getFirstName(),
                checkoutInfo.getLastName(),
                checkoutInfo.getPostalCode());

        if (scenario.shouldSucceed) {
            checkoutPage.finish();
            checkoutPage.assertOrderComplete();
            assertTrue(checkoutPage.isOrderCompleteDisplayed(),
                    "Order completion message should be displayed");
        } else {
            String expectedError;
            if (user.getUsername().equals("locked_out_user")) {
                expectedError = TestDataLoader.getErrorMessage("lockedOut");
            } else if (scenario.checkoutDataKey.equals("invalid")) {
                expectedError = TestDataLoader.getErrorMessage("missingFirstName");
            } else {
                expectedError = "Unknown error";
            }

            checkoutPage.shouldSeeError(expectedError);
            assertTrue(checkoutPage.isErrorDisplayed(expectedError),
                    "Expected error message should be displayed: " + expectedError);
        }

        log.info("Test completed for user: {}", user.getUsername());
    }

    // -----------------------
    // Helper class for parameterization
    // -----------------------
    static class CheckoutScenario {
        final String username;
        final String[] products;
        final String checkoutDataKey;
        final boolean shouldSucceed;

        public CheckoutScenario(String username, String[] products, String checkoutDataKey, boolean shouldSucceed) {
            this.username = username;
            this.products = products;
            this.checkoutDataKey = checkoutDataKey;
            this.shouldSucceed = shouldSucceed;
        }

        @Override
        public String toString() {
            return username + " [" + String.join(",", products) + "]";
        }
    }
}
