package stepDefinitions;

import annotations.LazyComponent;
import annotations.LazyAutowired;
import base.AutomationMethods;
import io.cucumber.java.en.*;
import org.junit.jupiter.api.Assertions;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.springframework.beans.factory.annotation.Value;
import utils.DriverManager;


public class GeneralSteps {

    @LazyAutowired
    private DriverManager driverManager;

    @LazyAutowired
    private AutomationMethods automationMethods;

    @Value("${url}")
    private String baseUrl;

    private static int savedNumber = -1;

    @Given("Go to url")
    public void go_to_url() throws Exception {
        System.out.println("Base URL: " + baseUrl);
        String browser = System.getProperty("browser", "chrome");
        WebDriver driver = driverManager.getDriver(browser);
        driver.get(baseUrl);
        System.out.println("URL açıldı: " + baseUrl);
        automationMethods.waitForElementToBeVisible("CEREZLERI_KABUL_ET_BUTON", 10);
    }

    @And("Click {string}")
    public void clickElement(String element) throws Exception {
        automationMethods.waitAndClick(element);
    }

    @And("Hover {string}")
    public void hover(String element) throws Exception {
        automationMethods.hoverOverElement(element);
    }

    @And("Switch new window")
    public void switchNewWindow() {
        automationMethods.switchToNewWindow();
    }

    @And("Assert that {string} element is visible")
    public void assert_that_element_is_visible(String element) throws Exception {
        boolean isVisible = automationMethods.waitForElementToBeVisible(element, 10);
        Assertions.assertTrue(isVisible, "Element is not visible: " + element);
    }

    @And("Assert that element {string} contains text {string}")
    public void assertElementContainsText(String element, String text) throws Exception {
        automationMethods.assertElementContainsText(element, text);
    }

    @And("Assert that {string} element is clickable")
    public void assertElementIsClickable(String element) throws Exception {
        automationMethods.assertElementIsClickable(element);
    }

    @And("Wait {int} seconds")
    public void wait_seconds(int seconds) throws InterruptedException {
        System.out.println("Waiting " + seconds + " seconds");
        Thread.sleep(seconds * 1000L);
        System.out.println("Waiting ended.");
    }

    @And("Assert that previous element number equal {string} this number")
    public void assertThatEqualNumbers(String key) throws Exception {
        WebElement element = automationMethods.findElement(key);
        String numberOnly = element.getText().replaceAll("[^0-9]", "");

        if (numberOnly.isEmpty()) throw new Exception("Element içinde sayı bulunamadı: " + key);

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
        WebElement element = automationMethods.findElement(key);
        String numberOnly = element.getText().replaceAll("[^0-9]", "");

        if (numberOnly.isEmpty()) throw new Exception("Element içinde sayı bulunamadı: " + key);

        savedNumber = Integer.parseInt(numberOnly);
        System.out.println("Saved number: " + savedNumber);
    }

    @And("Write {string} to {string} input field")
    public void sendKeysToInputField(String text, String key) throws Exception {
        automationMethods.sendKeysToElement(key, text);
    }

    @And("Assert that number of {string} element is greater than or equal {string}")
    public void assertThatElementNumberIsGreaterThanValue(String elementKey, String expectedNumberStr) throws Exception {
        WebElement element = automationMethods.findElement(elementKey);
        String elementText = element.getText().replaceAll("[^0-9]", "");

        if (elementText.isEmpty()) throw new Exception("Element içeriğinde sayı bulunamadı: " + elementKey);

        int actualNumber = Integer.parseInt(elementText);
        int expectedNumber = Integer.parseInt(expectedNumberStr);

        if (actualNumber < expectedNumber) {
            throw new AssertionError("Beklenen: " + actualNumber + " >= " + expectedNumber + " ama değil!");
        } else {
            System.out.println("Assertion PASSED: " + actualNumber + " >= " + expectedNumber);
        }
    }

    @And("Assert that number of {string} element is lower than or equal {string}")
    public void assertThatElementNumberIsLowerThanValue(String elementKey, String expectedNumberStr) throws Exception {
        WebElement element = automationMethods.findElement(elementKey);
        String elementText = element.getText().replaceAll("[^0-9]", "");

        if (elementText.isEmpty()) throw new Exception("Element içeriğinde sayı bulunamadı: " + elementKey);

        int actualNumber = Integer.parseInt(elementText);
        int expectedNumber = Integer.parseInt(expectedNumberStr);

        if (actualNumber > expectedNumber) {
            throw new AssertionError("Beklenen: " + actualNumber + " <= " + expectedNumber + " ama değil!");
        } else {
            System.out.println("Assertion PASSED: " + actualNumber + " <= " + expectedNumber);
        }
    }
}
