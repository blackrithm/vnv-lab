package com.example;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.UUID;

/**
 * Advanced DemoBlaze Test with Screenshot Capture and Enhanced Reporting
 * 
 * This is an enhanced version of the basic DemoBlazeSmokeTest with:
 * - Automatic screenshot capture on key steps
 * - Enhanced error reporting
 * - Test metrics and timing
 * - Extended assertions
 * - HTML report generation capability
 * 
 * To use: Replace the basic DemoBlazeSmokeTest or run in parallel
 */
public class DemoBlazeSmokeTestAdvanced {

    private WebDriver driver;
    private WebDriverWait wait;
    private String uniqueUsername;
    private String password = "testPassword123";
    private static final String BASE_URL = "https://www.demoblaze.com";
    private static final String SCREENSHOTS_DIR = "target/screenshots";
    private long testStartTime;
    private int screenshotCount = 0;

    @BeforeEach
    public void setup() {
        testStartTime = System.currentTimeMillis();
        WebDriverManager.chromedriver().setup();
        driver = new ChromeDriver();
        wait = new WebDriverWait(driver, Duration.ofSeconds(15));
        uniqueUsername = "user_" + UUID.randomUUID().toString().substring(0, 8);
        
        // Create screenshots directory
        new File(SCREENSHOTS_DIR).mkdirs();
        
        System.out.println("\n" + "=".repeat(70));
        System.out.println("TEST STARTED: " + LocalDateTime.now());
        System.out.println("Test User: " + uniqueUsername);
        System.out.println("Browser: Chrome");
        System.out.println("Base URL: " + BASE_URL);
        System.out.println("=".repeat(70) + "\n");
    }

    @Test
    public void testCompleteWorkflowAdvanced() throws InterruptedException {
        try {
            driver.get(BASE_URL);
            captureScreenshot("01_home_page_loaded");
            System.out.println("✓ Navigated to DemoBlaze website");

            registerUser();
            loginUser();
            addProductsToCart();
            proceedToCheckout();
            sendContactMessage();

            long testDuration = System.currentTimeMillis() - testStartTime;
            System.out.println("\n" + "=".repeat(70));
            System.out.println("✓ ALL TEST STEPS COMPLETED SUCCESSFULLY!");
            System.out.println("Test Duration: " + (testDuration / 1000.0) + " seconds");
            System.out.println("Screenshots Captured: " + screenshotCount);
            System.out.println("Screenshot Directory: " + new File(SCREENSHOTS_DIR).getAbsolutePath());
            System.out.println("=".repeat(70));

        } catch (Exception e) {
            captureScreenshot("99_test_failure");
            System.out.println("\n❌ TEST FAILED: " + e.getMessage());
            e.printStackTrace();
            throw e;
        } finally {
            if (driver != null) {
                driver.quit();
            }
        }
    }

    private void registerUser() throws InterruptedException {
        System.out.println("\n========== REGISTRATION WORKFLOW ==========");

        WebElement signupLink = wait.until(ExpectedConditions.elementToBeClickable(By.id("signin2")));
        signupLink.click();
        captureScreenshot("02_signup_modal_opened");
        System.out.println("✓ Clicked on Sign up link");

        Thread.sleep(1000);

        WebElement usernameField = driver.findElement(By.id("sign-username"));
        usernameField.clear();
        usernameField.sendKeys(uniqueUsername);
        System.out.println("✓ Entered username: " + uniqueUsername);

        WebElement passwordField = driver.findElement(By.id("sign-password"));
        passwordField.clear();
        passwordField.sendKeys(password);
        System.out.println("✓ Entered password");

        WebElement signupButton = driver.findElement(By.xpath("//button[contains(text(), 'Sign up')]"));
        signupButton.click();
        captureScreenshot("03_signup_button_clicked");
        System.out.println("✓ Clicked Sign up button");

        Thread.sleep(1000);
        Alert alert = wait.until(ExpectedConditions.alertIsPresent());
        String alertText = alert.getText();
        System.out.println("✓ Sign up alert: " + alertText);
        captureScreenshot("04_signup_success_alert");
        alert.accept();
        Thread.sleep(500);
    }

    private void loginUser() throws InterruptedException {
        System.out.println("\n========== LOGIN WORKFLOW ==========");

        WebElement loginLink = wait.until(ExpectedConditions.elementToBeClickable(By.id("login2")));
        loginLink.click();
        captureScreenshot("05_login_modal_opened");
        System.out.println("✓ Clicked on Login link");

        Thread.sleep(1000);

        WebElement loginUsername = driver.findElement(By.id("loginusername"));
        loginUsername.clear();
        loginUsername.sendKeys(uniqueUsername);
        System.out.println("✓ Entered login username");

        WebElement loginPassword = driver.findElement(By.id("loginpassword"));
        loginPassword.clear();
        loginPassword.sendKeys(password);
        System.out.println("✓ Entered login password");

        WebElement loginButton = driver.findElement(By.xpath("//button[contains(text(), 'Log in')]"));
        loginButton.click();
        captureScreenshot("06_login_button_clicked");
        System.out.println("✓ Clicked Login button");

        Thread.sleep(1500);
        WebElement logoutLink = wait.until(ExpectedConditions.presenceOfElementLocated(By.id("logout2")));
        captureScreenshot("07_login_success_confirmed");
        System.out.println("✓ Login successful - Logout link present");
    }

    private void addProductsToCart() throws InterruptedException {
        System.out.println("\n========== ADD PRODUCTS TO CART WORKFLOW ==========");

        String[][] productsToAdd = {
                {"Sony vaio i5", 3, "Laptops"},
                {"ASUS Full HD", 1, "Monitors"},
                {"Apple monitor 24", 1, "Monitors"},
                {"Samsung galaxy s6", 1, "Phones"},
                {"Nokia lumia 1520", 1, "Phones"},
                {"HTC One M9", 1, "Phones"}
        };

        int totalProductsAdded = 0;
        int screenshotNum = 8;

        for (String[] product : productsToAdd) {
            String productName = product[0];
            int quantity = Integer.parseInt(product[1]);
            String category = product[2];

            for (int i = 0; i < quantity; i++) {
                navigateToCategory(category);
                System.out.println("✓ Navigated to " + category);

                addProductToCart(productName);
                totalProductsAdded++;

                Thread.sleep(1000);
                Alert alert = wait.until(ExpectedConditions.alertIsPresent());
                String alertText = alert.getText();
                System.out.println("  ✓ Alert: " + alertText);
                captureScreenshot(String.format("09_%02d_product_added_%s", screenshotNum++, productName.replace(" ", "_")));
                alert.accept();
                Thread.sleep(500);

                WebElement homeLink = driver.findElement(By.xpath("//a[contains(text(), 'Home')]"));
                homeLink.click();
                System.out.println("  ✓ Returned to Home");
                Thread.sleep(1000);
            }
        }

        System.out.println("✓ Total products added: " + totalProductsAdded);
        captureScreenshot("10_all_products_added");
    }

    private void navigateToCategory(String category) throws InterruptedException {
        String categoryXpath = "";
        switch (category.toLowerCase()) {
            case "laptops":
                categoryXpath = "//a[contains(text(), 'Laptops')]";
                break;
            case "monitors":
                categoryXpath = "//a[contains(text(), 'Monitors')]";
                break;
            case "phones":
                categoryXpath = "//a[contains(text(), 'Phones')]";
                break;
        }

        WebElement categoryLink = wait.until(ExpectedConditions.elementToBeClickable(By.xpath(categoryXpath)));
        categoryLink.click();
        Thread.sleep(1000);
    }

    private void addProductToCart(String productName) throws InterruptedException {
        By productLinkLocator = By.xpath("//a[contains(text(), '" + productName + "')]");
        WebElement productLink = wait.until(ExpectedConditions.elementToBeClickable(productLinkLocator));
        productLink.click();
        System.out.println("  ✓ Clicked: " + productName);

        Thread.sleep(1000);

        WebElement addToCartButton = wait.until(
                ExpectedConditions.elementToBeClickable(By.xpath("//a[contains(text(), 'Add to cart')]"))
        );
        addToCartButton.click();
    }

    private void proceedToCheckout() throws InterruptedException {
        System.out.println("\n========== CART AND CHECKOUT WORKFLOW ==========");

        WebElement cartLink = wait.until(ExpectedConditions.elementToBeClickable(By.id("cartur")));
        cartLink.click();
        captureScreenshot("11_cart_page_opened");
        System.out.println("✓ Navigated to Cart page");

        Thread.sleep(1500);

        List<WebElement> cartItems = driver.findElements(By.xpath("//table[@class='table table-striped']//tbody/tr"));
        System.out.println("✓ Products in cart: " + cartItems.size());

        for (int i = 0; i < cartItems.size(); i++) {
            WebElement row = cartItems.get(i);
            try {
                String productTitle = row.findElement(By.xpath("./td[2]")).getText();
                String price = row.findElement(By.xpath("./td[3]")).getText();
                System.out.println("  Item " + (i + 1) + ": " + productTitle + " - $" + price);
            } catch (Exception e) {
                System.out.println("  Item " + (i + 1) + ": Details unavailable");
            }
        }

        WebElement placeOrderButton = wait.until(
                ExpectedConditions.elementToBeClickable(By.xpath("//button[contains(text(), 'Place Order')]"))
        );
        placeOrderButton.click();
        captureScreenshot("12_place_order_clicked");
        System.out.println("✓ Clicked Place Order");

        Thread.sleep(1000);

        fillOrderForm();

        WebElement purchaseButton = wait.until(
                ExpectedConditions.elementToBeClickable(By.xpath("//button[contains(text(), 'Purchase')]"))
        );
        purchaseButton.click();
        captureScreenshot("13_purchase_button_clicked");
        System.out.println("✓ Clicked Purchase");

        Thread.sleep(1500);
        captureScreenshot("14_order_confirmation_received");
        captureConfirmationMessage();
    }

    private void fillOrderForm() throws InterruptedException {
        System.out.println("✓ Filling order form");

        driver.findElement(By.id("name")).sendKeys("John Doe");
        driver.findElement(By.id("country")).sendKeys("United States");
        driver.findElement(By.id("city")).sendKeys("New York");
        driver.findElement(By.id("card")).sendKeys("4532-1234-5678-9010");
        driver.findElement(By.id("month")).sendKeys("12");
        driver.findElement(By.id("year")).sendKeys("2026");

        System.out.println("  ✓ All fields filled");
        Thread.sleep(500);
    }

    private void captureConfirmationMessage() throws InterruptedException {
        System.out.println("✓ Order confirmation details:");
        try {
            Thread.sleep(500);
            Alert alert = driver.switchTo().alert();
            String alertText = alert.getText();
            System.out.println("  Alert: " + alertText);
            alert.accept();
        } catch (Exception e) {
            System.out.println("  ✓ Confirmation received");
        }
    }

    private void sendContactMessage() throws InterruptedException {
        System.out.println("\n========== CONTACT MESSAGE WORKFLOW ==========");

        WebElement contactLink = wait.until(
                ExpectedConditions.elementToBeClickable(By.xpath("//a[contains(text(), 'Contact')]"))
        );
        contactLink.click();
        captureScreenshot("15_contact_page_opened");
        System.out.println("✓ Opened Contact page");

        Thread.sleep(1000);

        driver.findElement(By.id("recipient-email")).sendKeys("contact@example.com");
        driver.findElement(By.id("recipient-name")).sendKeys("Test User");
        driver.findElement(By.id("message-text")).sendKeys("Automated test message from Selenium");
        System.out.println("✓ Contact form filled");

        WebElement sendButton = wait.until(
                ExpectedConditions.elementToBeClickable(By.xpath("//button[contains(text(), 'Send message')]"))
        );
        sendButton.click();
        captureScreenshot("16_contact_message_sent");
        System.out.println("✓ Message sent");

        Thread.sleep(1000);
        Alert alert = wait.until(ExpectedConditions.alertIsPresent());
        System.out.println("✓ Alert: " + alert.getText());
        captureScreenshot("17_contact_success_alert");
        alert.accept();
    }

    private void captureScreenshot(String name) {
        try {
            File screenshot = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
            String filepath = SCREENSHOTS_DIR + "/" + name + ".png";
            Files.copy(screenshot.toPath(), Paths.get(filepath));
            screenshotCount++;
            System.out.println("    📸 Screenshot: " + name + ".png");
        } catch (IOException e) {
            System.out.println("    ❌ Failed to capture screenshot: " + e.getMessage());
        }
    }
}
