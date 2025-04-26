package runnerClass;

import org.junit.platform.suite.api.*;
import com.sahibinden.auto.spring.TestApplication;
import org.springframework.boot.test.context.SpringBootTest;

@Suite
@IncludeEngines("cucumber")
@SelectDirectories("src/test/resources")  // Feature dosyalarının dizini
@ConfigurationParameter(key = "cucumber.glue", value = "stepDefinitions,base,utils")
@ConfigurationParameter(key = "cucumber.plugin", value = "pretty, html:target/cucumber-report.html, io.qameta.allure.cucumber7jvm.AllureCucumber7Jvm")
@ConfigurationParameter(key = "cucumber.execution.parallel.enabled", value = "true")
@ConfigurationParameter(key = "cucumber.execution.parallel.config.strategy", value = "fixed")
@ConfigurationParameter(key = "cucumber.execution.parallel.config.fixed.parallelism", value = "3")
@SpringBootTest(classes = TestApplication.class)
public class TestRunner {
    static {
        // Varsayılan tarayıcı
        if (System.getProperty("browser") == null) {
            System.setProperty("browser", "chrome");
        }
        
        // Allure sonuçları için dizin belirt
        if (System.getProperty("allure.results.directory") == null) {
            System.setProperty("allure.results.directory", "target/allure-results");
        }
    }
}
