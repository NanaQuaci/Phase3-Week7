package com.projects.pages;

import static com.codeborne.selenide.Selenide.*;
import static com.codeborne.selenide.Condition.*;

public class ProductDetailsPage {

    public void shouldSeeProductTitle(String productName) {
        $(".inventory_details_name").shouldHave(text(productName));
    }

    public void addToCart() {
        $("button.btn_primary.btn_inventory").click();
    }

    public void backToProducts() {
        $("#back-to-products").click();
    }
}
