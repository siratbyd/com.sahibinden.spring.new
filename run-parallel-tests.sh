#!/bin/bash

# Raporlama klasörlerini temizle
echo "Eski test sonuçları temizleniyor..."
rm -rf target/allure-results
rm -rf target/allure-results/chrome
rm -rf target/allure-results/firefox
rm -rf target/allure-results/edge
mkdir -p target/allure-results/chrome
mkdir -p target/allure-results/firefox
mkdir -p target/allure-results/edge

# Selenium Grid'i başlat
echo "Selenium Grid başlatılıyor..."
./start-grid.sh

# Tarayıcı profilleri için ön kontrol
if [ ! -f "pom.xml" ]; then
  echo "HATA: pom.xml dosyası bulunamadı!"
  exit 1
fi

# Environment dosyalarını oluştur
echo "Tarayıcı bilgilerini environment dosyalarına ekleniyor..."

# Chrome için environment dosyası
cat > target/allure-results/chrome/environment.properties << EOL
Browser=Chrome
Selenium.Grid=true
EOL

# Firefox için environment dosyası
cat > target/allure-results/firefox/environment.properties << EOL
Browser=Firefox
Selenium.Grid=true
EOL

# Edge için environment dosyası
cat > target/allure-results/edge/environment.properties << EOL
Browser=Edge
Selenium.Grid=true
EOL

# Chrome testlerini başlat
echo "Chrome testleri başlatılıyor..."
echo "Chrome testleri $(date) tarihinde başladı." > target/allure-results/chrome/execution.log
mvn clean test -Duse_grid=true -Dbrowser=chrome -Dallure.results.directory=target/allure-results/chrome -Pcucumber.execution.parallel.enabled=true -Pcucumber.filter.tags="@TEST" -Pchrome &
CHROME_PID=$!

# Firefox testlerini başlat
echo "Firefox testleri başlatılıyor..."
echo "Firefox testleri $(date) tarihinde başladı." > target/allure-results/firefox/execution.log
mvn test -Duse_grid=true -Dbrowser=firefox -Dallure.results.directory=target/allure-results/firefox -Pcucumber.execution.parallel.enabled=true -Pcucumber.filter.tags="@TEST" -Pfirefox &
FIREFOX_PID=$!

# Edge testlerini başlat
echo "Edge testleri başlatılıyor..."
echo "Edge testleri $(date) tarihinde başladı." > target/allure-results/edge/execution.log
mvn test -Duse_grid=true -Dbrowser=edge -Dallure.results.directory=target/allure-results/edge -Pcucumber.execution.parallel.enabled=true -Pcucumber.filter.tags="@TEST" -Pedge &
EDGE_PID=$!

# Tüm test süreçlerinin tamamlanmasını bekle
echo "Tüm tarayıcılarda testler çalışıyor, lütfen bekleyin..."

wait $CHROME_PID
CHROME_STATUS=$?
echo "Chrome testleri tamamlandı. Çıkış kodu: $CHROME_STATUS"
echo "Chrome testleri $(date) tarihinde tamamlandı. Çıkış kodu: $CHROME_STATUS" >> target/allure-results/chrome/execution.log

wait $FIREFOX_PID
FIREFOX_STATUS=$?
echo "Firefox testleri tamamlandı. Çıkış kodu: $FIREFOX_STATUS"
echo "Firefox testleri $(date) tarihinde tamamlandı. Çıkış kodu: $FIREFOX_STATUS" >> target/allure-results/firefox/execution.log

wait $EDGE_PID
EDGE_STATUS=$?
echo "Edge testleri tamamlandı. Çıkış kodu: $EDGE_STATUS"
echo "Edge testleri $(date) tarihinde tamamlandı. Çıkış kodu: $EDGE_STATUS" >> target/allure-results/edge/execution.log

echo "Tüm test koşumları tamamlandı."

# Test sonuçlarına tarayıcı bilgilerini ekle
for file in target/allure-results/chrome/*.json; do
    if [ -f "$file" ]; then
        # Dosya içeriğinde "labels" dizisi arasına "browser: Chrome" ekle (jq varsa)
        if command -v jq &> /dev/null; then
            jq '.labels += [{"name": "browser", "value": "Chrome"}]' "$file" > "${file}.tmp" && mv "${file}.tmp" "$file"
        fi
    fi
done

for file in target/allure-results/firefox/*.json; do
    if [ -f "$file" ]; then
        # Dosya içeriğinde "labels" dizisi arasına "browser: Firefox" ekle (jq varsa)
        if command -v jq &> /dev/null; then
            jq '.labels += [{"name": "browser", "value": "Firefox"}]' "$file" > "${file}.tmp" && mv "${file}.tmp" "$file"
        fi
    fi
done

for file in target/allure-results/edge/*.json; do
    if [ -f "$file" ]; then
        # Dosya içeriğinde "labels" dizisi arasına "browser: Edge" ekle (jq varsa)
        if command -v jq &> /dev/null; then
            jq '.labels += [{"name": "browser", "value": "Edge"}]' "$file" > "${file}.tmp" && mv "${file}.tmp" "$file"
        fi
    fi
done

# Raporları oluştur
echo "Allure raporları oluşturuluyor..."
./generate-browser-reports.sh

# Genel sonucu belirle
if [ $CHROME_STATUS -eq 0 ] && [ $FIREFOX_STATUS -eq 0 ] && [ $EDGE_STATUS -eq 0 ]; then
  echo "Tüm testler başarıyla tamamlandı!"
  exit 0
else
  echo "Test koşumunda hatalar oluştu!"
  exit 1
fi 