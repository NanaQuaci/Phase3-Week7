package com.projects.tests;

import com.codeborne.selenide.junit5.ScreenShooterExtension;
import com.projects.base.BaseTest;
import com.projects.pages.LoginPage;
import com.projects.util.TestDataLoader;
import com.projects.util.User;
import io.qameta.allure.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

@Epic("Swag Labs UI Tests")
@Feature("Login Functionality")
@ExtendWith({ScreenShooterExtension.class})
public class LoginTest extends BaseTest {

    private final LoginPage loginPage = new LoginPage();

    @Test
    @Story("Valid Login")
    @Severity(SeverityLevel.BLOCKER)
    @Description("Verify user can login successfully with valid credentials")
    public void testValidLogin() {
        User user = TestDataLoader.getUser("standard_user");
        loginPage.openLogin();
        loginPage.loginAs(user);
        loginPage.shouldSeeProductsPage();
    }

    @Test
    @Story("Invalid Login")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Verify login fails with invalid credentials")
    public void testInvalidLogin() {
        User user = TestDataLoader.getUser("invalidUser");
        loginPage.openLogin();
        loginPage.loginAs(user);
        loginPage.shouldSeeError("Epic sadface: Username and password do not match any user in this service");
    }

    @Test
    @Story("Locked Out User")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Verify locked out user cannot login")
    public void testLockedOutUser() {
        User user = TestDataLoader.getUser("locked_out_user");
        loginPage.openLogin();
        loginPage.loginAs(user);
        loginPage.shouldSeeError("Epic sadface: Sorry, this user has been locked out.");
    }
}
