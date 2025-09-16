package com.projects.pages;

import static com.codeborne.selenide.Selenide.*;
import static com.codeborne.selenide.Condition.*;

public class ProductsPage {
    public void shouldHaveProduct(String productName) {
        $$("div.inventory_item").findBy(text(productName)).should(appear);
    }

    public void addProductToCart(String productName) {
        $$("div.inventory_item")
                .findBy(text(productName))
                .$("button.btn_inventory")
                .click();
    }

    public void openCart() { $("#shopping_cart_container a").click(); }
}
