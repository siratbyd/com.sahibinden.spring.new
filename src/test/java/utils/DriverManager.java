//package utils;
//
//import io.github.bonigarcia.wdm.WebDriverManager;
//import org.openqa.selenium.WebDriver;
//import org.openqa.selenium.chrome.ChromeOptions;
//import org.openqa.selenium.firefox.FirefoxOptions;
//import org.openqa.selenium.ie.InternetExplorerOptions;
//import org.openqa.selenium.edge.EdgeOptions;
//import org.openqa.selenium.opera.OperaOptions;
//import org.openqa.selenium.safari.SafariOptions;
//import org.openqa.selenium.chrome.ChromeDriver;
//import org.openqa.selenium.firefox.FirefoxDriver;
//import org.openqa.selenium.ie.InternetExplorerDriver;
//import org.openqa.selenium.edge.EdgeDriver;
//import org.openqa.selenium.opera.OperaDriver;
//import org.openqa.selenium.safari.SafariDriver;
//import org.openqa.selenium.remote.RemoteWebDriver;
//import org.springframework.stereotype.Service;
//
//import java.net.MalformedURLException;
//import java.net.URL;
//import java.util.Locale;
//import java.util.concurrent.ConcurrentHashMap;
//
//@Service
//public class DriverManager {
//
//    private static final ConcurrentHashMap<Long, WebDriver> driverMap = new ConcurrentHashMap<>();
//    private static final String GRID_URL = "http://localhost:4444/wd/hub";
//    private static final boolean USE_GRID = Boolean.parseBoolean(ConfigManager.getProperty("use_grid"));
//
//    public static WebDriver getDriver(String browserType) {
//        long threadId = Thread.currentThread().getId();
//
//        if (browserType == null || browserType.isEmpty()) {
//            browserType = "chrome";
//            System.out.println("'browser' null geldi, default olarak 'chrome' seçildi.");
//        }
//
//        if (!driverMap.containsKey(threadId)) {
//            try {
//                WebDriver driver = createDriver(browserType);
//                driverMap.put(threadId, driver);
//                System.out.println("Thread ID: " + threadId + " için " + browserType + " driver oluşturuldu.");
//            } catch (MalformedURLException e) {
//                e.printStackTrace();
//                throw new RuntimeException("Grid URL hatalı: " + GRID_URL);
//            }
//        }
//
//        return driverMap.get(threadId);
//    }
//
//    private static WebDriver createDriver(String browserType) throws MalformedURLException {
//        WebDriver driver;
//
//        switch (browserType.toLowerCase(Locale.ROOT)) {
//            case "chrome":
//                ChromeOptions chromeOptions = new ChromeOptions();
//                chromeOptions.addArguments("--disable-notifications");
//                chromeOptions.addArguments("--disable-popup-blocking");
//                chromeOptions.addArguments("user-agent=Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/122.0.0.0 Safari/537.36 SahibindenOtomasyon/da1f7dbf5c7842819cb75d6a25362611");
//                if (USE_GRID) {
//                    driver = new RemoteWebDriver(new URL(GRID_URL), chromeOptions);
//                } else {
//                    WebDriverManager.chromedriver().setup();
//                    driver = new ChromeDriver(chromeOptions);
//                }
//                break;
//
//            case "firefox":
//                FirefoxOptions firefoxOptions = new FirefoxOptions();
//                firefoxOptions.addPreference("dom.webnotifications.enabled", false);
//                firefoxOptions.addPreference("general.useragent.override", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/122.0.0.0 Safari/537.36 SahibindenOtomasyon/da1f7dbf5c7842819cb75d6a25362611");
//                if (USE_GRID) {
//                    driver = new RemoteWebDriver(new URL(GRID_URL), firefoxOptions);
//                } else {
//                    WebDriverManager.firefoxdriver().setup();
//                    driver = new FirefoxDriver(firefoxOptions);
//                }
//                break;
//
//            case "edge":
//                EdgeOptions edgeOptions = new EdgeOptions();
//                edgeOptions.addArguments("--disable-notifications");
//                edgeOptions.addArguments("user-agent=Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/122.0.0.0 Safari/537.36 SahibindenOtomasyon/da1f7dbf5c7842819cb75d6a25362611");
//                if (USE_GRID) {
//                    driver = new RemoteWebDriver(new URL(GRID_URL), edgeOptions);
//                } else {
//                    WebDriverManager.edgedriver().setup();
//                    driver = new EdgeDriver(edgeOptions);
//                }
//                break;
//
//            case "opera":
//                OperaOptions operaOptions = new OperaOptions();
//                operaOptions.addArguments("user-agent=Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/122.0.0.0 Safari/537.36 SahibindenOtomasyon/da1f7dbf5c7842819cb75d6a25362611");
//                if (USE_GRID) {
//                    driver = new RemoteWebDriver(new URL(GRID_URL), operaOptions);
//                } else {
//                    WebDriverManager.operadriver().setup();
//                    driver = new OperaDriver(operaOptions);
//                }
//                break;
//
//            case "internet explorer":
//                InternetExplorerOptions ieOptions = new InternetExplorerOptions();
//                ieOptions.ignoreZoomSettings();
//                WebDriverManager.iedriver().setup();
//                driver = new InternetExplorerDriver(ieOptions);
//                break;
//
//            case "safari":
//                SafariOptions safariOptions = new SafariOptions();
//                driver = new SafariDriver(safariOptions);
//                break;
//
//            default:
//                throw new IllegalArgumentException("Geçersiz tarayıcı türü: " + browserType);
//        }
//
//        driver.manage().window().maximize();
//        return driver;
//    }
//
//    public WebDriver getDriver() {
//        long threadId = Thread.currentThread().getId();
//        return driverMap.get(threadId);
//    }
//
//    public void quitDriver() {
//        long threadId = Thread.currentThread().getId();
//        if (driverMap.containsKey(threadId)) {
//            driverMap.get(threadId).quit();
//            driverMap.remove(threadId);
//            System.out.println("Thread ID: " + threadId + " için driver kapatıldı.");
//        }
//    }
//
//    // Tüm driverları temizlemek için
//    public static void quitAllDrivers() {
//        for (WebDriver driver : driverMap.values()) {
//            if (driver != null) {
//                driver.quit();
//            }
//        }
//        driverMap.clear();
//        System.out.println("Tüm driverlar kapatıldı.");
//    }
//}


package utils;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.*;
import org.openqa.selenium.edge.*;
import org.openqa.selenium.firefox.*;
import org.openqa.selenium.ie.*;
import org.openqa.selenium.opera.*;
import org.openqa.selenium.safari.*;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.springframework.stereotype.Service;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Locale;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Semaphore;

@Service
public class DriverManager {

    private static final ConcurrentHashMap<Long, WebDriver> driverMap = new ConcurrentHashMap<>();
    private static final String GRID_URL = "http://localhost:4444/wd/hub";
    private static final boolean USE_GRID = Boolean.parseBoolean(ConfigManager.getProperty("use_grid"));

    private static final int MAX_PARALLEL_BROWSERS = Integer.parseInt(ConfigManager.getProperty("max.parallel.browsers"));
    private static final Semaphore BROWSER_SEMAPHORE = new Semaphore(MAX_PARALLEL_BROWSERS);
    private static final String USER_AGENT = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/122.0.0.0 Safari/537.36 SahibindenOtomasyon/da1f7dbf5c7842819cb75d6a25362611";

    public static WebDriver getDriver(String browserType) {
        long threadId = Thread.currentThread().getId();

        if (browserType == null || browserType.isEmpty()) {
            browserType = "chrome";
            System.out.println("'browser' null geldi, default olarak 'chrome' seçildi.");
        }

        if (!driverMap.containsKey(threadId)) {
            try {
                // Eğer izin yoksa bekleyecek
                BROWSER_SEMAPHORE.acquire();
                WebDriver driver = createDriver(browserType);
                driverMap.put(threadId, driver);
                System.out.println("Thread ID: " + threadId + " için " + browserType + " driver oluşturuldu.");
            } catch (MalformedURLException e) {
                e.printStackTrace();
                throw new RuntimeException("Grid URL hatalı: " + GRID_URL);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                throw new RuntimeException("Driver oluşturulurken thread kesildi!");
            }
        }

        return driverMap.get(threadId);
    }

    private static WebDriver createDriver(String browserType) throws MalformedURLException {
        WebDriver driver;

        switch (browserType.toLowerCase(Locale.ROOT)) {
            case "chrome":
                ChromeOptions chromeOptions = new ChromeOptions();
                chromeOptions.addArguments("--disable-notifications");
                chromeOptions.addArguments("--disable-popup-blocking");
                chromeOptions.addArguments("user-agent=" + USER_AGENT);
                if (USE_GRID) {
                    driver = new RemoteWebDriver(new URL(GRID_URL), chromeOptions);
                } else {
                    WebDriverManager.chromedriver().setup();
                    driver = new ChromeDriver(chromeOptions);
                }
                break;

            case "firefox":
                FirefoxOptions firefoxOptions = new FirefoxOptions();
                firefoxOptions.addPreference("dom.webnotifications.enabled", false);
                firefoxOptions.addPreference("general.useragent.override", USER_AGENT);
                if (USE_GRID) {
                    driver = new RemoteWebDriver(new URL(GRID_URL), firefoxOptions);
                } else {
                    WebDriverManager.firefoxdriver().setup();
                    driver = new FirefoxDriver(firefoxOptions);
                }
                break;

            case "edge":
                EdgeOptions edgeOptions = new EdgeOptions();
                edgeOptions.addArguments("--disable-notifications");
                edgeOptions.addArguments("user-agent=" + USER_AGENT);
                if (USE_GRID) {
                    driver = new RemoteWebDriver(new URL(GRID_URL), edgeOptions);
                } else {
                    WebDriverManager.edgedriver().setup();
                    driver = new EdgeDriver(edgeOptions);
                }
                break;

            case "opera":
                OperaOptions operaOptions = new OperaOptions();
                operaOptions.addArguments("user-agent=" + USER_AGENT);
                if (USE_GRID) {
                    driver = new RemoteWebDriver(new URL(GRID_URL), operaOptions);
                } else {
                    WebDriverManager.operadriver().setup();
                    driver = new OperaDriver(operaOptions);
                }
                break;

            case "internet explorer":
                InternetExplorerOptions ieOptions = new InternetExplorerOptions();
                ieOptions.ignoreZoomSettings();
                WebDriverManager.iedriver().setup();
                driver = new InternetExplorerDriver(ieOptions);
                break;

            case "safari":
                SafariOptions safariOptions = new SafariOptions();
                driver = new SafariDriver(safariOptions);
                break;

            default:
                throw new IllegalArgumentException("Geçersiz tarayıcı türü: " + browserType);
        }

        driver.manage().window().maximize();
        return driver;
    }

    public WebDriver getDriver() {
        long threadId = Thread.currentThread().getId();
        return driverMap.get(threadId);
    }

    public void quitDriver() {
        long threadId = Thread.currentThread().getId();
        if (driverMap.containsKey(threadId)) {
            driverMap.get(threadId).quit();
            driverMap.remove(threadId);
            BROWSER_SEMAPHORE.release();
            System.out.println("Thread ID: " + threadId + " için driver kapatıldı ve slot boşaltıldı.");
        }
    }

    public static void quitAllDrivers() {
        for (WebDriver driver : driverMap.values()) {
            if (driver != null) {
                driver.quit();
            }
        }
        driverMap.clear();
        // Açık olan tüm izinleri bırak
        while (BROWSER_SEMAPHORE.availablePermits() < MAX_PARALLEL_BROWSERS) {
            BROWSER_SEMAPHORE.release();
        }
        System.out.println("Tüm driverlar kapatıldı ve tüm slotlar boşaltıldı.");
    }
}

