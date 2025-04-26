#!/bin/bash

# Eski container'ları durdur ve sil
echo "Eski containerlar durduruluyor ve temizleniyor..."
docker-compose -f docker-compose.jenkins.yml down
docker stop jenkins selenium-hub chrome firefox edge 2>/dev/null || true
docker rm jenkins selenium-hub chrome firefox edge 2>/dev/null || true
docker system prune -f --volumes

# Jenkins ve Selenium Grid kurulumunu başlat
echo "Jenkins ve Selenium Grid başlatılıyor..."
docker-compose -f docker-compose.jenkins.yml build --no-cache
if [ $? -ne 0 ]; then
    echo "Docker imajları oluşturulurken hata oluştu. Lütfen yukarıdaki hata mesajlarını kontrol edin."
    exit 1
fi

echo "Docker imajları başarıyla oluşturuldu. Şimdi konteynerler başlatılıyor..."
docker-compose -f docker-compose.jenkins.yml up -d

# Jenkins container durumunu kontrol et
echo "Jenkins container durumunu kontrol et..."
if ! docker ps | grep -q "jenkins"; then
    echo "Jenkins container başlatılamadı! Logs kontrol ediliyor..."
    docker-compose -f docker-compose.jenkins.yml logs jenkins
    exit 1
fi

# Jenkins'in hazır olmasını bekle
echo "Jenkins'in hazır olması bekleniyor..."
echo "Bu işlem birkaç dakika sürebilir, lütfen sabırla bekleyin..."

# Max 5 dakika (300 saniye) bekle
MAX_WAIT=300
START_TIME=$(date +%s)
CURRENT_TIME=$(date +%s)
ELAPSED_TIME=$((CURRENT_TIME - START_TIME))

while [ $ELAPSED_TIME -lt $MAX_WAIT ]; do
    if curl --output /dev/null --silent --head --fail http://localhost:8080; then
        echo -e "\nJenkins başarıyla başlatıldı!"
        break
    else
        # Her 10 saniyede bir bilgisi güncelle
        if [ $((ELAPSED_TIME % 10)) -eq 0 ]; then
            echo -ne "\rJenkins yükleniyor... Geçen süre: ${ELAPSED_TIME}s / ${MAX_WAIT}s"
        fi
        sleep 1
        CURRENT_TIME=$(date +%s)
        ELAPSED_TIME=$((CURRENT_TIME - START_TIME))
        
        # Container durumunu kontrol et
        if ! docker ps | grep -q "jenkins"; then
            echo -e "\nHata: Jenkins container durdu!"
            docker-compose -f docker-compose.jenkins.yml logs jenkins
            exit 1
        fi
    fi
done

if [ $ELAPSED_TIME -ge $MAX_WAIT ]; then
    echo -e "\nJenkins belirtilen süre içinde başlatılamadı (${MAX_WAIT}s). Jenkins log çıktısını kontrol edin:"
    docker-compose -f docker-compose.jenkins.yml logs jenkins
    echo "Jenkins hala başlıyor olabilir, tarayıcınızdan http://localhost:8080 adresini kontrol edin."
    exit 1
fi

# Jenkins initial admin password'ü göster
echo "İlk giriş için admin şifresi:"
PASSWORD_OUTPUT=$(docker exec jenkins cat /var/jenkins_home/secrets/initialAdminPassword 2>/dev/null)
if [ $? -eq 0 ] && [ ! -z "$PASSWORD_OUTPUT" ]; then
    echo "$PASSWORD_OUTPUT"
else
    echo "Şifre henüz oluşturulmamış olabilir. Birkaç dakika sonra tekrar deneyin veya şu komutu çalıştırın:"
    echo "docker exec jenkins cat /var/jenkins_home/secrets/initialAdminPassword"
fi
echo ""

# Selenium Grid durumunu kontrol et
echo "Selenium Grid durumu kontrol ediliyor..."
if curl -s http://localhost:4444/wd/hub/status | grep -q "\"ready\":true"; then
    echo "Selenium Grid hazır! Hub: http://localhost:4444"
else
    echo "Selenium Grid henüz hazır değil, biraz daha beklemeye devam edin."
    echo "Şu komutla durumu kontrol edebilirsiniz: docker-compose -f docker-compose.jenkins.yml logs selenium-hub"
fi

echo ""
echo "Jenkins'te aşağıdaki pipeline yapılandırmasını kullanabilirsiniz:"
echo "-------------------------------------------------------------------------"
echo 'pipeline {'
echo '    agent any'
echo '    stages {'
echo '        stage("Checkout") {'
echo '            steps {'
echo '                git url: "REPO_URL", branch: "main"'
echo '            }'
echo '        }'
echo '        stage("Run Tests") {'
echo '            steps {'
echo '                sh "mvn clean test -Duse_grid=true -Pchromerider,firefoxrider,edgerider"'
echo '            }'
echo '        }'
echo '        stage("Generate Allure Report") {'
echo '            steps {'
echo '                sh "mvn allure:report"'
echo '            }'
echo '        }'
echo '    }'
echo '    post {'
echo '        always {'
echo '            allure includeProperties: false, jdk: "", results: [[path: "target/allure-results"]]'
echo '        }'
echo '    }'
echo '}'
echo "-------------------------------------------------------------------------"
echo "Tarayıcınızdan http://localhost:8080 adresine giderek Jenkins'e erişebilirsiniz." 