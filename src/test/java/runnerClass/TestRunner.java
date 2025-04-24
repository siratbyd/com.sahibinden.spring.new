//package runnerClass;

/*import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;
import org.junit.Before;


import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.runners.Parameterized;
import utils.DataManager;
import utils.DriverManager;


import org.junit.runner.RunWith;
@RunWith(Cucumber.class)
@CucumberOptions(features = "src/test/resources",
        glue = "stepDefinitions",
        tags = "@ADD_PRODUCT",
        plugin = {"pretty", "html:target/cucumber-reports.html", "io.qameta.allure.cucumber7jvm.AllureCucumber7Jvm",},
        monochrome = true)

public class TestRunner{

    @BeforeEach
    @Parameterized.Parameters(name = "browser")     //buraya bir bakmam lazım!!!
    public void setup(String browser) {DriverManager.getDriver(browser);}

    @AfterEach
    public void tearDown() {DriverManager.quitDriver();}

}


 */



//import io.cucumber.junit.platform.engine.Cucumber;
//
//@Cucumber
//public class TestRunner {}




    // JUnit 5 ile Cucumber entegrasyonu için bu sınıf boş bırakılır.
    // daha temiz kurulum için
//    import org.junit.platform.suite.api.*;
//
//    @Suite
//    @IncludeEngines("cucumber")
//    @SelectDirectories("src/test/resources")
//    @ConfigurationParameter(key = "cucumber.glue", value = "stepDefinitions")
//    @ConfigurationParameter(key = "cucumber.plugin", value = "pretty, html:target/cucumber-report.html")
//    @ConfigurationParameter(key = "cucumber.filter.tags", value = "@YourTagIfNeeded")
//    public class TestSuite {


package runnerClass;

import org.junit.platform.suite.api.*;

@Suite
@IncludeEngines("cucumber")
@SelectDirectories("src/test/resources")  // Feature dosyalarının dizini
@ConfigurationParameter(key = "cucumber.glue", value = "stepDefinitions,base,utils")
@ConfigurationParameter(key = "cucumber.plugin", value = "pretty, html:target/cucumber-report.html")
@ConfigurationParameter(key = "cucumber.filter.tags", value = "@test")
public class TestRunner {
}
