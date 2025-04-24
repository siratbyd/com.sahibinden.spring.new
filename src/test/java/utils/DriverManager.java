package utils;

import io.github.bonigarcia.wdm.WebDriverManager;


import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.ie.InternetExplorerOptions;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.safari.SafariDriver;
import org.openqa.selenium.safari.SafariOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.opera.OperaDriver;
import org.openqa.selenium.opera.OperaOptions;


public class DriverManager {

    private static ThreadLocal<WebDriver> driver = new ThreadLocal<>();


    public static WebDriver getDriver(String browserType) {
        if (driver.get() == null) {  // Eğer thread'deki driver null ise
            switch (browserType.toLowerCase()) {
                case "chrome":
                    ChromeOptions chromeOptions = new ChromeOptions();
                    chromeOptions.addArguments("--disable-notifications");
                    chromeOptions.addArguments("--disable-popup-blocking");
                    // case için temin edilen user agent
                    chromeOptions.addArguments("user-agent=Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/122.0.0.0 Safari/537.36 SahibindenOtomasyon/da1f7dbf5c7842819cb75d6a25362611");
                    WebDriverManager.chromedriver().setup();
                    driver.set(new ChromeDriver(chromeOptions));  // Chrome driver'ı oluştur
                    break;

                case "firefox":
                    FirefoxOptions firefoxOptions = new FirefoxOptions();
                    firefoxOptions.addPreference("dom.webnotifications.enabled", false);
                    firefoxOptions.addArguments("user-agent=Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/122.0.0.0 Safari/537.36 SahibindenOtomasyon/da1f7dbf5c7842819cb75d6a25362611");
                    WebDriverManager.firefoxdriver().setup();
                    driver.set(new FirefoxDriver(firefoxOptions));  // Firefox driver'ı oluştur
                    break;

                case "internet explorer":
                    WebDriverManager.iedriver().setup();
                    InternetExplorerOptions ieOptions = new InternetExplorerOptions();
                    ieOptions.ignoreZoomSettings(); // IE’de yaygın sorunlardan biri zoom ayarlarıdır.
                    driver.set(new InternetExplorerDriver(ieOptions)); // IE driver'ı oluştur
                    break;

                case "safari":
                    SafariOptions safariOptions = new SafariOptions();
                    //terminalden safaridriver --enable ile etkinleştirmeyi unutma.
                    driver.set(new SafariDriver(safariOptions)); // Safari driver'ı oluştur
                    break;

                case "edge":
                    EdgeOptions edgeOptions = new EdgeOptions();
                    edgeOptions.addArguments("user-agent=Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/122.0.0.0 Safari/537.36 SahibindenOtomasyon/da1f7dbf5c7842819cb75d6a25362611");
                    WebDriverManager.edgedriver().setup();
                    driver.set(new EdgeDriver(edgeOptions));  // edge driver'ı oluştur
                    break;

                case "opera":
                    OperaOptions operaOptions = new OperaOptions();
                    operaOptions.addArguments("user-agent=Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/122.0.0.0 Safari/537.36 SahibindenOtomasyon/da1f7dbf5c7842819cb75d6a25362611");
                    WebDriverManager.operadriver().setup();
                    driver.set(new OperaDriver(operaOptions));  // opera driver'ı oluştur
                    break;



                default:
                    throw new IllegalArgumentException("Geçersiz tarayıcı türü: " + browserType);
            }

            driver.get().manage().window().maximize();
        }
        return driver.get();  // Her test için thread'e özel driver'ı döndür
    }

    public static void quitDriver() {

        if (driver.get() != null) {
            driver.get().quit();
            driver.remove();  //Threadlocal driver'ı temizlemek için
        }
    }
}
