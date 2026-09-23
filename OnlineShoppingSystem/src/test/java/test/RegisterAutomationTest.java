package test;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import static org.junit.jupiter.api.Assertions.*;

public class RegisterAutomationTest {

    private static final String BASE_URL = "http://localhost:8080";

    private WebDriver createDriver() {

        WebDriverManager.chromedriver().setup();

        ChromeOptions options = new ChromeOptions();

        WebDriver driver = new ChromeDriver(options);

        driver.manage().window().maximize();

        return driver;
    }

    private void register(
            WebDriver driver,
            String name,
            String email,
            String password,
            String confirmPassword
    ) throws InterruptedException {

        // Open Register page
        driver.get(BASE_URL + "/register");

        Thread.sleep(1500);

        // Enter Name
        driver.findElement(By.id("name"))
                .sendKeys(name);

        Thread.sleep(1000);

        // Enter Email
        driver.findElement(By.id("email"))
                .sendKeys(email);

        Thread.sleep(1000);

        // Enter Password
        driver.findElement(By.id("password"))
                .sendKeys(password);

        Thread.sleep(1000);

        // Enter Confirm Password
        driver.findElement(By.id("confirmPassword"))
                .sendKeys(confirmPassword);

        Thread.sleep(1000);

        // Click Register
        driver.findElement(By.id("registerBtn"))
                .click();

        // Wait so you can see the result
        Thread.sleep(3000);
    }


    // ---------------------------------------------------------
    // TEST 1: Password below minimum
    // ---------------------------------------------------------

    @Test
    void password7CharsBelowMinimumTest() throws InterruptedException {

        WebDriver driver = createDriver();

        try {

            register(
                    driver,
                    "Test User",
                    "newuser1@example.com",
                    "Abc1234",
                    "Abc1234"
            );

            WebElement error =
                    driver.findElement(By.id("errorMessage"));

            assertTrue(error.isDisplayed());

            assertTrue(
                    error.getText().contains("at least")
            );

            Thread.sleep(2000);

        } finally {

            driver.quit();
        }
    }


    // ---------------------------------------------------------
    // TEST 2: Password exactly 8 characters
    // ---------------------------------------------------------

    @Test
    void password8CharsAtMinimumTest() throws InterruptedException {

        WebDriver driver = createDriver();

        try {

            register(
                    driver,
                    "Test User",
                    "newuser2@example.com",
                    "Abc12345",
                    "Abc12345"
            );

            Thread.sleep(2000);

            // If an error message exists,
            // it should not be a minimum-length error.
            if (!driver.findElements(
                    By.id("errorMessage")
            ).isEmpty()) {

                WebElement error =
                        driver.findElement(By.id("errorMessage"));

                assertFalse(
                        error.getText().contains("at least")
                );
            }

        } finally {

            driver.quit();
        }
    }


    // ---------------------------------------------------------
    // TEST 3: Password exactly 20 characters
    // ---------------------------------------------------------

    @Test
    void password20CharsAtMaximumTest() throws InterruptedException {

        WebDriver driver = createDriver();

        try {

            register(
                    driver,
                    "Test User",
                    "newuser3@example.com",
                    "Abcdefgh12345678901A",
                    "Abcdefgh12345678901A"
            );

            Thread.sleep(2000);

            if (!driver.findElements(
                    By.id("errorMessage")
            ).isEmpty()) {

                WebElement error =
                        driver.findElement(By.id("errorMessage"));

                assertFalse(
                        error.getText().contains("exceed")
                );
            }

        } finally {

            driver.quit();
        }
    }


    // ---------------------------------------------------------
    // TEST 4: Password above maximum
    // ---------------------------------------------------------

    @Test
    void password21CharsAboveMaximumTest() throws InterruptedException {

        WebDriver driver = createDriver();

        try {

            register(
                    driver,
                    "Test User",
                    "newuser4@example.com",
                    "Abcdefgh12345678901AB",
                    "Abcdefgh12345678901AB"
            );

            WebElement error =
                    driver.findElement(By.id("errorMessage"));

            assertTrue(error.isDisplayed());

            assertTrue(
                    error.getText().contains("exceed")
            );

            Thread.sleep(2000);

        } finally {

            driver.quit();
        }
    }


    // ---------------------------------------------------------
    // TEST 5: Empty name
    // ---------------------------------------------------------

    @Test
    void emptyNameTest() throws InterruptedException {

        WebDriver driver = createDriver();

        try {

            register(
                    driver,
                    "",
                    "newuser5@example.com",
                    "Abc12345",
                    "Abc12345"
            );

            WebElement error =
                    driver.findElement(By.id("errorMessage"));

            assertTrue(error.isDisplayed());

            assertTrue(
                    error.getText().contains("full name")
            );

            Thread.sleep(2000);

        } finally {

            driver.quit();
        }
    }


    // ---------------------------------------------------------
    // TEST 6: Invalid email
    // ---------------------------------------------------------

    @Test
    void malformedEmailTest() throws InterruptedException {

        WebDriver driver = createDriver();

        try {

            register(
                    driver,
                    "Test User",
                    "not-an-email",
                    "Abc12345",
                    "Abc12345"
            );

            WebElement error =
                    driver.findElement(By.id("errorMessage"));

            assertTrue(error.isDisplayed());

            assertTrue(
                    error.getText().contains("valid email")
            );

            Thread.sleep(2000);

        } finally {

            driver.quit();
        }
    }


    // ---------------------------------------------------------
    // TEST 7: Password mismatch
    // ---------------------------------------------------------

    @Test
    void mismatchedPasswordsTest() throws InterruptedException {

        WebDriver driver = createDriver();

        try {

            register(
                    driver,
                    "Test User",
                    "newuser6@example.com",
                    "Abc12345",
                    "Different123"
            );

            WebElement error =
                    driver.findElement(By.id("errorMessage"));

            assertTrue(error.isDisplayed());

            assertTrue(
                    error.getText().contains("do not match")
            );

            Thread.sleep(2000);

        } finally {

            driver.quit();
        }
    }


    // ---------------------------------------------------------
    // TEST 8: Sign in link
    // ---------------------------------------------------------

    @Test
    void signInLinkTest() throws InterruptedException {

        WebDriver driver = createDriver();

        try {

            driver.get(BASE_URL + "/register");

            Thread.sleep(2000);

            driver.findElement(By.id("signInLink"))
                    .click();

            Thread.sleep(3000);

            assertTrue(
                    driver.getCurrentUrl().endsWith("/login")
            );

            Thread.sleep(2000);

        } finally {

            driver.quit();
        }
    }
}