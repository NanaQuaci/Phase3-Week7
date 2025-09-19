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

        assertTrue(loginPage.isProductsPageDisplayed(),
                "Products page should be visible after login");
    }

    @Test
    @Story("Invalid Login")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Verify login fails with invalid credentials")
    public void testInvalidLogin() {
        User user = TestDataLoader.getUser("invalidUser");
        String expectedError = TestDataLoader.getErrorMessage("invalidCredentials");

        log.info("Opening login page for invalid login test");
        loginPage.openLogin();

        log.info("Attempting login with invalid user: {}", user.getUsername());
        loginPage.loginAs(user);

        log.info("Checking error message is displayed");
        loginPage.shouldSeeError(expectedError);

        assertTrue(loginPage.isErrorDisplayed(expectedError),
                "Error message should be visible for invalid login");
    }

    @Test
    @Story("Locked Out User")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Verify locked out user cannot login")
    public void testLockedOutUser() {
        User user = TestDataLoader.getUser("locked_out_user");
        String expectedError = TestDataLoader.getErrorMessage("lockedOut");

        log.info("Opening login page for locked out user test");
        loginPage.openLogin();

        log.info("Attempting login with locked out user: {}", user.getUsername());
        loginPage.loginAs(user);

        log.info("Checking locked out error message is displayed");
        loginPage.shouldSeeError(expectedError);

        assertTrue(loginPage.isErrorDisplayed(expectedError),
                "Error message should be visible for locked out user");
    }

    @Test
    @Story("Empty Username")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Verify login fails when username field is empty")
    public void testEmptyUsername() {
        User user = TestDataLoader.getUser("empty_username");
        String expectedError = TestDataLoader.getErrorMessage("emptyUsername");

        log.info("Opening login page for empty username test");
        loginPage.openLogin();

        log.info("Attempting login with empty username and valid password");
        loginPage.loginAs(user);

        log.info("Checking error message is displayed");
        loginPage.shouldSeeError(expectedError);

        assertTrue(loginPage.isErrorDisplayed(expectedError),
                "Error message should be visible when username is empty");
    }

    @Test
    @Story("Empty Password")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Verify login fails when password field is empty")
    public void testEmptyPassword() {
        User user = TestDataLoader.getUser("empty_password");
        String expectedError = TestDataLoader.getErrorMessage("emptyPassword");

        log.info("Opening login page for empty password test");
        loginPage.openLogin();

        log.info("Attempting login with valid username and empty password");
        loginPage.loginAs(user);

        log.info("Checking error message is displayed");
        loginPage.shouldSeeError(expectedError);

        assertTrue(loginPage.isErrorDisplayed(expectedError),
                "Error message should be visible when password is empty");
    }

    @Test
    @Story("Problem User Login")
    @Severity(SeverityLevel.NORMAL)
    @Description("Verify Problem User can login successfully (even with UI quirks)")
    public void testProblemUserLogin() {
        User user = TestDataLoader.getUser("problem_user");

        log.info("Opening login page for problem user test");
        loginPage.openLogin();

        log.info("Logging in as problem user: {}", user.getUsername());
        loginPage.loginAs(user);

        assertTrue(loginPage.isProductsPageDisplayed(),
                "Products page should still load for problem user");
    }

    @Test
    @Story("Performance Glitch User Login")
    @Severity(SeverityLevel.NORMAL)
    @Description("Verify Performance Glitch User can login successfully even if slower")
    public void testPerformanceGlitchUserLogin() {
        User user = TestDataLoader.getUser("performance_glitch_user");

        log.info("Opening login page for performance glitch user test");
        loginPage.openLogin();

        log.info("Logging in as performance glitch user: {}", user.getUsername());
        loginPage.loginAs(user);

        loginPage.shouldSeeProductsPage();

        assertTrue(loginPage.isProductsPageDisplayed(),
                "Products page should load for performance glitch user, even if delayed");
    }
}
