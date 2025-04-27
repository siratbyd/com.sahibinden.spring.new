#!/bin/bash

# Eski containerları temizle
echo "Eski docker containerları temizleniyor..."
docker-compose down
docker stop selenium-hub chrome firefox edge 2>/dev/null || true
docker rm selenium-hub chrome firefox edge 2>/dev/null || true

# Docker containerları başlat
echo "Selenium Grid ve node containerları başlatılıyor..."
docker-compose up -d

# Hazır olana kadar bekle
echo "Selenium Grid'in hazır olması bekleniyor..."
echo "Bu işlem biraz zaman alabilir, lütfen bekleyin..."

# Grid'in hazır olmasını bekle - 30 saniye veya 6 deneme
MAX_RETRIES=6
RETRY_COUNT=0
GRID_READY=false

while [ $RETRY_COUNT -lt $MAX_RETRIES ] && [ "$GRID_READY" = false ]; do
    echo "Kontrol ediliyor... (${RETRY_COUNT}/${MAX_RETRIES})"
    sleep 5
    
    # Önce Hub'ın çalışıp çalışmadığını kontrol et
    if docker ps | grep "selenium-hub" | grep "Up" > /dev/null; then
        # Grid API'sini kontrol et
        if curl -s http://localhost:4444/wd/hub/status | grep "\"ready\":true" > /dev/null; then
            GRID_READY=true
        fi
    fi
    
    RETRY_COUNT=$((RETRY_COUNT+1))
done

# Sonucu kontrol et
if [ "$GRID_READY" = true ]; then
    echo "Selenium Grid hazır! Hub: http://localhost:4444"
    
    # Node'ların durumunu kontrol et
    echo "Tarayıcı node'ları kontrol ediliyor..."
    NODE_COUNT=$(curl -s http://localhost:4444/grid/api/hub | grep -o '"nodeCount":[0-9]*' | cut -d':' -f2)
    
    if [ "$NODE_COUNT" -gt 0 ]; then
        echo "Toplam $NODE_COUNT tarayıcı node'u hazır:"
        echo "- Chrome"
        echo "- Firefox"
        echo "- Edge"
        echo ""
        echo "Test koşumu başlatabilirsiniz."
        exit 0
    else
        echo "Uyarı: Node'lar henüz kaydolmamış olabilir. Birkaç saniye daha bekleyin."
        echo "Hub hazır, ancak node'ların bağlanması biraz daha zaman alabilir."
        echo "docker-compose logs komutu ile durumu kontrol edebilirsiniz."
        exit 0
    fi
else
    echo "Selenium Grid başlatılamadı veya Docker izinleri verilmediği için kontrol edilemiyor! Lütfen aşağıdaki komutlarla manual kontrol edin, containerlar görünüyorsa sorun yoktur:"
    echo "docker-compose ps"
    echo "docker-compose logs selenium-hub"
    echo "Selenium Hub: http://localhost:4444/ui"
    exit 1
fi 