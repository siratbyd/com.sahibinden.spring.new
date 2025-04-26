@echo off
SETLOCAL EnableDelayedExpansion

REM Raporlama klasörlerini temizle
ECHO Eski test sonuçları temizleniyor...
IF EXIST target\allure-results RD /S /Q target\allure-results
IF EXIST target\allure-results\chrome RD /S /Q target\allure-results\chrome
IF EXIST target\allure-results\firefox RD /S /Q target\allure-results\firefox
IF EXIST target\allure-results\edge RD /S /Q target\allure-results\edge
mkdir target\allure-results\chrome
mkdir target\allure-results\firefox
mkdir target\allure-results\edge

REM Selenium Grid'i başlat
ECHO Selenium Grid başlatılıyor...
call start-grid.bat

REM Tarayıcı profilleri için ön kontrol
IF NOT EXIST pom.xml (
  ECHO HATA: pom.xml dosyası bulunamadı!
  EXIT /B 1
)

REM Environment dosyalarını oluştur
ECHO Tarayıcı bilgilerini environment dosyalarına ekleniyor...

REM Chrome için environment dosyası
ECHO Browser=Chrome> target\allure-results\chrome\environment.properties
ECHO Selenium.Grid=true>> target\allure-results\chrome\environment.properties

REM Firefox için environment dosyası
ECHO Browser=Firefox> target\allure-results\firefox\environment.properties
ECHO Selenium.Grid=true>> target\allure-results\firefox\environment.properties

REM Edge için environment dosyası
ECHO Browser=Edge> target\allure-results\edge\environment.properties
ECHO Selenium.Grid=true>> target\allure-results\edge\environment.properties

REM Tarayıcı testlerini paralel başlat
ECHO Tarayıcı testleri paralel başlatılıyor...

REM Chrome testlerini başlat
ECHO Chrome testleri başlatılıyor...
ECHO Chrome testleri %date% %time% tarihinde başladı. > target\allure-results\chrome\execution.log
START "Chrome Tests" cmd /c "mvn clean test -Duse_grid=true -Dbrowser=chrome -Dallure.results.directory=target\allure-results\chrome -Dcucumber.plugin=io.qameta.allure.cucumber7jvm.AllureCucumber7Jvm -Dcucumber.execution.parallel.enabled=true -Dcucumber.filter.tags=@TEST -Pchrome && ECHO Chrome testleri tamamlandı. Çıkış kodu: %%ERRORLEVEL%% >> target\allure-results\chrome\execution.log"

REM Firefox testlerini başlat
ECHO Firefox testleri başlatılıyor...
ECHO Firefox testleri %date% %time% tarihinde başladı. > target\allure-results\firefox\execution.log
START "Firefox Tests" cmd /c "mvn test -Duse_grid=true -Dbrowser=firefox -Dallure.results.directory=target\allure-results\firefox -Dcucumber.plugin=io.qameta.allure.cucumber7jvm.AllureCucumber7Jvm -Dcucumber.execution.parallel.enabled=true -Dcucumber.filter.tags=@TEST -Pfirefox && ECHO Firefox testleri tamamlandı. Çıkış kodu: %%ERRORLEVEL%% >> target\allure-results\firefox\execution.log"

REM Edge testlerini başlat
ECHO Edge testleri başlatılıyor...
ECHO Edge testleri %date% %time% tarihinde başladı. > target\allure-results\edge\execution.log
START "Edge Tests" cmd /c "mvn test -Duse_grid=true -Dbrowser=edge -Dallure.results.directory=target\allure-results\edge -Dcucumber.plugin=io.qameta.allure.cucumber7jvm.AllureCucumber7Jvm -Dcucumber.execution.parallel.enabled=true -Dcucumber.filter.tags=@TEST -Pedge && ECHO Edge testleri tamamlandı. Çıkış kodu: %%ERRORLEVEL%% >> target\allure-results\edge\execution.log"

ECHO.
ECHO Tarayıcı testleri başlatıldı ve arkaplanda çalışıyor.
ECHO Testler tamamlandığında "generate-browser-reports.bat" çalıştırarak raporları görüntüleyebilirsiniz.

ENDLOCAL 