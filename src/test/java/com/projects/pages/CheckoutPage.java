package com.projects.pages;

import static com.codeborne.selenide.Selenide.*;
import static com.codeborne.selenide.Condition.*;

public class CheckoutPage {

    public void fillInformation(String firstName, String lastName, String postalCode) {
        $("#first-name").setValue(firstName);
        $("#last-name").setValue(lastName);
        $("#postal-code").setValue(postalCode);
        $("#continue").click();
    }

    public void finish() {
        $("#finish").click();
    }

    public void assertOrderComplete() {
        $(".complete-header").shouldHave(text("THANK YOU FOR YOUR ORDER"));
    }

    public boolean isOrderComplete() {
        return $(".complete-header").has(text("THANK YOU FOR YOUR ORDER"));
    }

    public void shouldSeeError(String expectedError) {
        $(".error-message-container").shouldHave(text(expectedError));
    }

    public void cancel() {
        $("#cancel").click();
    }

    public boolean isOrderCompleteDisplayed() {
        return $(".complete-header").has(text("THANK YOU FOR YOUR ORDER"));
    }

    public boolean isErrorDisplayed(String expectedError) {
        return $(".error-message-container").has(text(expectedError));
    }
}
