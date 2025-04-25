package base;

import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import utils.ConfigManager;
import utils.DriverManager;
import utils.ElementManager;

import java.io.IOException;
import java.time.Duration;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Set;

import static utils.ElementManager.*;




public class AutomationMethods {



    static String browser = System.getProperty("browser"); // Default 'chrome' tarayıcısı


    public static boolean waitForElementToBeVisible(String element, int timeoutInSeconds) throws IOException {
        By xpath = ElementManager.getLocatorFromJson(element);
        try {

            WebDriver driver = DriverManager.getDriver(browser);
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(timeoutInSeconds));

            // Element görünene kadar bekle
            WebElement webElement = wait.until(ExpectedConditions.visibilityOfElementLocated(xpath));

            return webElement.isDisplayed(); // Eğer element görünüyorsa true döndür
        } catch (TimeoutException e) {
            return false;
        }
    }

    public static void hoverOverElement(String element) throws IOException {

        By xpath = ElementManager.getLocatorFromJson(element);
        WebDriver driver = DriverManager.getDriver(browser);

        WebDriverWait wait = new WebDriverWait(DriverManager.getDriver(browser), Duration.ofSeconds(10));
        WebElement webElement = wait.until(ExpectedConditions.visibilityOfElementLocated(xpath));

        Actions actions = new Actions(driver);
        actions.moveToElement(webElement).perform();
    }




    public static void switchToNewWindow() {
        WebDriver driver = DriverManager.getDriver(browser);
        String currentWindow = driver.getWindowHandle();

        Set<String> allWindows = driver.getWindowHandles();
        for (String window : allWindows) {
            if (!window.equals(currentWindow)) {
                driver.switchTo().window(window); // Yeni pencereye geçiş yap
                System.out.println("Changed window: " + driver.getTitle());
                return;
            }
        }
        System.out.println("Yeni pencere bulunamadı!");
    }



    public static void waitAndClick(String element) throws IOException {
//        By xpath = ElementManager.getLocatorFromJson(element);
//        WebDriverWait wait = new WebDriverWait(DriverManager.getDriver(browser), Duration.ofSeconds(100));
//        WebElement webElement = wait.until(ExpectedConditions.elementToBeClickable(xpath));
//        webElement.click();
        WebDriver driver = DriverManager.getDriver(); // Burada browser parametresine ihtiyaç yok!
        By locator = ElementManager.getLocatorFromJson(element);

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(15));

        try {
            WebElement webElement = wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
            wait.until(ExpectedConditions.elementToBeClickable(locator));

            // Scroll into view
            ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block: 'center'});", webElement);

            webElement.click();

            System.out.println("✅ Successfully clicked on: " + element);

        } catch (Exception e) {
            System.out.println("⚠️ Normal click failed, trying JavaScript click...");

            WebElement webElement = driver.findElement(locator);
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", webElement);
        }
    }

    public static void assertElementContainsText(String key, String expectedText) throws Exception {
        WebElement element = ElementManager.findElement(key);
        String actualText = element.getText().trim();

        if (!actualText.contains(expectedText)) {
            throw new AssertionError(" Expected text error!\nExpected: " + expectedText + "\nActual: " + actualText);
        }

        System.out.println(" Success: " + expectedText);
    }

    public static void assertElementIsClickable(String key) throws Exception {
        WebDriver driver = DriverManager.getDriver(System.getProperty("browser", "chrome"));
        By locator = ElementManager.getLocatorFromJson(key);

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

        try {
            wait.until(ExpectedConditions.elementToBeClickable(locator));
            System.out.println(" Element is clickable: " + key);
        } catch (TimeoutException e) {
            throw new AssertionError("Element is not clickable: " + key);
        }
    }

    public static WebElement findElement(String key) throws Exception {
        return ElementManager.findElement(key);
    }

    public static void sendKeysToElement(String key, String text) throws Exception {
        WebElement element = findElement(key);

        try {
            element.click();  // bazen tıklayınca aktif olur
        } catch (Exception e) {
            System.out.println("Element tıklanamadı, direkt yazılacak");
        }


        element.clear();       // Önce varsa içindeki yazıyı temizler
        element.sendKeys(text); // Sonra istediğin yazıyı yazar
        System.out.println("Text sent to element [" + key + "]: " + text);
    }



}
