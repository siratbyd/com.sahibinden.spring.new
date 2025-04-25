Feature: Sahibinden Yepy homepage


    @YEPY_HOMEPAGE @TESTt
    Scenario: Sahibinden Yepy Onboarding Page Control
        * Go to url
        * Click "CEREZLERI_KABUL_ET_BUTON"
        * Click "YEPY_BUTTON"
        * Wait 3 seconds
        * Switch new window
        * Assert that "YENILENMIS_TELEFON_AL_BUTON" element is visible
        * Assert that "KENDI_TELEFONUNU_SAT_BUTON" element is visible

    @YEPY_HOMEPAGE @TESTt
    Scenario: Sahibinden Yepy "Yenilenmiş Telefon Al" Landing Page UI Control
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



