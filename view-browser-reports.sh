#!/bin/bash

# Raporların oluşturulmuş olup olmadığını kontrol et
if [ ! -d "target/allure-report-chrome" ] && [ ! -d "target/allure-report-firefox" ] && [ ! -d "target/allure-report-edge" ]; then
    echo "Hata: Rapor dizinleri bulunamadı."
    echo "Önce 'generate-browser-reports.sh' script'ini çalıştırarak raporları oluşturun."
    exit 1
fi

# Kullanıcıya tarayıcı seçeneklerini göster
echo "Görüntülemek istediğiniz tarayıcı raporunu seçin:"
echo "1. Chrome Raporu"
echo "2. Firefox Raporu"
echo "3. Edge Raporu"
echo "4. Tüm Raporlar (ayrı sekmelerde)"
echo "5. Çıkış"

# Seçim yap
read -p "Seçiminiz (1-5): " choice

case $choice in
    1)
        if [ -d "target/allure-report-chrome" ]; then
            echo "Chrome raporu görüntüleniyor..."
            allure open target/allure-report-chrome
        else
            echo "Chrome raporu bulunamadı!"
        fi
        ;;
    2)
        if [ -d "target/allure-report-firefox" ]; then
            echo "Firefox raporu görüntüleniyor..."
            allure open target/allure-report-firefox
        else
            echo "Firefox raporu bulunamadı!"
        fi
        ;;
    3)
        if [ -d "target/allure-report-edge" ]; then
            echo "Edge raporu görüntüleniyor..."
            allure open target/allure-report-edge
        else
            echo "Edge raporu bulunamadı!"
        fi
        ;;
    4)
        # Tüm raporları ayrı sekmelerde aç
        if [ -d "target/allure-report-chrome" ]; then
            echo "Chrome raporu açılıyor..."
            allure open target/allure-report-chrome &
            sleep 2
        fi
        
        if [ -d "target/allure-report-firefox" ]; then
            echo "Firefox raporu açılıyor..."
            allure open target/allure-report-firefox &
            sleep 2
        fi
        
        if [ -d "target/allure-report-edge" ]; then
            echo "Edge raporu açılıyor..."
            allure open target/allure-report-edge &
        fi
        ;;
    5)
        echo "Çıkış yapılıyor..."
        exit 0
        ;;
    *)
        echo "Geçersiz seçim! Lütfen 1-5 arasında bir değer girin."
        ;;
esac 