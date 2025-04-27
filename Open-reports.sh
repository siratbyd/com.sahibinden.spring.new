#!/bin/bash

# target klasörüne gir
cd target || { echo "target klasörü bulunamadı"; exit 1; }

# Kullanıcıya seçenekleri göster
echo "Hangi raporu açmak istiyorsunuz?"
echo "1. Chrome raporu (chrome-report.html)"
echo "2. Edge raporu (edge-report.html)"
echo "3. Firefox raporu (firefox-report.html)"
echo "4. Paralel test koşum raporları (chrome, edge, firefox raporlarını aç)"
read -p "Seçiminiz (1-4): " choice

# Seçime göre işlem yap
case $choice in
  1)
    if [ -f "chrome-report.html" ]; then
      start "" "$PWD/chrome-report.html"
    else
      echo "Chrome raporu bulunamadı."
    fi
    ;;
  2)
    if [ -f "edge-report.html" ]; then
      start "" "$PWD/edge-report.html"
    else
      echo "Edge raporu bulunamadı."
    fi
    ;;
  3)
    if [ -f "firefox-report.html" ]; then
      start "" "$PWD/firefox-report.html"
    else
      echo "Firefox raporu bulunamadı."
    fi
    ;;
  4)
    # Üç raporu da ayrı ayrı sekmelerde aç
    [ -f "chrome-report.html" ] && start "" "$PWD/chrome-report.html"
    [ -f "edge-report.html" ] && start "" "$PWD/edge-report.html"
    [ -f "firefox-report.html" ] && start "" "$PWD/firefox-report.html"
    ;;
  *)
    echo "Geçersiz seçim! Lütfen 1-4 arası bir sayı girin."
    ;;
esac
