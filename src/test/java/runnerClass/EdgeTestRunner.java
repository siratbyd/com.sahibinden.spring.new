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
        value = "pretty, html:target/edge-report.html, io.qameta.allure.cucumber7jvm.AllureCucumber7Jvm"
)
@ConfigurationParameter(key = "allure.results.directory", value = "target/allure-results/edge")
public class EdgeTestRunner {

    static {
        if (System.getProperty("browser") == null) {
            System.setProperty("browser", "edge");
        }
    }
}

//IntelliJ, @Suite ile işaretlenmiş sınıflara sağ tıklayıp Run dediğinde her defasında yeni bir temporary run config oluşturduğu ve VM configleri
//içermediği için testlerimizi paralel koşuma uygun olması için sağ üstteki dropdown'dan başlatmamız gerekmekte.
//İlgili run configurationlar .idea/runConfigurations/ içerisinde