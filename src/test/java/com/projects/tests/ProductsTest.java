package com.projects.tests;

import com.projects.pages.*;
import com.projects.util.TestDataLoader;
import com.projects.util.User;
import io.qameta.allure.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.stream.Stream;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Selenide.$$;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;

@Epic("Swag Labs UI Tests")
@Feature("Products")
public class ProductsTest {

    private static final Logger log = LoggerFactory.getLogger(ProductsTest.class);

    private final LoginPage loginPage = new LoginPage();
    private final ProductsPage productsPage = new ProductsPage();
    private final ProductDetailsPage detailsPage = new ProductDetailsPage();
    private final CartPage cartPage = new CartPage();

    // -----------------------
    // Data source for parameterized test
    // -----------------------
    static Stream<ProductScenario> productScenarios() {
        return Stream.of(
                new ProductScenario("Sauce Labs Backpack", "view"),
                new ProductScenario("Sauce Labs Bike Light", "addToCart"),
                new ProductScenario("Sauce Labs Fleece Jacket", "removeFromCart"),
                new ProductScenario("Sauce Labs Bolt T-Shirt", "addMultiple")
        );
    }

    // -----------------------
    // Parameterized Test
    // -----------------------
    @ParameterizedTest(name = "{index} => product={0}, action={1}")
    @MethodSource("productScenarios")
    @Story("Product Operations")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Run multiple product-related operations dynamically")
    void testProductScenarios(ProductScenario scenario) {
        User user = TestDataLoader.getUser("standard_user");

        log.info("Starting product test for user {}", user.getUsername());
        loginPage.openLogin();
        loginPage.loginAs(user);

        String product = scenario.productName;

        switch (scenario.action) {
            case "view":
                log.info("Checking if product '{}' is displayed", product);
                productsPage.shouldHaveProduct(product);
                assertTrue(productsPage.isProductDisplayed(product),
                        "Product should be visible on Products page");
                break;

            case "addToCart":
                log.info("Adding product '{}' to cart", product);
                productsPage.addProductToCart(product);
                assertTrue(productsPage.isProductInCart(product),
                        "Product should be marked as added to cart");
                break;

            case "removeFromCart":
                log.info("Adding and then removing product '{}' from cart", product);
                productsPage.addProductToCart(product);
                assertTrue(productsPage.isProductInCart(product),
                        "Product should first be in cart");

                cartPage.removeItemFromCart(product);
                assertTrue(cartPage.isCartBadgeGone(),
                        "Cart badge should disappear after removing product");
                break;

            case "addMultiple":
                log.info("Adding multiple products to cart: {}", product);
                productsPage.addProductToCart(product);
                String badgeCount = cartPage.getCartBadgeCount();
                assertEquals("1", badgeCount,
                        "Cart badge should update correctly after adding product");
                break;

            case "viewDetails":
                log.info("Opening product details for '{}'", product);
                $$(".inventory_item_name").findBy(text(product)).click();
                detailsPage.shouldSeeProductTitle(product);
                detailsPage.backToProducts();
                productsPage.shouldBeVisible();
                break;

            default:
                log.warn("Unknown action '{}' for product '{}'", scenario.action, product);
        }

        log.info("Product test completed for product '{}'", product);
    }

    // -----------------------
    // Helper class for parameterization
    // -----------------------
    static class ProductScenario {
        final String productName;
        final String action;

        public ProductScenario(String productName, String action) {
            this.productName = productName;
            this.action = action;
        }

        @Override
        public String toString() {
            return productName + " [" + action + "]";
        }
    }
}
