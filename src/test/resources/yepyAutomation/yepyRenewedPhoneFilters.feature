Feature: Sahibinden Yepy Renewed Phone Filters


  @YEPY_FILTERS @TEST
  Scenario: Sahibinden Yepy Filter Phone Number and Actual Phone Number Control
    * Go to url
    * Click "CEREZLERI_KABUL_ET_BUTON"
    * Click "YEPY_BUTTON"
    * Wait 3 seconds
    * Switch new window
    * Click "YENILENMIS_TELEFON_AL_BUTON"
    * Wait 3 seconds
    * Save number from element "AVAILABLE_PHONE_NUMBER_ON_FILTER"
    * Click "FILTER_APPLE_BUTTON"
    * Wait 3 seconds
    * Assert that "APPLE_SECTION_TEXTBUTTON" element is visible
    * Assert that element "APPLE_SECTION_TEXTBUTTON" contains text "Apple"
    * Assert that element "YENILENMIS_APPLE_HEADER" contains text "Yenilenmiş Apple"
    * Assert that previous element number equal "AVAILABLE_PHONE_NUMBER_ON_HEADER" this number


  @YEPY_FILTERS @TEST
  Scenario: Sahibinden Yepy Min Price Control
    * Go to url
    * Click "CEREZLERI_KABUL_ET_BUTON"
    * Click "YEPY_BUTTON"
    * Wait 2 seconds
    * Switch new window
    * Click "YENILENMIS_TELEFON_AL_BUTON"
    #* Wait 3 seconds
    * Click "FILTER_APPLE_BUTTON"
    * Wait 1 seconds
    * Write "12000" to "EN_DUSUK_FIYAT_TEXT_BOX" input field
    * Click "SEARCH_BUTTON"
   # * Wait 1 seconds
    * Click "SORTING_ARROW_BUTTON"
    * Click "FIYAT_DUSUKTEN_YUKSEGE_BUTTON"
    * Assert that number of "FIRST_ITEM_PRICE" element is greater than or equal "12000"

  @YEPY_FILTERS @TEST
  Scenario: Sahibinden Yepy Max Price Control
    * Go to url
    * Click "CEREZLERI_KABUL_ET_BUTON"
    * Click "YEPY_BUTTON"
    * Wait 2 seconds
    * Switch new window
    * Click "YENILENMIS_TELEFON_AL_BUTON"
    #* Wait 3 seconds
    * Click "FILTER_APPLE_BUTTON"
    * Wait 1 seconds
    * Write "12000" to "EN_YUKSEK_FIYAT_TEXT_BOX" input field
    * Click "SEARCH_BUTTON"
   # * Wait 1 seconds
    * Click "SORTING_ARROW_BUTTON"
    * Click "FIYAT_YUKSEKTEN_DUSUGE_BUTTON"
    * Wait 3 seconds
    * Assert that number of "FIRST_ITEM_PRICE" element is lower than or equal "12000"




