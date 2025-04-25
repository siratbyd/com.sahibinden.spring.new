package runnerClass;


import org.junit.platform.suite.api.*;

@Suite
@IncludeEngines("cucumber")
@SelectDirectories("src/test/resources")
@ConfigurationParameter(key = "cucumber.glue", value = "stepDefinitions")
@ConfigurationParameter(
        key = "cucumber.plugin",
        value = "pretty, html:target/firefox-report.html, io.qameta.allure.cucumber7jvm.AllureCucumber7Jvm"
)
@ConfigurationParameter(key = "allure.results.directory", value = "target/allure-results-firefox")
@ConfigurationParameter(key = "cucumber.filter.tags", value = "@TEST")

public class FirefoxTestRunner {

    static {
        if (System.getProperty("browser") == null) {
            System.setProperty("browser", "firefox");
        }
    }
}

//IntelliJ, @Suite ile işaretlenmiş sınıflara sağ tıklayıp Run dediğinde her defasında yeni bir temporary run config oluşturduğu ve VM configleri
//içermediği için testlerimizi paralel koşuma uygun olması için sağ üstteki dropdown'dan başlatmamız gerekmekte.
//İlgili run configurationlar .idea/runConfigurations/ içerisinde