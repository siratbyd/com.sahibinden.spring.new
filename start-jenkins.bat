@echo off
SETLOCAL EnableDelayedExpansion

ECHO Eski containerlar durduruluyor ve temizleniyor...
docker-compose -f docker-compose.jenkins.yml down
docker stop jenkins selenium-hub chrome firefox edge 2>nul
docker rm jenkins selenium-hub chrome firefox edge 2>nul
docker system prune -f --volumes

ECHO Jenkins ve Selenium Grid başlatılıyor...
docker-compose -f docker-compose.jenkins.yml build --no-cache
IF %ERRORLEVEL% NEQ 0 (
    ECHO Docker imajları oluşturulurken hata oluştu. Lütfen yukarıdaki hata mesajlarını kontrol edin.
    EXIT /B 1
)

ECHO Docker imajları başarıyla oluşturuldu. Şimdi konteynerler başlatılıyor...
docker-compose -f docker-compose.jenkins.yml up -d

REM Jenkins container durumunu kontrol et
ECHO Jenkins container durumunu kontrol et...
docker ps | find "jenkins" > nul
IF %ERRORLEVEL% NEQ 0 (
    ECHO Jenkins container başlatılamadı! Logs kontrol ediliyor...
    docker-compose -f docker-compose.jenkins.yml logs jenkins
    EXIT /B 1
)

ECHO Jenkins'in hazır olması bekleniyor...
ECHO Bu işlem birkaç dakika sürebilir, lütfen sabırla bekleyin...

REM Max 5 dakika (300 saniye) bekle - Windows'ta biraz farklı yaklaşım
SET MAX_WAIT=30
SET COUNTER=0

:WAIT_LOOP
IF %COUNTER% GEQ %MAX_WAIT% GOTO TIMEOUT
curl --output nul --silent --head --fail http://localhost:8080
IF %ERRORLEVEL% EQU 0 (
    ECHO.
    ECHO Jenkins başarıyla başlatıldı!
    GOTO JENKINS_READY
)

REM Container durumunu kontrol et
docker ps | find "jenkins" > nul
IF %ERRORLEVEL% NEQ 0 (
    ECHO.
    ECHO Hata: Jenkins container durdu!
    docker-compose -f docker-compose.jenkins.yml logs jenkins
    EXIT /B 1
)

SET /A COUNTER+=1
ECHO Bekleniyor... (%COUNTER%/%MAX_WAIT%) 
timeout /t 10 /nobreak > nul
GOTO WAIT_LOOP

:TIMEOUT
ECHO.
ECHO Jenkins belirtilen süre içinde başlatılamadı. Jenkins log çıktısını kontrol edin:
docker-compose -f docker-compose.jenkins.yml logs jenkins
ECHO Jenkins hala başlıyor olabilir, tarayıcınızdan http://localhost:8080 adresini kontrol edin.
EXIT /B 1

:JENKINS_READY
ECHO.
ECHO İlk giriş için admin şifresi:
docker exec jenkins cat /var/jenkins_home/secrets/initialAdminPassword 2>nul
IF %ERRORLEVEL% NEQ 0 (
    ECHO Şifre henüz oluşturulmamış olabilir. Birkaç dakika sonra tekrar deneyin veya şu komutu çalıştırın:
    ECHO docker exec jenkins cat /var/jenkins_home/secrets/initialAdminPassword
)
ECHO.

ECHO Selenium Grid durumu kontrol ediliyor...
curl -s http://localhost:4444/wd/hub/status | findstr "\"ready\":true" > nul
IF %ERRORLEVEL% EQU 0 (
    ECHO Selenium Grid hazır! Hub: http://localhost:4444
) ELSE (
    ECHO Selenium Grid henüz hazır değil, biraz daha beklemeye devam edin.
    ECHO Şu komutla durumu kontrol edebilirsiniz: docker-compose -f docker-compose.jenkins.yml logs selenium-hub
)

ECHO.
ECHO Jenkins'te aşağıdaki pipeline yapılandırmasını kullanabilirsiniz:
ECHO -------------------------------------------------------------------------
ECHO pipeline {
ECHO     agent any
ECHO     stages {
ECHO         stage("Checkout") {
ECHO             steps {
ECHO                 git url: "REPO_URL", branch: "main"
ECHO             }
ECHO         }
ECHO         stage("Run Tests") {
ECHO             steps {
ECHO                 sh "mvn clean test -Duse_grid=true -Pchromerider,firefoxrider,edgerider"
ECHO             }
ECHO         }
ECHO         stage("Generate Allure Report") {
ECHO             steps {
ECHO                 sh "mvn allure:report"
ECHO             }
ECHO         }
ECHO     }
ECHO     post {
ECHO         always {
ECHO             allure includeProperties: false, jdk: "", results: [[path: "target/allure-results"]]
ECHO         }
ECHO     }
ECHO }
ECHO -------------------------------------------------------------------------
ECHO Tarayıcınızdan http://localhost:8080 adresine giderek Jenkins'e erişebilirsiniz.

ENDLOCAL 