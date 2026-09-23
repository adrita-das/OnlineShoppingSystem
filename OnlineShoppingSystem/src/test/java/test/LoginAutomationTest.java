package test;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class LoginAutomationTest {

    private static final String BASE_URL =
            "http://localhost:8080";

    @Test
    void loginToProductTest() throws InterruptedException {

        // Setup ChromeDriver
        WebDriverManager.chromedriver().setup();

        ChromeOptions options = new ChromeOptions();

        WebDriver driver = new ChromeDriver(options);

        try {

            // Open Login page
            driver.get(BASE_URL + "/login");

            Thread.sleep(2000);

            // Enter email
            driver.findElement(By.id("email"))
                    .sendKeys("adrita@gmail.com");

            Thread.sleep(1000);

            // Enter password
            driver.findElement(By.id("password"))
                    .sendKeys("123456789a");

            Thread.sleep(1000);

            // Click Login button
            driver.findElement(By.id("loginBtn"))
                    .click();

            Thread.sleep(3000);

            // Verify successful login
            String currentUrl = driver.getCurrentUrl();

            assertFalse(
                    currentUrl.contains("/products"),
                    "Login did not navigate to Product page"
            );

            Thread.sleep(2000);

        } finally {

            driver.quit();
        }
    }
}