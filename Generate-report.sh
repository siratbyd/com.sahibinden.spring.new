#!/bin/bash
echo "===================================================="
echo "Sahibinden Test Otomasyon - Allure Rapor Oluşturma"
echo "===================================================="

# Java sınıfı ile sonuçları birleştir
echo "Java ile Allure sonuçlarını birleştirme..."
mvn exec:java -Dexec.mainClass="utils.AllureReportGenerator" -Dexec.classpathScope=test

# Allure raporu oluştur
echo "Allure raporu oluşturuluyor..."
mvn allure:report

# Raporu aç
echo "================================="
echo "Rapor hazır! Raporu görüntülemek için aşağıdaki adresi tarayıcınızda açın:"
echo "$(pwd)/target/site/allure-maven-plugin/index.html"
echo "================================="