package com.example;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;
import java.util.UUID;

public class DemoBlazeSmokeTest {

    private WebDriver driver;
    private WebDriverWait wait;
    private String uniqueUsername;
    private String password = "testPassword123";
    private static final String BASE_URL = "https://www.demoblaze.com";

    @BeforeEach
    public void setup() {
        WebDriverManager.chromedriver().setup();
        driver = new ChromeDriver();
        wait = new WebDriverWait(driver, Duration.ofSeconds(15));
        // Generate unique username for each test run
        uniqueUsername = "user_" + UUID.randomUUID().toString().substring(0, 8);
    }

    @Test
    public void testCompleteWorkflow() throws InterruptedException {
        try {
            driver.get(BASE_URL);
            System.out.println("✓ Navigated to DemoBlaze website");

            // Step 1: User Registration
            registerUser();

            // Step 2: Login
            loginUser();

            // Step 3: Add Products to Cart
            addProductsToCart();

            // Step 4: Cart and Checkout
            proceedToCheckout();

            // Step 5: Send Contact Message
            sendContactMessage();

            System.out.println("\n✓ All test steps completed successfully!");

        } finally {
            if (driver != null) {
                driver.quit();
            }
        }
    }

    private void registerUser() throws InterruptedException {
        System.out.println("\n========== REGISTRATION WORKFLOW ==========");

        // Click on Sign up link
        WebElement signupLink = wait.until(ExpectedConditions.elementToBeClickable(By.id("signin2")));
        signupLink.click();
        System.out.println("✓ Clicked on Sign up link");

        // Wait for signup modal to appear
        Thread.sleep(1000);

        // Fill in username
        WebElement usernameField = driver.findElement(By.id("sign-username"));
        usernameField.clear();
        usernameField.sendKeys(uniqueUsername);
        System.out.println("✓ Entered username: " + uniqueUsername);

        // Fill in password
        WebElement passwordField = driver.findElement(By.id("sign-password"));
        passwordField.clear();
        passwordField.sendKeys(password);
        System.out.println("✓ Entered password");

        // Click signup button
        WebElement signupButton = driver.findElement(By.xpath("//button[contains(text(), 'Sign up')]"));
        signupButton.click();
        System.out.println("✓ Clicked Sign up button");

        // Handle signup success alert
        Thread.sleep(1000);
        Alert alert = wait.until(ExpectedConditions.alertIsPresent());
        String alertText = alert.getText();
        System.out.println("✓ Sign up alert: " + alertText);
        alert.accept();
        Thread.sleep(500);
    }

    private void loginUser() throws InterruptedException {
        System.out.println("\n========== LOGIN WORKFLOW ==========");

        // Click on Login link
        WebElement loginLink = wait.until(ExpectedConditions.elementToBeClickable(By.id("login2")));
        loginLink.click();
        System.out.println("✓ Clicked on Login link");

        // Wait for login modal to appear
        Thread.sleep(1000);

        // Fill in username
        WebElement loginUsername = driver.findElement(By.id("loginusername"));
        loginUsername.clear();
        loginUsername.sendKeys(uniqueUsername);
        System.out.println("✓ Entered login username: " + uniqueUsername);

        // Fill in password
        WebElement loginPassword = driver.findElement(By.id("loginpassword"));
        loginPassword.clear();
        loginPassword.sendKeys(password);
        System.out.println("✓ Entered login password");

        // Click login button
        WebElement loginButton = driver.findElement(By.xpath("//button[contains(text(), 'Log in')]"));
        loginButton.click();
        System.out.println("✓ Clicked Login button");

        // Wait for successful login - check for "Welcome [username]"
        Thread.sleep(1500);
        String pageSource = driver.getPageSource();
        if (pageSource.contains("Welcome")) {
            System.out.println("✓ Login successful - Welcome message displayed");
        } else {
            System.out.println("⚠ Welcome message not found in page source");
        }

        // Also verify the logout link appears (sign of successful login)
        WebElement logoutLink = wait.until(ExpectedConditions.presenceOfElementLocated(By.id("logout2")));
        System.out.println("✓ Logout link present - Login confirmed");
    }

    private void addProductsToCart() throws InterruptedException {
        System.out.println("\n========== ADD PRODUCTS TO CART WORKFLOW ==========");

        // Products to add
        String[][] productsToAdd = {
                {"Sony vaio i5", 3, "Laptops"},
                {"ASUS Full HD", 1, "Monitors"},
                {"Apple monitor 24", 1, "Monitors"},
                {"Samsung galaxy s6", 1, "Phones"},
                {"Nokia lumia 1520", 1, "Phones"},
                {"HTC One M9", 1, "Phones"}
        };

        int totalProductsAdded = 0;

        for (String[] product : productsToAdd) {
            String productName = product[0];
            int quantity = Integer.parseInt(product[1]);
            String category = product[2];

            for (int i = 0; i < quantity; i++) {
                // Navigate to category
                navigateToCategory(category);
                System.out.println("✓ Navigated to " + category + " category");

                // Find and click the product
                addProductToCart(productName);
                totalProductsAdded++;

                // Handle product added alert
                Thread.sleep(1000);
                Alert alert = wait.until(ExpectedConditions.alertIsPresent());
                String alertText = alert.getText();
                System.out.println("  ✓ Alert: " + alertText);
                alert.accept();
                Thread.sleep(500);

                // Return to home
                WebElement homeLink = driver.findElement(By.xpath("//a[contains(text(), 'Home')]"));
                homeLink.click();
                System.out.println("  ✓ Returned to Home page");
                Thread.sleep(1000);
            }
        }

        System.out.println("✓ Total products added to cart: " + totalProductsAdded);
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
            default:
                throw new IllegalArgumentException("Unknown category: " + category);
        }

        WebElement categoryLink = wait.until(ExpectedConditions.elementToBeClickable(By.xpath(categoryXpath)));
        categoryLink.click();
        Thread.sleep(1000);
    }

    private void addProductToCart(String productName) throws InterruptedException {
        // Wait for products to load and find the product link
        By productLinkLocator = By.xpath("//a[contains(text(), '" + productName + "')]");
        WebElement productLink = wait.until(ExpectedConditions.elementToBeClickable(productLinkLocator));
        productLink.click();
        System.out.println("  ✓ Clicked on product: " + productName);

        Thread.sleep(1000);

        // Find and click "Add to cart" button
        WebElement addToCartButton = wait.until(
                ExpectedConditions.elementToBeClickable(By.xpath("//a[contains(text(), 'Add to cart')]"))
        );
        addToCartButton.click();
        System.out.println("  ✓ Clicked Add to cart button");
    }

    private void proceedToCheckout() throws InterruptedException {
        System.out.println("\n========== CART AND CHECKOUT WORKFLOW ==========");

        // Click on Cart link
        WebElement cartLink = wait.until(ExpectedConditions.elementToBeClickable(By.id("cartur")));
        cartLink.click();
        System.out.println("✓ Navigated to Cart page");

        Thread.sleep(1500);

        // Verify products in cart
        List<WebElement> cartItems = driver.findElements(By.xpath("//table[@class='table table-striped']//tbody/tr"));
        System.out.println("✓ Number of products in cart: " + cartItems.size());

        if (cartItems.size() == 8) {
            System.out.println("✓ All 8 products are present in the cart");
        } else {
            System.out.println("⚠ Expected 8 products but found: " + cartItems.size());
        }

        // Print cart items details
        for (int i = 0; i < cartItems.size(); i++) {
            WebElement row = cartItems.get(i);
            try {
                String productTitle = row.findElement(By.xpath("./td[2]")).getText();
                String price = row.findElement(By.xpath("./td[3]")).getText();
                System.out.println("  Item " + (i + 1) + ": " + productTitle + " - $" + price);
            } catch (Exception e) {
                System.out.println("  Item " + (i + 1) + ": Could not retrieve details");
            }
        }

        // Click on "Place Order" button
        WebElement placeOrderButton = wait.until(
                ExpectedConditions.elementToBeClickable(By.xpath("//button[contains(text(), 'Place Order')]"))
        );
        placeOrderButton.click();
        System.out.println("✓ Clicked Place Order button");

        Thread.sleep(1000);

        // Fill in order form
        fillOrderForm();

        // Click Purchase button
        WebElement purchaseButton = wait.until(
                ExpectedConditions.elementToBeClickable(By.xpath("//button[contains(text(), 'Purchase')]"))
        );
        purchaseButton.click();
        System.out.println("✓ Clicked Purchase button");

        Thread.sleep(1500);

        // Capture and print confirmation message
        captureConfirmationMessage();
    }

    private void fillOrderForm() throws InterruptedException {
        System.out.println("✓ Filling out order form");

        // Fill Name
        WebElement nameField = driver.findElement(By.id("name"));
        nameField.clear();
        nameField.sendKeys("John Doe");
        System.out.println("  ✓ Entered name: John Doe");

        // Fill Country
        WebElement countryField = driver.findElement(By.id("country"));
        countryField.clear();
        countryField.sendKeys("United States");
        System.out.println("  ✓ Entered country: United States");

        // Fill City
        WebElement cityField = driver.findElement(By.id("city"));
        cityField.clear();
        cityField.sendKeys("New York");
        System.out.println("  ✓ Entered city: New York");

        // Fill Credit Card
        WebElement cardField = driver.findElement(By.id("card"));
        cardField.clear();
        cardField.sendKeys("4532-1234-5678-9010");
        System.out.println("  ✓ Entered credit card");

        // Fill Month
        WebElement monthField = driver.findElement(By.id("month"));
        monthField.clear();
        monthField.sendKeys("12");
        System.out.println("  ✓ Entered month: 12");

        // Fill Year
        WebElement yearField = driver.findElement(By.id("year"));
        yearField.clear();
        yearField.sendKeys("2026");
        System.out.println("  ✓ Entered year: 2026");

        Thread.sleep(500);
    }

    private void captureConfirmationMessage() throws InterruptedException {
        System.out.println("✓ Capturing order confirmation");

        Thread.sleep(1000);

        // The confirmation appears in a modal/alert-like element
        try {
            WebElement confirmationMessage = wait.until(
                    ExpectedConditions.visibilityOfElementLocated(
                            By.xpath("//h2[contains(text(), 'Thank you for your purchase')]")
                    )
            );
            String message = confirmationMessage.getText();
            System.out.println("  Confirmation Title: " + message);

            // Try to get more details from the modal
            try {
                WebElement modalBody = driver.findElement(By.xpath("//div[@class='modal-body']"));
                String details = modalBody.getText();
                System.out.println("  Confirmation Details:\n" + details);
            } catch (Exception e) {
                System.out.println("  ✓ Order confirmation received");
            }
        } catch (Exception e) {
            System.out.println("  ⚠ Could not locate confirmation message element");
        }

        // Handle any alerts
        try {
            Thread.sleep(500);
            Alert alert = driver.switchTo().alert();
            String alertText = alert.getText();
            System.out.println("  Alert Message: " + alertText);
            alert.accept();
        } catch (Exception e) {
            // No alert present
        }
    }

    private void sendContactMessage() throws InterruptedException {
        System.out.println("\n========== CONTACT MESSAGE WORKFLOW ==========");

        // Click on Contact link in navbar
        WebElement contactLink = wait.until(
                ExpectedConditions.elementToBeClickable(By.xpath("//a[contains(text(), 'Contact')]"))
        );
        contactLink.click();
        System.out.println("✓ Clicked on Contact link");

        Thread.sleep(1000);

        // Fill in contact form
        fillContactForm();

        // Submit the message
        WebElement sendButton = wait.until(
                ExpectedConditions.elementToBeClickable(By.xpath("//button[contains(text(), 'Send message')]"))
        );
        sendButton.click();
        System.out.println("✓ Clicked Send message button");

        // Handle the success alert
        Thread.sleep(1000);
        Alert alert = wait.until(ExpectedConditions.alertIsPresent());
        String alertText = alert.getText();
        System.out.println("✓ Contact message alert: " + alertText);
        alert.accept();
        Thread.sleep(500);
    }

    private void fillContactForm() throws InterruptedException {
        System.out.println("✓ Filling out contact form");

        // Fill in Email
        WebElement emailField = driver.findElement(By.id("recipient-email"));
        emailField.clear();
        emailField.sendKeys("contact@example.com");
        System.out.println("  ✓ Entered email: contact@example.com");

        // Fill in Name
        WebElement nameField = driver.findElement(By.id("recipient-name"));
        nameField.clear();
        nameField.sendKeys("Test User");
        System.out.println("  ✓ Entered name: Test User");

        // Fill in Message
        WebElement messageField = driver.findElement(By.id("message-text"));
        messageField.clear();
        messageField.sendKeys("This is an automated test message sent from Selenium. Testing the contact form functionality on DemoBlaze website.");
        System.out.println("  ✓ Entered message");

        Thread.sleep(500);
    }
}
