package stepDefinitions;

import io.cucumber.java.Before;
import io.cucumber.java.After;
import utils.DriverManager;

public class Hooks {

    @Before
    public void setUp() {
        String browser = System.getProperty("browser");
        DriverManager.getDriver(browser);
        System.out.println(" Browser started: " + browser);
    }

    @After
    public void tearDown() {
        DriverManager.quitDriver();
        System.out.println(" Browser ended.");
    }
}
