package com.projects.tests;

import com.codeborne.selenide.junit5.ScreenShooterExtension;
import com.projects.base.BaseTest;
import com.projects.pages.LoginPage;
import com.projects.util.TestDataLoader;
import com.projects.util.User;
import io.qameta.allure.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.junit.jupiter.api.Assertions.*;

@Epic("Swag Labs UI Tests")
@Feature("Login Functionality")
@ExtendWith({ScreenShooterExtension.class})
public class LoginTest extends BaseTest {

    private static final Logger log = LoggerFactory.getLogger(LoginTest.class);
    private final LoginPage loginPage = new LoginPage();

    @Test
    @Story("Valid Login")
    @Severity(SeverityLevel.BLOCKER)
    @Description("Verify user can login successfully with valid credentials")
    public void testValidLogin() {
        User user = TestDataLoader.getUser("standard_user");
        log.info("Opening login page for valid login test");
        loginPage.openLogin();

        log.info("Logging in as user: {}", user.getUsername());
        loginPage.loginAs(user);

        log.info("Verifying that Products page is displayed");
        loginPage.shouldSeeProductsPage();

        log.info("Valid login test completed successfully");
        assertTrue(loginPage.isProductsPageDisplayed(),
                "Products page should be visible after login");
    }

    @Test
    @Story("Invalid Login")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Verify login fails with invalid credentials")
    public void testInvalidLogin() {
        User user = TestDataLoader.getUser("invalidUser");
        log.info("Opening login page for invalid login test");
        loginPage.openLogin();

        log.info("Attempting login with invalid user: {}", user.getUsername());
        loginPage.loginAs(user);

        log.info("Checking error message is displayed");
        loginPage.shouldSeeError("Epic sadface: Username and password do not match any user in this service");

        log.info("Invalid login test completed successfully");
        assertTrue(loginPage.isErrorDisplayed("Epic sadface: Username and password do not match any user in this service"),
                "Error message should be visible for invalid login");
    }

    @Test
    @Story("Locked Out User")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Verify locked out user cannot login")
    public void testLockedOutUser() {
        User user = TestDataLoader.getUser("locked_out_user");
        log.info("Opening login page for locked out user test");
        loginPage.openLogin();

        log.info("Attempting login with locked out user: {}", user.getUsername());
        loginPage.loginAs(user);

        log.info("Checking locked out error message is displayed");
        loginPage.shouldSeeError("Epic sadface: Sorry, this user has been locked out.");

        log.info("Locked out user login test completed successfully");
        assertTrue(loginPage.isErrorDisplayed("Epic sadface: Sorry, this user has been locked out."),
                "Error message should be visible for locked out user");
    }
}
