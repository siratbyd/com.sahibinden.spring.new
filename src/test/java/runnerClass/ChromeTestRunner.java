package runnerClass;

import annotations.SeleniumTest;
import org.junit.platform.suite.api.*;

@SeleniumTest
@Suite
@IncludeEngines("cucumber")
@SelectDirectories("src/test/resources")
@ConfigurationParameter(key = "cucumber.glue", value = "stepDefinitions")
@ConfigurationParameter(
        key = "cucumber.plugin",
        value = "pretty, html:target/chrome-report.html, io.qameta.allure.cucumber7jvm.AllureCucumber7Jvm"
)
@ConfigurationParameter(key = "allure.results.directory", value = "target/allure-results/chrome")
public class ChromeTestRunner {

    static {
        if (System.getProperty("browser") == null) {
            System.setProperty("browser", "chrome");
        }
    }
}


//IntelliJ, @Suite ile işaretlenmiş sınıflara sağ tıklayıp Run dediğinde her defasında yeni bir temporary run config oluşturduğu ve VM configleri
//içermediği için testlerimizi paralel koşuma uygun olması için sağ üstteki dropdown'dan başlatmamız gerekmekte.
//İlgili run configurationlar .idea/runConfigurations/ içerisinde