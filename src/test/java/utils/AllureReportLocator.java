package utils;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

/**
 * Allure rapor dosyalarını bulmak için yardımcı sınıf
 */
public class AllureReportLocator {

    public static void main(String[] args) {
        System.out.println("Allure rapor dosyalarını arama işlemi başlatılıyor...");


        File targetDir = new File("target");
        if (targetDir.exists() && targetDir.isDirectory()) {
            System.out.println("'target' dizini bulundu.");


            List<String> allureResultPaths = new ArrayList<>();
            findAllureResults(targetDir, allureResultPaths);

            if (allureResultPaths.isEmpty()) {
                System.out.println("Hiçbir Allure sonuç dosyası bulunamadı!");
            } else {
                System.out.println("\nBulunan Allure sonuç dizinleri:");
                for (String path : allureResultPaths) {
                    System.out.println("- " + path);


                    File resultDir = new File(path);
                    File[] files = resultDir.listFiles();
                    if (files != null) {
                        System.out.println("  Dosya sayısı: " + files.length);
                        // İlk 5 dosyayı göster
                        int count = 0;
                        for (File file : files) {
                            if (count < 5) {
                                System.out.println("  • " + file.getName());
                                count++;
                            } else {
                                System.out.println("  • ...");
                                break;
                            }
                        }
                    }
                }

                System.out.println("\nŞimdi AllureReportGenerator sınıfını bu dizinleri kullanacak şekilde güncelleyin.");
            }
        } else {
            System.out.println("'target' dizini bulunamadı!");
        }
    }

    private static void findAllureResults(File dir, List<String> resultPaths) {
        if (dir == null || !dir.isDirectory()) {
            return;
        }


        if (dir.getName().contains("allure") || dir.getName().contains("Allure")) {

            File[] files = dir.listFiles();
            if (files != null && files.length > 0) {
                for (File file : files) {
                    if (file.getName().endsWith(".json") ||
                            file.getName().endsWith(".xml") ||
                            file.getName().endsWith(".attach")) {

                        resultPaths.add(dir.getAbsolutePath());
                        break;
                    }
                }
            }
        }


        File[] subDirs = dir.listFiles(File::isDirectory);
        if (subDirs != null) {
            for (File subDir : subDirs) {
                findAllureResults(subDir, resultPaths);
            }
        }
    }
}