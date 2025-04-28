Feature: Sahibinden Yepy homepage


    @YEPY_HOMEPAGE @TEST
    Scenario: YEPY-1 Sahibinden Yepy Onboarding Page Control
        * Go to url
        * Click "CEREZLERI_KABUL_ET_BUTON"
        * Click "YEPY_BUTTON"
        * Wait 3 seconds
        * Switch new window
        * Assert that "YENILENMIS_TELEFON_AL_BUTON" element is visible
        * Assert that "KENDI_TELEFONUNU_SAT_BUTON" element is visible


#URL KONTROLÜ
    @YEPY_HOMEPAGE @TEST
    Scenario: YEPY-13 "Yenilenmiş Telefon Al" Page URL Control
        * Go to url
        * Click "CEREZLERI_KABUL_ET_BUTON"
        * Click "YEPY_BUTTON"
        * Wait 3 seconds
        * Switch new window
        * Click "YENILENMIS_TELEFON_AL_BUTON"
        * Wait 1 seconds
        * Assert that current URL is "https://www.sahibinden.com/yepy/yenilenmis-telefonlar"


    @YEPY_HOMEPAGE @TEST
    Scenario: YEPY-2 "Yenilenmiş Telefon Al" Homepage UI Control
        * Go to url
        * Click "CEREZLERI_KABUL_ET_BUTON"
        * Click "YEPY_BUTTON"
        * Wait 3 seconds
        * Switch new window
        * Click "YENILENMIS_TELEFON_AL_BUTON"
        * Wait 3 seconds
        * Assert that "BANNER_YEPY_LOGO" element is visible
        * Assert that element "BANNER_TEXT" contains text "Teknolojik cihaz alışverişinde yepyeni bir dönem!"
        * Assert that "BANNER_PHONE_PHOTO" element is visible
        * Assert that element "HEADER_YENILENMIS_CEP_TELEFONLARI" contains text "Yenilenmiş Cep Telefonları"
        * Assert that "KATEGORILER_SECTION" element is visible
        * Assert that "FIYAT_SECTION" element is visible
        * Assert that "KOZMETIK_DURUM_SECTION" element is visible
        * Assert that "RENK_SECTION" element is visible
        * Assert that "DEPOLAMA_KAPASITESI_SECTION" element is visible
        * Assert that "SEARCH_BUTTON" element is clickable
        * Assert that "GELISMIS_SIRALAMA_DROPDOWN" element is clickable



