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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


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

    @And("Assert that first 10 product prices are sorted in ascending order")
    public void assertFirst10ProductPricesSortedAscending() throws Exception {
        List<WebElement> priceElements = automationMethods.findElements("PRODUCT_PRICE_LIST"); // JSON'dan geliyor
        List<Integer> prices = new ArrayList<>();

        int count = Math.min(10, priceElements.size()); // Ürün 10'dan azsa hata vermesin

        for (int i = 0; i < count; i++) {
            String priceText = priceElements.get(i).getText().replaceAll("[^0-9]", ""); // Sadece rakamları alıyoruz
            if (!priceText.isEmpty()) {
                prices.add(Integer.parseInt(priceText));
            }
        }

        if (prices.isEmpty()) {
            throw new Exception("Fiyat listesi boş geldi!");
        }

        List<Integer> sortedPrices = new ArrayList<>(prices);
        Collections.sort(sortedPrices);

        if (!prices.equals(sortedPrices)) {
            throw new AssertionError("İlk " + count + " ürün fiyatı düşükten yükseğe sıralı değil!\nGerçek sıra: " + prices + "\nBeklenen sıra: " + sortedPrices);
        } else {
            System.out.println("Assertion PASSED: İlk " + count + " ürün fiyatı düşükten yükseğe sıralı.");
        }
    }

    @And("Assert that first 10 product prices are sorted in descending order")
    public void assertFirst10ProductPricesSortedDescending() throws Exception {
        List<WebElement> priceElements = automationMethods.findElements("PRODUCT_PRICE_LIST");
        List<Integer> prices = new ArrayList<>();

        int count = Math.min(10, priceElements.size());

        for (int i = 0; i < count; i++) {
            String priceText = priceElements.get(i).getText().replaceAll("[^0-9]", "");
            if (!priceText.isEmpty()) {
                prices.add(Integer.parseInt(priceText));
            }
        }

        if (prices.isEmpty()) {
            throw new Exception("Fiyat listesi boş geldi!");
        }

        List<Integer> sortedPrices = new ArrayList<>(prices);
        sortedPrices.sort(Collections.reverseOrder());

        if (!prices.equals(sortedPrices)) {
            throw new AssertionError("İlk " + count + " ürün fiyatı yüksekten düşüğe sıralı değil!\nGerçek sıra: " + prices + "\nBeklenen sıra: " + sortedPrices);
        } else {
            System.out.println("Assertion PASSED: İlk " + count + " ürün fiyatı yüksekten düşüğe sıralı.");
        }
    }
    @And("Save the texts from {string} element")
    public void saveTheTextsFromElement(String elementKey) throws Exception {
        WebElement element = automationMethods.findElement(elementKey);
        String elementText = element.getText().trim();

        if (elementText.isEmpty()) {
            throw new Exception("Elementten alınan text boş: " + elementKey);
        }

        automationMethods.storeElementText(elementKey, elementText);
    }

    @And("Assert that stored text from {string} is contained in {string} element")
    public void assertThatStoredTextIsContainedAfterCleaningAndSplitting(String storedKey, String elementKey) throws Exception {
        String storedText = automationMethods.getStoredText(storedKey);
        WebElement element = automationMethods.findElement(elementKey);
        String elementText = element.getText();

        // Temizleme: Küçük harfe çevir, virgül ve tire kaldır
        storedText = storedText.toLowerCase().replace(",", "").replace("-", "").trim();
        elementText = elementText.toLowerCase().replace(",", "").replace("-", "").trim();

        // Split kelimelere
        String[] storedWords = storedText.split("\\s+"); // boşluklara göre ayır

        for (String word : storedWords) {
            if (!elementText.contains(word)) {
                throw new AssertionError("Beklenen kelime bulunamadı!\nAranan kelime: " + word + "\nElement Text: " + elementText);
            }
        }
        System.out.println("Assertion Passed: Tüm kelimeler bulundu.");
    }




}
