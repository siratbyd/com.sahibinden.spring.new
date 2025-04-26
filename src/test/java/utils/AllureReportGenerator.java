package utils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;

/**
 * IntelliJ Compound yapılandırması ile koşturulan testlerden sonra
 * Allure raporlarını birleştirmek için kullanılabilecek yardımcı sınıf.
 * Bu sınıfı doğrudan IntelliJ'den çalıştırabilirsiniz.
 */
public class AllureReportGenerator {

    public static void main(String[] args) {
        System.out.println("Allure rapor birleştirme işlemi başlatılıyor...");

        try {
            // Hedef dizin yolu
            String targetDir = "target/allure-results";

            // Hedef dizini oluştur
            Path targetPath = Paths.get(targetDir);
            Files.createDirectories(targetPath);

            // Browser dizinleri
            String[] browserDirs = {
                    "target/allure-results/chrome",
                    "target/allure-results/firefox",
                    "target/allure-results/edge"
            };

            // Browser sonuçlarını kopyala
            int totalCopiedFiles = 0;

            for (String browserDir : browserDirs) {
                File source = new File(browserDir);

                if (source.exists() && source.isDirectory()) {
                    File[] files = source.listFiles();
                    if (files != null) {
                        System.out.println("-------------------------");
                        System.out.println("Kopyalanıyor: " + browserDir);
                        for (File file : files) {
                            if (file.isFile()) {
                                Path sourcePath = file.toPath();
                                Path destinationPath = targetPath.resolve(file.getName());
                                Files.copy(sourcePath, destinationPath, StandardCopyOption.REPLACE_EXISTING);
                                totalCopiedFiles++;

                                // Her 10 dosyada bir progress göster
                                if (totalCopiedFiles % 10 == 0) {
                                    System.out.println("  " + totalCopiedFiles + " dosya kopyalandı...");
                                }
                            }
                        }
                    }
                } else {
                    System.out.println("Dizin bulunamadı veya boş: " + browserDir);
                }
            }

            System.out.println("-------------------------");
            System.out.println("Toplam " + totalCopiedFiles + " dosya birleştirildi.");

            if (totalCopiedFiles > 0) {
                System.out.println("\nAllure rapor birleştirme tamamlandı!");
                System.out.println("Şimdi aşağıdaki komutu çalıştırarak raporu oluşturabilirsiniz:");
                System.out.println("mvn allure:report\n");
                System.out.println("Veya aşağıdaki yolu tarayıcınızda açabilirsiniz:");
                System.out.println("target/site/allure-maven-plugin/index.html (Rapor oluşturulduysa)\n");
            } else {
                System.out.println("\nHiç dosya kopyalanamadı! Önce testleri çalıştırın.");
            }

        } catch (IOException e) {
            System.err.println("Hata oluştu: " + e.getMessage());
            e.printStackTrace();
        }
    }
} 