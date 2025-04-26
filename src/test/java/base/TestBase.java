package base;

import annotations.LazyAutowired;
import annotations.LazyComponent;
import annotations.SeleniumTest;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.TestInstance;
import lombok.Getter;
import org.openqa.selenium.WebDriver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import utils.DriverManager;




@SeleniumTest
@Getter
@TestInstance(TestInstance.Lifecycle.PER_CLASS) // @BeforeAll ve @AfterAll non-static olsun
public class TestBase {

    @LazyAutowired
    protected DriverManager driverManager;

    protected WebDriver driver;

    @BeforeEach
    public void setup() {
        String browser = System.getProperty("browser", "chrome"); // VM Option'dan alıyoruz, yoksa chrome
        driver = driverManager.getDriver(browser);
        System.out.println("Driver açıldı, browser: " + browser);
    }

    @AfterEach
    public void tearDown() {
        if (driver != null) {
            driverManager.quitDriver();
            System.out.println("Driver kapatıldı.");
        }
    }
}
