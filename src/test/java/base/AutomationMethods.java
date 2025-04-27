package base;

import annotations.LazyComponent;
import jakarta.annotation.PostConstruct;
import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import utils.DriverManager;
import utils.ElementManager;

import java.io.IOException;
import java.time.Duration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

@LazyComponent
public class AutomationMethods {

    private WebDriver driver;

    @Autowired
    private DriverManager driverManager;

    @Autowired
    private ElementManager elementManager;

    private static final Map<String, String> memoryStorage = new HashMap<>();



    private WebDriver getDriver() {
        if (driver == null) {
            driver = driverManager.getDriver(System.getProperty("browser", "chrome"));
        }
        return driver;
    }

    public boolean waitForElementToBeVisible(String element, int timeoutInSeconds) throws IOException {
        By locator = elementManager.getLocatorFromJson(element);
        try {
            WebDriverWait wait = new WebDriverWait(getDriver(), Duration.ofSeconds(timeoutInSeconds));
            WebElement webElement = wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
            return webElement.isDisplayed();
        } catch (TimeoutException e) {
            System.out.println("Element görünür değil: " + element);
            return false;
        }
    }

    public void hoverOverElement(String element) throws IOException {
        By locator = elementManager.getLocatorFromJson(element);
        WebDriverWait wait = new WebDriverWait(getDriver(), Duration.ofSeconds(10));
        WebElement webElement = wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
        new Actions(getDriver()).moveToElement(webElement).perform();
    }

    public void switchToNewWindow() {
        WebDriver driver = getDriver();
        String currentWindowHandle = driver.getWindowHandle();
        for (String windowHandle : driver.getWindowHandles()) {
            if (!windowHandle.equals(currentWindowHandle)) {
                driver.switchTo().window(windowHandle);
                break;
            }
        }
    }

    public void waitAndClick(String element) throws IOException {
        By locator = elementManager.getLocatorFromJson(element);
        WebDriverWait wait = new WebDriverWait(getDriver(), Duration.ofSeconds(10));
        WebElement webElement = wait.until(ExpectedConditions.elementToBeClickable(locator));
        webElement.click();
    }

    public void assertElementContainsText(String key, String expectedText) throws Exception {
        WebElement element = elementManager.findElement(key);
        String actualText = element.getText().trim();
        if (!actualText.contains(expectedText)) {
            throw new AssertionError("Expected text error!\nExpected: " + expectedText + "\nActual: " + actualText);
        }
        System.out.println("Success: " + expectedText);
    }

    public void assertElementIsClickable(String key) throws Exception {
        By locator = elementManager.getLocatorFromJson(key);
        WebDriverWait wait = new WebDriverWait(getDriver(), Duration.ofSeconds(10));
        try {
            wait.until(ExpectedConditions.elementToBeClickable(locator));
            System.out.println("Element is clickable: " + key);
        } catch (TimeoutException e) {
            throw new AssertionError("Element is not clickable: " + key);
        }
    }

    public WebElement findElement(String key) throws Exception {
        return elementManager.findElement(key);
    }

    public void scrollToElementInsideScrollableDiv(String targetElementKey) throws Exception {
        WebElement targetElement = findElement(targetElementKey);
        JavascriptExecutor jsExecutor = (JavascriptExecutor) getDriver();
        jsExecutor.executeScript("arguments[0].scrollIntoView({behavior: 'smooth', block: 'center'});", targetElement);
    }





    public void sendKeysToElement(String key, String text) throws Exception {
        WebElement element = findElement(key);
        try {
            element.click();
        } catch (Exception e) {
            System.out.println("Element tıklanamadı, direkt yazılacak");
        }
        element.clear();
        element.sendKeys(text);
        System.out.println("Text sent to element [" + key + "]: " + text);
    }
    public java.util.List<WebElement> findElements(String key) throws Exception {
        By locator = elementManager.getLocatorFromJson(key);
        WebDriverWait wait = new WebDriverWait(getDriver(), Duration.ofSeconds(10));
        wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(locator)); // Önce elementlerin DOM'da var olduğundan emin oluyoruz
        return getDriver().findElements(locator);
    }

    public void assertProductTitlesContainOnlyColor(String expectedColor) throws Exception {
        List<WebElement> productTitles = findElements("PRODUCTS_COLOR_LIST");
        for (WebElement title : productTitles) {
            String titleText = title.getText().toLowerCase();
            if (!titleText.contains(expectedColor.toLowerCase())) {
                throw new AssertionError("Ürün başlığında beklenen renk bulunamadı! Başlık: " + titleText);
            }
        }
    }

    public void assertAllProductTitlesContainExpectedStorage(String expectedStorage) throws Exception {
        List<WebElement> productTitles = findElements("PRODUCTS_STORAGE_LIST");
        for (WebElement title : productTitles) {
            String titleText = title.getText().toLowerCase();
            if (!titleText.contains(expectedStorage.toLowerCase())) {
                throw new AssertionError("Ürün başlığında beklenen hafıza bulunamadı! Başlık: " + titleText);
            }
        }
    }



    public void storeElementText(String key, String elementText) {
        memoryStorage.put(key, elementText);
        System.out.println("Stored text with key [" + key + "]: " + elementText);
    }

    public void assertElementIsSelected(String key) throws Exception {
        WebElement element = findElement(key); // JSON'dan locator alıyor
        String classAttribute = element.getAttribute("class");

        if (classAttribute == null || !classAttribute.contains("selected")) {
            throw new AssertionError("Element '" + key + "' is not selected!");
        }
    }


    /**
     * Daha sonra saklanan text'i bu methodla çağırayoruz, case'ler için lazım
     */
    public String getStoredText(String key) {
        return memoryStorage.get(key);
    }
}
