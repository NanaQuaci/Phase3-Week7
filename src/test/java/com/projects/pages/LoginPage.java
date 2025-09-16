package com.projects.pages;

import com.projects.util.User;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.open;

public class LoginPage {
    public void openLogin() {
        open("/"); // uses baseUrl from Configuration
    }

    public void loginAs(String username, String password) {
        $("#user-name").setValue(username);
        $("#password").setValue(password);
        $("#login-button").click();
    }

    // Overload: accept User from TestDataLoader
    public void loginAs(User user) {
        loginAs(user.getUsername(), user.getPassword());
    }

    public void shouldSeeProductsPage() {
        $("#inventory_container").shouldBe(visible);
    }

    public void shouldSeeError(String expectedError) {
        $(".error-message-container").shouldHave(text(expectedError));
    }
}
