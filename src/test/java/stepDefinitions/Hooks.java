package stepDefinitions;

import io.cucumber.java.Before;
import io.cucumber.java.After;
import io.cucumber.java.Scenario;
import annotations.LazyAutowired;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Scope;
import utils.DriverManager;

@Scope("cucumber-glue")
public class Hooks {

    @LazyAutowired
    private DriverManager driverManager;

    private String browser;

    @Before
    public void setUp(Scenario scenario) {
        browser = System.getProperty("browser", "chrome");
        System.out.println("Senaryo başlatılıyor: " + scenario.getName());
        System.out.println("Browser: " + browser);
        driverManager.getDriver(browser);
    }

    @After
    public void tearDown(Scenario scenario) {
        System.out.println("Senaryo sonlandırılıyor: " + scenario.getName() + " - Durum: " + scenario.getStatus());
        driverManager.quitDriver();
    }
}
