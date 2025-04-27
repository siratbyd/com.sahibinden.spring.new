#!/bin/bash

# target klasörünün içine giriyoruz
cd target || { echo "target klasörü bulunamadı"; exit 1; }

# Her *-report.html dosyasını bulup Google Chrome ile aç
for report in *-report.html; do
  if [ -f "$report" ]; then
    "/c/Program Files/Google/Chrome/Application/chrome.exe" "$PWD/$report"
  fi
done
