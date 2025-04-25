//package utils;
//
//import io.github.bonigarcia.wdm.WebDriverManager;
//
//
//import org.openqa.selenium.WebDriver;
//import org.openqa.selenium.chrome.ChromeDriver;
//import org.openqa.selenium.chrome.ChromeOptions;
//import org.openqa.selenium.firefox.FirefoxDriver;
//import org.openqa.selenium.firefox.FirefoxOptions;
//import org.openqa.selenium.ie.InternetExplorerDriver;
//import org.openqa.selenium.ie.InternetExplorerOptions;
//import org.openqa.selenium.remote.RemoteWebDriver;
//import org.openqa.selenium.safari.SafariDriver;
//import org.openqa.selenium.safari.SafariOptions;
//import org.openqa.selenium.edge.EdgeDriver;
//import org.openqa.selenium.edge.EdgeOptions;
//import org.openqa.selenium.opera.OperaDriver;
//import org.openqa.selenium.opera.OperaOptions;
//
//
//public class DriverManager {
//
//    private static ThreadLocal<WebDriver> driver = new ThreadLocal<>();
//
//
//    public static WebDriver getDriver(String browserType) {
//
//        if (browserType == null || browserType.isEmpty()) {
//            browserType = "chrome";  // fallback garanti chrome
//            System.out.println("'browser' null geldi, default olarak 'chrome' seçildi.");
//        }
//
//
//
//        if (driver.get() == null) {  // Eğer thread'deki driver null ise
//            switch (browserType.toLowerCase()) {
//                case "chrome":
//                    ChromeOptions chromeOptions = new ChromeOptions();
//                    chromeOptions.addArguments("--disable-notifications");
//                    chromeOptions.addArguments("--disable-popup-blocking");
//                    // case için temin edilen user agent
//                    chromeOptions.addArguments("user-agent=Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/122.0.0.0 Safari/537.36 SahibindenOtomasyon/da1f7dbf5c7842819cb75d6a25362611");
//                    WebDriverManager.chromedriver().setup();
//                    driver.set(new ChromeDriver(chromeOptions));  // Chrome driver'ı oluştur
//                    break;
//
//                case "firefox":
//                    FirefoxOptions firefoxOptions = new FirefoxOptions();
//                    firefoxOptions.addPreference("dom.webnotifications.enabled", false);
//                    firefoxOptions.addPreference("general.useragent.override", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/122.0.0.0 Safari/537.36 SahibindenOtomasyon/da1f7dbf5c7842819cb75d6a25362611");
//                    WebDriverManager.firefoxdriver().setup();
//                    driver.set(new FirefoxDriver(firefoxOptions));  // Firefox driver'ı oluştur
//                    break;
//
//                case "internet explorer":
//                    WebDriverManager.iedriver().setup();
//                    InternetExplorerOptions ieOptions = new InternetExplorerOptions();
//                    ieOptions.ignoreZoomSettings(); // IE’de yaygın sorunlardan biri zoom ayarlarıdır.
//                    driver.set(new InternetExplorerDriver(ieOptions)); // IE driver'ı oluştur
//                    break;
//
//                case "safari":
//                    SafariOptions safariOptions = new SafariOptions();
//                    //terminalden safaridriver --enable ile etkinleştirmeyi unutma.
//                    driver.set(new SafariDriver(safariOptions)); // Safari driver'ı oluştur
//                    break;
//
//                case "edge":
//                    EdgeOptions edgeOptions = new EdgeOptions();
//                    edgeOptions.addArguments("user-agent=Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/122.0.0.0 Safari/537.36 SahibindenOtomasyon/da1f7dbf5c7842819cb75d6a25362611");
//                    WebDriverManager.edgedriver().setup();
//                    driver.set(new EdgeDriver(edgeOptions));  // edge driver'ı oluştur
//                    break;
//
//                case "opera":
//                    OperaOptions operaOptions = new OperaOptions();
//                    operaOptions.addArguments("user-agent=Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/122.0.0.0 Safari/537.36 SahibindenOtomasyon/da1f7dbf5c7842819cb75d6a25362611");
//                    WebDriverManager.operadriver().setup();
//                    driver.set(new OperaDriver(operaOptions));  // opera driver'ı oluştur
//                    break;
//
//
//
//                default:
//                    throw new IllegalArgumentException("Geçersiz tarayıcı türü: " + browserType);
//            }
//
//            driver.get().manage().window().maximize();
//        }
//        return driver.get();  // Her test için thread'e özel driver'ı döndür
//    }
//
//    public static void quitDriver() {
//
//        if (driver.get() != null) {
//            driver.get().quit();
//            driver.remove();  //Threadlocal driver'ı temizlemek için
//        }
//    }
//    public static WebDriver getDriver() {
//        return driver.get();
//    }
//}


package utils;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.ie.InternetExplorerOptions;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.opera.OperaOptions;
import org.openqa.selenium.safari.SafariOptions;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.opera.OperaDriver;
import org.openqa.selenium.safari.SafariDriver;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.net.MalformedURLException;
import java.net.URL;

public class DriverManager {

    private static ThreadLocal<WebDriver> driver = new ThreadLocal<>();

    private static final String GRID_URL = "http://localhost:4444/wd/hub";


    //docker-seleniumgrid paralel koşum için configurati.properties dosyasından use_grid true'ya çekilir, local test için false olmalu
    private static final boolean USE_GRID = Boolean.parseBoolean(ConfigManager.getProperty("use_grid"));



    public static WebDriver getDriver(String browserType) {
        if (browserType == null || browserType.isEmpty()) {
            browserType = "chrome";
            System.out.println("'browser' null geldi, default olarak 'chrome' seçildi.");
        }

        if (driver.get() == null) {
            try {
                switch (browserType.toLowerCase()) {
                    case "chrome":
                        ChromeOptions chromeOptions = new ChromeOptions();
                        chromeOptions.addArguments("--disable-notifications");
                        chromeOptions.addArguments("--disable-popup-blocking");
                        chromeOptions.addArguments("user-agent=Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/122.0.0.0 Safari/537.36 SahibindenOtomasyon/da1f7dbf5c7842819cb75d6a25362611");
                        if (USE_GRID) {
                            driver.set(new RemoteWebDriver(new URL(GRID_URL), chromeOptions));
                        } else {
                            WebDriverManager.chromedriver().setup();
                            driver.set(new ChromeDriver(chromeOptions));
                        }
                        break;

                    case "firefox":
                        FirefoxOptions firefoxOptions = new FirefoxOptions();
                        firefoxOptions.addPreference("dom.webnotifications.enabled", false);
                        firefoxOptions.addPreference("general.useragent.override", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/122.0.0.0 Safari/537.36 SahibindenOtomasyon/da1f7dbf5c7842819cb75d6a25362611");
                        if (USE_GRID) {
                            driver.set(new RemoteWebDriver(new URL(GRID_URL), firefoxOptions));
                        } else {
                            WebDriverManager.firefoxdriver().setup();
                            driver.set(new FirefoxDriver(firefoxOptions));
                        }
                        break;

                    case "edge":
                        EdgeOptions edgeOptions = new EdgeOptions();
                        edgeOptions.addArguments("--disable-notifications");
                        edgeOptions.addArguments("user-agent=Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/122.0.0.0 Safari/537.36 SahibindenOtomasyon/da1f7dbf5c7842819cb75d6a25362611");
                        if (USE_GRID) {
                            driver.set(new RemoteWebDriver(new URL(GRID_URL), edgeOptions));
                        } else {
                            WebDriverManager.edgedriver().setup();
                            driver.set(new EdgeDriver(edgeOptions));
                        }
                        break;

                    case "opera":
                        OperaOptions operaOptions = new OperaOptions();
                        operaOptions.addArguments("user-agent=Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/122.0.0.0 Safari/537.36 SahibindenOtomasyon/da1f7dbf5c7842819cb75d6a25362611");
                        if (USE_GRID) {
                            driver.set(new RemoteWebDriver(new URL(GRID_URL), operaOptions));
                        } else {
                            WebDriverManager.operadriver().setup();
                            driver.set(new OperaDriver(operaOptions));
                        }
                        break;

                    case "internet explorer":
                        InternetExplorerOptions ieOptions = new InternetExplorerOptions();
                        ieOptions.ignoreZoomSettings();
                        WebDriverManager.iedriver().setup();
                        driver.set(new InternetExplorerDriver(ieOptions));
                        break;

                    case "safari":
                        SafariOptions safariOptions = new SafariOptions();
                        driver.set(new SafariDriver(safariOptions));
                        break;

                    default:
                        throw new IllegalArgumentException("Geçersiz tarayıcı türü: " + browserType);
                }

                driver.get().manage().window().maximize();

            } catch (MalformedURLException e) {
                e.printStackTrace();
                throw new RuntimeException("Grid URL hatalı: " + GRID_URL);
            }
        }

        return driver.get();
    }

    public static void quitDriver() {
        if (driver.get() != null) {
            driver.get().quit();
            driver.remove();
        }
    }

    public static WebDriver getDriver() {
        return driver.get();
    }
}
