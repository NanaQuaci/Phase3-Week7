package com.projects.pages;

import static com.codeborne.selenide.Selenide.*;
import static com.codeborne.selenide.Condition.*;

public class CartPage {

    public void addItemToCart(String productName) {
        $$("div.inventory_item")
                .findBy(text(productName))
                .$("button.btn_inventory")
                .click();
    }

    public void removeItemFromCart(String productName) {
        $$("div.inventory_item, .cart_item")
                .findBy(text(productName))
                .$("button")
                .click();
    }

    public void goToCart() {
        $("#shopping_cart_container a").click();
    }

    public boolean isItemInCart(String productName) {
        return $$(".cart_item").findBy(text(productName)).is(visible);
    }

    public String getCartBadgeCount() {
        return $("#shopping_cart_container .shopping_cart_badge").getText();
    }

    public boolean isCartBadgeGone() {
        return $$("#shopping_cart_container .shopping_cart_badge").isEmpty();
    }

    public void checkout() {
        $("#checkout").click();
    }

    public void shouldBeVisible() {
        $(".cart_list").should(appear);
    }
}
