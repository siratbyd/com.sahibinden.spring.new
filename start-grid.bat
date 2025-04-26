@echo off
SETLOCAL

ECHO Eski docker containerları temizleniyor...
docker-compose down
docker stop selenium-hub chrome firefox edge 2>nul
docker rm selenium-hub chrome firefox edge 2>nul

ECHO Selenium Grid ve node containerları başlatılıyor...
docker-compose up -d

ECHO Selenium Grid'in hazır olması bekleniyor...
ECHO Bu işlem biraz zaman alabilir, lütfen bekleyin...

REM Grid'in hazır olmasını bekle - en fazla 6 deneme
SET MAX_RETRIES=6
SET RETRY_COUNT=0
SET GRID_READY=false

:CHECK_GRID
IF %RETRY_COUNT% GEQ %MAX_RETRIES% GOTO GRID_FAILED
IF "%GRID_READY%"=="true" GOTO GRID_READY

ECHO Kontrol ediliyor... (%RETRY_COUNT%/%MAX_RETRIES%)
timeout /t 5 /nobreak > nul

REM Hub'ın çalışıp çalışmadığını kontrol et
docker ps | find "selenium-hub" | find "Up" > nul
IF %ERRORLEVEL% NEQ 0 (
  SET /A RETRY_COUNT+=1
  GOTO CHECK_GRID
)

REM Grid API'sini kontrol et
curl -s http://localhost:4444/wd/hub/status | findstr "\"ready\":true" > nul
IF %ERRORLEVEL% EQU 0 (
  SET GRID_READY=true
  GOTO GRID_READY
) ELSE (
  SET /A RETRY_COUNT+=1
  GOTO CHECK_GRID
)

:GRID_READY
ECHO Selenium Grid hazır! Hub: http://localhost:4444

REM Node'ların sayısını kontrol et
FOR /F "tokens=* USEBACKQ" %%F IN (`curl -s http://localhost:4444/grid/api/hub ^| findstr /r "\"nodeCount\":[0-9]*"`) DO (
  SET NODE_INFO=%%F
)
SET NODE_COUNT=%NODE_INFO:*:=%
IF NOT DEFINED NODE_COUNT SET NODE_COUNT=0

IF %NODE_COUNT% GTR 0 (
  ECHO Toplam %NODE_COUNT% tarayıcı node'u hazır:
  ECHO - Chrome
  ECHO - Firefox
  ECHO - Edge
  ECHO.
  ECHO Test koşumu başlatabilirsiniz.
  EXIT /B 0
) ELSE (
  ECHO Uyarı: Node'lar henüz kaydolmamış olabilir. Birkaç saniye daha bekleyin.
  ECHO Hub hazır, ancak node'ların bağlanması biraz daha zaman alabilir.
  ECHO docker-compose logs komutu ile durumu kontrol edebilirsiniz.
  EXIT /B 0
)

:GRID_FAILED
ECHO Selenium Grid başlatılamadı veya hazır değil! Lütfen aşağıdaki komutlarla kontrol edin:
ECHO docker-compose ps
ECHO docker-compose logs selenium-hub
EXIT /B 1

ENDLOCAL 