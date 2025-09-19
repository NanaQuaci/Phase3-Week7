package com.projects.tests;

import com.codeborne.selenide.junit5.ScreenShooterExtension;
import com.projects.base.BaseTest;
import com.projects.pages.LoginPage;
import com.projects.util.TestDataLoader;
import com.projects.util.User;
import io.qameta.allure.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertTrue;

@Epic("Swag Labs UI Tests")
@Feature("Login Functionality")
@ExtendWith({ScreenShooterExtension.class})
public class LoginTest extends BaseTest {

    private static final Logger log = LoggerFactory.getLogger(LoginTest.class);
    private final LoginPage loginPage = new LoginPage();

    // Provide login scenarios for the parameterized test
    static Stream<LoginScenario> loginScenarios() {
        return Stream.of(
                new LoginScenario("standard_user", null, true, "Valid Login"),
                new LoginScenario("invalidUser", "invalidLogin", false, "Invalid Login"),
                new LoginScenario("locked_out_user", "lockedOutUser", false, "Locked Out User"),
                new LoginScenario("empty_username", "emptyUsername", false, "Empty Username"),
                new LoginScenario("empty_password", "emptyPassword", false, "Empty Password"),
                new LoginScenario("problem_user", null, true, "Problem User Login"),
                new LoginScenario("performance_glitch_user", null, true, "Performance Glitch User Login")
        );
    }

    @ParameterizedTest(name = "{index} - {0}")
    @MethodSource("loginScenarios")
    @DisplayName("Parameterized Login Test")
    @Severity(SeverityLevel.BLOCKER)
    @Story("Login Scenarios")
    void testLogin(LoginScenario scenario) {
        User user = TestDataLoader.getUser(scenario.username);
        String expectedError = scenario.expectedErrorKey != null
                ? TestDataLoader.getErrorMessage(scenario.expectedErrorKey)
                : null;

        log.info("Running login scenario: {}", scenario.displayName);
        loginPage.openLogin();
        loginPage.loginAs(user);

        if (scenario.shouldSucceed) {
            log.info("Verifying Products page is displayed");
            loginPage.shouldSeeProductsPage();
            assertTrue(loginPage.isProductsPageDisplayed(),
                    "Products page should be visible for scenario: " + scenario.displayName);
        } else {
            log.info("Verifying error message: {}", expectedError);
            loginPage.shouldSeeError(expectedError);
            assertTrue(loginPage.isErrorDisplayed(expectedError),
                    "Error message should be visible for scenario: " + scenario.displayName);
        }

        log.info("Completed login scenario: {}", scenario.displayName);
    }

    // Helper class for scenario data
    static class LoginScenario {
        final String username;
        final String expectedErrorKey;
        final boolean shouldSucceed;
        final String displayName;

        LoginScenario(String username, String expectedErrorKey, boolean shouldSucceed, String displayName) {
            this.username = username;
            this.expectedErrorKey = expectedErrorKey;
            this.shouldSucceed = shouldSucceed;
            this.displayName = displayName;
        }

        @Override
        public String toString() {
            return displayName;
        }
    }
}
