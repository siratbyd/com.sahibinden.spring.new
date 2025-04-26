@echo off
SETLOCAL

REM Raporların oluşturulmuş olup olmadığını kontrol et
IF NOT EXIST target\allure-report-chrome (
    IF NOT EXIST target\allure-report-firefox (
        IF NOT EXIST target\allure-report-edge (
            ECHO Hata: Rapor dizinleri bulunamadı.
            ECHO Önce 'generate-browser-reports.bat' script'ini çalıştırarak raporları oluşturun.
            EXIT /B 1
        )
    )
)

REM Kullanıcıya tarayıcı seçeneklerini göster
ECHO Görüntülemek istediğiniz tarayıcı raporunu seçin:
ECHO 1. Chrome Raporu
ECHO 2. Firefox Raporu
ECHO 3. Edge Raporu
ECHO 4. Tüm Raporlar (ayrı sekmelerde)
ECHO 5. Çıkış

REM Seçim yap
SET /P choice=Seçiminiz (1-5): 

IF "%choice%"=="1" (
    IF EXIST target\allure-report-chrome (
        ECHO Chrome raporu görüntüleniyor...
        START /B cmd /c allure open target\allure-report-chrome
    ) ELSE (
        ECHO Chrome raporu bulunamadı!
    )
) ELSE IF "%choice%"=="2" (
    IF EXIST target\allure-report-firefox (
        ECHO Firefox raporu görüntüleniyor...
        START /B cmd /c allure open target\allure-report-firefox
    ) ELSE (
        ECHO Firefox raporu bulunamadı!
    )
) ELSE IF "%choice%"=="3" (
    IF EXIST target\allure-report-edge (
        ECHO Edge raporu görüntüleniyor...
        START /B cmd /c allure open target\allure-report-edge
    ) ELSE (
        ECHO Edge raporu bulunamadı!
    )
) ELSE IF "%choice%"=="4" (
    REM Tüm raporları ayrı sekmelerde aç
    IF EXIST target\allure-report-chrome (
        ECHO Chrome raporu açılıyor...
        START /B cmd /c allure open target\allure-report-chrome
        timeout /t 2 > nul
    )
    
    IF EXIST target\allure-report-firefox (
        ECHO Firefox raporu açılıyor...
        START /B cmd /c allure open target\allure-report-firefox
        timeout /t 2 > nul
    )
    
    IF EXIST target\allure-report-edge (
        ECHO Edge raporu açılıyor...
        START /B cmd /c allure open target\allure-report-edge
    )
) ELSE IF "%choice%"=="5" (
    ECHO Çıkış yapılıyor...
    EXIT /B 0
) ELSE (
    ECHO Geçersiz seçim! Lütfen 1-5 arasında bir değer girin.
)

ENDLOCAL 