package utils;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import io.qameta.allure.Allure;
import io.qameta.allure.AllureLifecycle;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import io.qameta.allure.Description;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.qameta.allure.junit5.AllureJunit5;

/**
 * Allure raporlamasının çalışıp çalışmadığını test etmek için basit bir test sınıfı
 */
@ExtendWith(AllureJunit5.class)
public class AllureTestRunner {

    @Test
    @Feature("Allure Raporlama")
    @Story("Allure Raporlama Kontrolü")
    @Description("Allure raporlarının doğru oluşturulduğunu kontrol eden test")
    @Severity(SeverityLevel.NORMAL)
    public void allureTestRaporuOlustur() {
        System.out.println("Allure test raporu oluşturuluyor...");

        // Allure rapor adımları oluştur
        AllureLifecycle lifecycle = Allure.getLifecycle();

        Allure.step("Adım 1: Test başlatıldı");

        // Bazı bilgiler ekle
        Allure.addAttachment("Test Bilgisi", "text/plain", "Bu bir test Allure raporudur");
        Allure.parameter("Tarayıcı", System.getProperty("browser", "chrome"));
        Allure.parameter("Ortam", "Test");

        Allure.step("Adım 2: Test bilgileri eklendi");

        // Geçerli bir test iddiası oluştur
        org.junit.jupiter.api.Assertions.assertTrue(true, "Test başarılı oldu");

        Allure.step("Adım 3: Test başarıyla tamamlandı");

        System.out.println("Test tamamlandı - Allure raporları 'target/allure-results' dizinine kaydedilmeli");
        System.out.println("Allure sonuçları dizini: " + System.getProperty("allure.results.directory", "target/allure-results"));
    }
}