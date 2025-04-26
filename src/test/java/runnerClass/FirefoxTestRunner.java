package runnerClass;

import annotations.SeleniumTest;
import org.junit.jupiter.api.BeforeAll;
import org.junit.platform.suite.api.*;

@SeleniumTest
@Suite
@IncludeEngines("cucumber")
@SelectDirectories("src/test/resources")
@ConfigurationParameter(key = "cucumber.glue", value = "stepDefinitions")
@ConfigurationParameter(
        key = "cucumber.plugin",
        value = "pretty, html:target/firefox-report.html, io.qameta.allure.cucumber7jvm.AllureCucumber7Jvm"
)
@ConfigurationParameter(key = "allure.results.directory", value = "target/allure-results/firefox")

public class FirefoxTestRunner {

    @BeforeAll
    public static void setupBrowser() {
        System.setProperty("browser", "firefox");
        System.out.println("Firefox test runner başlatıldı, browser: firefox");
    }
}

//IntelliJ, @Suite ile işaretlenmiş sınıflara sağ tıklayıp Run dediğinde her defasında yeni bir temporary run config oluşturduğu ve VM configleri
//içermediği için testlerimizi paralel koşuma uygun olması için sağ üstteki dropdown'dan başlatmamız gerekmekte.
//İlgili run configurationlar .idea/runConfigurations/ içerisinde