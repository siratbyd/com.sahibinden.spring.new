package base;

import io.cucumber.java.After;
import io.cucumber.java.Before;
import org.junit.jupiter.api.*;
import org.openqa.selenium.WebDriver;
import utils.DriverManager;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)  // @BeforeAll ve @AfterAll metodlarının non-static olabilmesi için
public class TestBase {

    private String browser;
    private WebDriver driver;

    @Before
    public void setup() {
        browser = System.getProperty("browser"); // Varsayılan chrome
        driver = DriverManager.getDriver(browser);
    }

    @After
    public void tearDown() {
        DriverManager.quitDriver(); // Her test sonunda kapatılır
        }
    }
