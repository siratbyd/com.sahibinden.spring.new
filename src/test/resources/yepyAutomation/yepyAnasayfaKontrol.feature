Feature: Sahibinden Yepy homepage


    @test
    Scenario: Sahibinden Yepy Onboarding Page Control
        * Go to url
        * Click "CEREZLERI_KABUL_ET_BUTON"
        * Assert that "YEPY_BUTTON" element is visible
        * Click "YEPY_BUTTON"
        * Assert that "YENILENMIS_TELEFON_AL_BUTON" element is visible
       # * Assert that "KENDI_TELEFONUNU_SAT_BUTON" element is visible

    @test
    Scenario: Sahibinden Yepy Homepage Control
        * Go to url
        * Click "CEREZLERI_KABUL_ET_BUTON"
        * Assert that "YEPY_BUTTON" element is visible
        * Click "YEPY_BUTTON"
        * Wait 3 seconds
        * Click "YENILENMIS_TELEFON_AL_BUTON"
        * Wait 3 seconds
        * Assert that "YEPY_LOGO" element is visible



