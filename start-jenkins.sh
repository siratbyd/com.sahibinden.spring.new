#!/bin/bash

# Eski container'ları durdur ve sil
echo "Eski containerlar durduruluyor ve temizleniyor..."
docker-compose -f docker-compose.jenkins.yml down || true
docker stop jenkins selenium-hub chrome firefox edge 2>/dev/null || true
docker rm jenkins selenium-hub chrome firefox edge 2>/dev/null || true

# Docker Compose dosyası yoksa oluştur
if [ ! -f "docker-compose.jenkins.yml" ]; then
    echo "docker-compose.jenkins.yml dosyası oluşturuluyor..."
    cat > docker-compose.jenkins.yml << 'EOL'
version: '3'
services:
  jenkins:
    image: jenkins/jenkins:lts
    container_name: jenkins
    user: root
    privileged: true
    ports:
      - "8080:8080"
      - "50000:50000"
    volumes:
      - jenkins_home:/var/jenkins_home
      - /var/run/docker.sock:/var/run/docker.sock
      - /usr/bin/docker:/usr/bin/docker
    networks:
      - jenkins-selenium
    restart: unless-stopped

  selenium-hub:
    image: selenium/hub:4.10.0
    container_name: selenium-hub
    ports:
      - "4442:4442"
      - "4443:4443"
      - "4444:4444"
    environment:
      - GRID_MAX_SESSION=16
      - GRID_BROWSER_TIMEOUT=300
      - GRID_TIMEOUT=300
    networks:
      - jenkins-selenium
    restart: unless-stopped

  chrome:
    image: selenium/node-chrome:4.10.0
    container_name: chrome
    shm_size: 2g
    depends_on:
      - selenium-hub
    environment:
      - SE_EVENT_BUS_HOST=selenium-hub
      - SE_EVENT_BUS_PUBLISH_PORT=4442
      - SE_EVENT_BUS_SUBSCRIBE_PORT=4443
      - SE_NODE_MAX_SESSIONS=8
      - SE_NODE_OVERRIDE_MAX_SESSIONS=true
    volumes:
      - /dev/shm:/dev/shm
    networks:
      - jenkins-selenium
    restart: unless-stopped

  firefox:
    image: selenium/node-firefox:4.10.0
    container_name: firefox
    shm_size: 2g
    depends_on:
      - selenium-hub
    environment:
      - SE_EVENT_BUS_HOST=selenium-hub
      - SE_EVENT_BUS_PUBLISH_PORT=4442
      - SE_EVENT_BUS_SUBSCRIBE_PORT=4443
      - SE_NODE_MAX_SESSIONS=8
      - SE_NODE_OVERRIDE_MAX_SESSIONS=true
    volumes:
      - /dev/shm:/dev/shm
    networks:
      - jenkins-selenium
    restart: unless-stopped

  edge:
    image: selenium/node-edge:4.10.0
    container_name: edge
    shm_size: 2g
    depends_on:
      - selenium-hub
    environment:
      - SE_EVENT_BUS_HOST=selenium-hub
      - SE_EVENT_BUS_PUBLISH_PORT=4442
      - SE_EVENT_BUS_SUBSCRIBE_PORT=4443
      - SE_NODE_MAX_SESSIONS=8
      - SE_NODE_OVERRIDE_MAX_SESSIONS=true
    volumes:
      - /dev/shm:/dev/shm
    networks:
      - jenkins-selenium
    restart: unless-stopped

networks:
  jenkins-selenium:

volumes:
  jenkins_home:
EOL
    echo "docker-compose.jenkins.yml dosyası oluşturuldu."
fi

# Jenkins ve Selenium Grid kurulumunu başlat
echo "Jenkins ve Selenium Grid başlatılıyor..."
docker-compose -f docker-compose.jenkins.yml up -d

# Container'ların başlama durumunu kontrol et
echo "Container'ların durumu kontrol ediliyor..."
docker-compose -f docker-compose.jenkins.yml ps

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
else
    # Jenkins initial admin password'ü göster
    echo "İlk giriş için admin şifresi:"
    sleep 5 # Şifre dosyasının oluşturulması için biraz bekle

    PASSWORD_OUTPUT=$(docker exec jenkins cat /var/jenkins_home/secrets/initialAdminPassword 2>/dev/null)
    if [ $? -eq 0 ] && [ ! -z "$PASSWORD_OUTPUT" ]; then
        echo "$PASSWORD_OUTPUT"
    else
        echo "Şifre henüz oluşturulmamış olabilir. Birkaç dakika sonra tekrar deneyin veya şu komutu çalıştırın:"
        echo "docker exec jenkins cat /var/jenkins_home/secrets/initialAdminPassword"
    fi
fi

echo ""
# Selenium Grid durumunu kontrol et
echo "Selenium Grid durumu kontrol ediliyor..."
MAX_GRID_WAIT=120
START_GRID_TIME=$(date +%s)
GRID_READY=0

while [ $(($(date +%s) - START_GRID_TIME)) -lt $MAX_GRID_WAIT ]; do
    if curl -s http://localhost:4444/wd/hub/status | grep -q "\"ready\":true"; then
        echo "Selenium Grid hazır! Hub: http://localhost:4444"
        GRID_READY=1
        break
    else
        echo "Selenium Grid hazırlanıyor veya docker izinlerinden dolayı hazır olmasına rağmen bu süre hala devam ediyor, lütfen kontrol edin http://localhost:8080/ kontrol edin ... ($(($(($(date +%s) - START_GRID_TIME)))s / ${MAX_GRID_WAIT}s)"
        sleep 10
    fi
done

if [ $GRID_READY -eq 0 ]; then
    echo "Selenium Grid belirtilen süre içinde hazır duruma gelmedi."
    echo "Mevcut durumu kontrol etmek için: docker-compose -f docker-compose.jenkins.yml logs selenium-hub"
fi

echo ""
echo "Jenkins ve Selenium Grid başlatıldı."
echo "Tarayıcınızdan http://localhost:8080 adresine giderek Jenkins'e erişebilirsiniz."
echo "Selenium Grid konsolu: http://localhost:4444/ui"
echo ""
echo "Jenkins ayarları:"
echo "- Maven kurulumu için: 'Maven' adında bir Maven yüklemesi yapılandırın"
echo "- JDK kurulumu için: 'JDK' adında bir JDK yüklemesi yapılandırın"
echo "- Allure plugin'ini yükleyin ve yapılandırın"
echo ""
echo "Jenkins'te kullanabileceğiniz örnek pipeline yapılandırması:"
echo "-------------------------------------------------------------------------"
cat << 'EOL'
pipeline {
    agent any

    tools {
        maven 'Maven'
        jdk 'JDK'
    }

    parameters {
        choice(name: 'BROWSER_PROFILE', choices: ['parallel-browsers', 'chrome', 'firefox', 'edge', 'all-browsers'], description: 'Hangi tarayıcı profili ile testler koşulsun?')
        booleanParam(name: 'USE_GRID', defaultValue: true, description: 'Selenium Grid kullanılsın mı?')
        string(name: 'CUCUMBER_TAGS', defaultValue: '@TEST', description: 'Çalıştırılacak Cucumber tag\'leri (örn: @TEST veya @smoke or @regression)')
    }

    stages {
        stage('Checkout') {
            steps {
                checkout scm
            }
        }

        stage('Build ve Test') {
            steps {
                sh """
                    mvn clean test \
                    -P${params.BROWSER_PROFILE} \
                    -Duse_grid=${params.USE_GRID} \
                    -Dcucumber.filter.tags="${params.CUCUMBER_TAGS}" \
                    -Dallure.results.directory=target/allure-results
                """
            }
            post {
                always {
                    junit '**/target/surefire-reports/**/*.xml'
                }
            }
        }

        stage('Allure Rapor') {
            steps {
                sh 'mkdir -p target/allure-results || true'
                allure([
                    includeProperties: false,
                    jdk: '',
                    reportBuildPolicy: 'ALWAYS',
                    results: [[path: 'target/allure-results']]
                ])
            }
        }
    }
}
EOL
echo "-------------------------------------------------------------------------"
echo "Jenkins ve Selenium Grid başarıyla kuruldu!"
