package stepDefinitions;

import base.AutomationMethods;
import io.cucumber.java.en.*;
import org.junit.jupiter.api.Assertions;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import utils.ConfigManager;

import utils.DriverManager;



import java.io.IOException;

import static base.AutomationMethods.*;
import static utils.ElementManager.findElement;

public class GeneralSteps {

    public static boolean isElementVisibilityWithSize(String elementKey) throws Exception {
        WebElement element = findElement(elementKey);
        return element.isDisplayed() && element.getSize().getHeight() > 0 && element.getSize().getWidth() > 0;
    }
    private static int savedNumber = -1;  // Class seviyesinde değişken, her test adımında erişilir



    @Given("Go to url")
    public void go_to_url() throws Exception {
        String browser = System.getProperty("browser"); // Default 'chrome' tarayıcısı

        // Tarayıcıyı parametre olarak DriverManager'a iletmek
        DriverManager.getDriver(browser).get(ConfigManager.getProperty("url"));
        waitForElementToBeVisible("CEREZLERI_KABUL_ET_BUTON",5);
    }

    @And("Click {string}")
    public void clickElement(String element) throws Exception {

        waitAndClick(element);
    }

    @And("Hover {string}")
    public void hover(String element) throws IOException {
        hoverOverElement(element);
    }

    @And("Switch new window")
    public void switchNewWindow() {
        switchToNewWindow();
    }

    @And("Assert that {string} element is visible")
    public void assert_that_element_is_visible(String element) throws Exception {
        boolean isVisible = isElementVisibilityWithSize(element);
        Assertions.assertTrue(isVisible, "Element is not visible: " + element);
    }

    @And("Wait {int} seconds")
    public void wait_seconds(int seconds) throws InterruptedException {
        System.out.println( "Waiting" + seconds + "seconds");
        Thread.sleep(seconds * 1000L);
        System.out.println("Waiting ended.");
    }

    @And("Assert that element {string} contains text {string}")
    public void assertElementContainsTextStep(String key, String expectedText) throws Exception {
        assertElementContainsText(key, expectedText);
    }

    @And("Assert that {string} element is clickable")
    public void assertThatElementIsClickable(String key) throws Exception {
        AutomationMethods.assertElementIsClickable(key);
    }

    @And("Assert that previous element number equal {string} this number")
    public void assertThatEqualNumbers(String key) throws Exception {
        WebElement element = AutomationMethods.findElement(key);
        String text = element.getText();

        String numberOnly = text.replaceAll("[^0-9]", "");

        if (numberOnly.isEmpty()) {
            throw new Exception("Element içinde sayı bulunamadı: " + key);
        }

        int currentNumber = Integer.parseInt(numberOnly);
        System.out.println("Current number: " + currentNumber);

        if (savedNumber != currentNumber) {
            throw new AssertionError("Numbers are NOT equal! Saved: " + savedNumber + ", Current: " + currentNumber);
        } else {
            System.out.println("Numbers are equal! (" + savedNumber + ")");
        }
    }

    @And("Save number from element {string}")
    public void saveNumberFromElement(String key) throws Exception {
        WebElement element = AutomationMethods.findElement(key);
        String text = element.getText();

        // İçerikten sadece rakamları almak
        String numberOnly = text.replaceAll("[^0-9]", "");

        if (numberOnly.isEmpty()) {
            throw new Exception("Element içinde sayı bulunamadı: " + key);
        }

        savedNumber = Integer.parseInt(numberOnly);
        System.out.println("Saved number: " + savedNumber);
    }

    @And("Write {string} to {string} input field")
    public void sendKeysToInputField(String text, String key) throws Exception {
        AutomationMethods.sendKeysToElement(key, text);
    }

    @And("Assert that number of {string} element is greater than or equal {string}")
    public void assertThatElementNumberIsGreaterThanValue(String elementKey, String expectedNumberStr) throws Exception {
        WebElement element = AutomationMethods.findElement(elementKey);

        // İçerikten sadece sayıları almak için
        String elementText = element.getText().replaceAll("[^0-9]", "");

        if (elementText.isEmpty()) {
            throw new Exception("Element içeriğinde sayı bulunamadı: " + elementKey);
        }

        int actualNumber = Integer.parseInt(elementText);
        int expectedNumber = Integer.parseInt(expectedNumberStr); // burası direkt string'den integer'a dönüştü

        System.out.println("Actual number (" + elementKey + "): " + actualNumber);
        System.out.println("Expected minimum number: " + expectedNumber);

        if (actualNumber < expectedNumber) {
            throw new AssertionError("Beklenen: " + actualNumber + " >= " + expectedNumber + " ama değil!");
        } else {
            System.out.println("Assertion PASSED: " + actualNumber + " >= " + expectedNumber);
        }
    }

    @And("Assert that number of {string} element is lower than or equal {string}")
    public void assertThatElementNumberIsLowerThanValue(String elementKey, String expectedNumberStr) throws Exception {
        WebElement element = AutomationMethods.findElement(elementKey);

        // İçerikten sadece sayıları almak için
        String elementText = element.getText().replaceAll("[^0-9]", "");

        if (elementText.isEmpty()) {
            throw new Exception("Element içeriğinde sayı bulunamadı: " + elementKey);
        }

        int actualNumber = Integer.parseInt(elementText);
        int expectedNumber = Integer.parseInt(expectedNumberStr);

        System.out.println("Actual number (" + elementKey + "): " + actualNumber);
        System.out.println("Expected maximum number: " + expectedNumber);

        if (actualNumber > expectedNumber) {
            throw new AssertionError("Beklenen: " + actualNumber + " <= " + expectedNumber + " ama değil!");
        } else {
            System.out.println("Assertion PASSED: " + actualNumber + " <= " + expectedNumber);
        }
    }







}
