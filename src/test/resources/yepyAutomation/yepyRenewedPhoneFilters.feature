Feature: Sahibinden Yepy Renewed Phone Filters
  


  #HER SENARYO ÖNCESİNDE BU STEPLER KOŞULUR
  Background:
    * Go to url
    * Click "CEREZLERI_KABUL_ET_BUTON"
    * Click "YEPY_BUTTON"
    * Wait 2 seconds
    * Switch new window
    * Click "YENILENMIS_TELEFON_AL_BUTON"


  @YEPY_FILTERS @TEST
  Scenario: Filter Phone Number and Actual Phone Number Control
    * Wait 3 seconds
    * Save number from element "AVAILABLE_PHONE_NUMBER_ON_FILTER"
    * Click "FILTER_APPLE_BUTTON"
    * Wait 3 seconds
    * Assert that "APPLE_SECTION_TEXTBUTTON" element is visible
    * Assert that element "APPLE_SECTION_TEXTBUTTON" contains text "Apple"
    * Assert that element "YENILENMIS_APPLE_HEADER" contains text "Yenilenmiş Apple"
    * Assert that previous element number equal "AVAILABLE_PHONE_NUMBER_ON_HEADER" this number


  @YEPY_FILTERS @TEST
  Scenario: Min Price Control
    * Click "FILTER_APPLE_BUTTON"
    * Wait 1 seconds
    * Write "12000" to "EN_DUSUK_FIYAT_TEXT_BOX" input field
    * Click "SEARCH_BUTTON"
    * Wait 3 seconds
    * Click "SORTING_ARROW_BUTTON"
    * Click "FIYAT_DUSUKTEN_YUKSEGE_BUTTON"
    * Assert that number of "FIRST_ITEM_PRICE" element is greater than or equal "12000"

  @YEPY_FILTERS @TEST
  Scenario: Max Price Control
    * Click "FILTER_APPLE_BUTTON"
    * Wait 1 seconds
    * Write "12000" to "EN_YUKSEK_FIYAT_TEXT_BOX" input field
    * Click "SEARCH_BUTTON"
    * Wait 3 seconds
    * Click "SORTING_ARROW_BUTTON"
    * Click "FIYAT_YUKSEKTEN_DUSUGE_BUTTON"
    * Wait 3 seconds
    * Assert that number of "FIRST_ITEM_PRICE" element is lower than or equal "12000"


#DÜŞÜKTEN YÜKSEĞE FİYAT KONTROLÜ
  @YEPY_FILTERS @TEST
  Scenario: Ascending Sorting Control
    * Click "FILTER_APPLE_BUTTON"
    * Wait 3 seconds
    * Click "SORTING_ARROW_BUTTON"
    * Click "FIYAT_DUSUKTEN_YUKSEGE_BUTTON"
    * Wait 3 seconds
    * Assert that first 10 product prices are sorted in ascending order


#YÜKSEKTEN DÜŞÜĞE FİYAT KONTROLÜ
  @YEPY_FILTERS @TEST
  Scenario: Descending Sorting Control
    * Click "FILTER_APPLE_BUTTON"
    * Wait 3 seconds
    * Click "SORTING_ARROW_BUTTON"
    * Click "FIYAT_YUKSEKTEN_DUSUGE_BUTTON"
    * Wait 3 seconds
    * Assert that first 10 product prices are sorted in descending order


#LISTEDEKİ ÜRÜNLE, ÜRÜNE TIKLAYINCA ÇIKAN DETAYDAKİ ÜRÜNÜN AYNI OLUP OLMADIĞI KONTROLÜ
  @YEPY_FILTERS @TEST
  Scenario: Comparing Lısted Product and Product Detail Infos
    * Wait 3 seconds
    * Save the texts from "FIRST_PRODUCT_MODEL_ISMI_LIST_PAGE" element
    * Save the texts from "FIRST_PRODUCT_RENK_AND_HAFIZA_LIST_PAGE" element
    * Save the texts from "FIRST_PRODUCT_FIYAT_LIST_PAGE" element
    * Click "FIRST_PRODUCT_CLICK"
    * Wait 3 seconds
    * Assert that stored text from "FIRST_PRODUCT_MODEL_ISMI_LIST_PAGE" is contained in "FIRST_PRODUCT_RENK_MODELISMI_HAFIZA_DETAIL_PAGE" element
    * Assert that stored text from "FIRST_PRODUCT_RENK_AND_HAFIZA_LIST_PAGE" is contained in "FIRST_PRODUCT_RENK_MODELISMI_HAFIZA_DETAIL_PAGE" element
    * Assert that stored text from "FIRST_PRODUCT_FIYAT_LIST_PAGE" is contained in "FIRST_PRODUCT_FIYAT_DETAIL_PAGE" element


  @YEPY_FILTERS @TEST
  Scenario: "Can Not Found Devices" Warning Control
    * Wait 1 seconds
    * Write "999.999" to "EN_DUSUK_FIYAT_TEXT_BOX" input field
    * Click "SEARCH_BUTTON"
    * Wait 2 seconds
    * Assert that element "NOT_FOUND_DEVICES_WARNING_HEADER" contains text "Aramanıza uygun cihaz bulunamadı"
    * Assert that element "NOT_FOUND_DEVICES_WARNING_TEXT" contains text "Farklı bir cihaz veya özellikleri için tekrar arama yapabilirsiniz."
    * Assert that "NOT_FOUND_DEVICES_WARNING_LOGO" element is visible
    * Click "TUMUNU_TEMIZLE_BUTTON"
    * Wait 2 seconds
    * Assert that "FIRST_PRODUCT_ON_LIST" element is visible


  @YEPY_FILTERS @TEST
  Scenario: Color Filter Control
    * Click "COLOR_FILTER_WHITE"
    * Click "SEARCH_BUTTON"
    * Wait 2 seconds
    * Assert that all product titles contain only color "Beyaz"
    * Assert that element "COLOR_FILTER_WHITE_HEADER" contains text "Beyaz"
    * Click "FIRST_PRODUCT_ON_LIST"
    * Wait 2 seconds
    * Assert that "WHITE_COLOR_CIRCLE_AT_PRODUCT_DETAIL" element is selected


  @YEPY_FILTERS @TEST
  Scenario: Cosmetic Situation Filter Control
    * Click "KOZMETIK_DURUM_MUKEMMEL_BUTON_FILTER"
    * Click "SEARCH_BUTTON"
    * Wait 2 seconds
    * Assert that element "KOZMETIK_DURUM_MUKEMMEL_HEADER" contains text "Mükemmel"
    * Click "FIRST_PRODUCT_ON_LIST"
    * Wait 3 seconds
    * Assert that "KOZMETIK_DURUM_MUKEMMEL_BUTON_PRODUCT_DETAIL" element is selected


  @YEPY_FILTERS @TEST
  Scenario: Storage Filter Control
    * Wait 3 seconds
    * Scroll to "STORAGE_FILTER_64GB_BUTTON" element
    * Click "STORAGE_FILTER_64GB_BUTTON"
    * Click "SEARCH_BUTTON"
    * Wait 2 seconds
    * Assert that all product titles contain only color "64 GB"
    * Assert that element "STORAGE_FILTER_64GB_HEADER" contains text "64 GB"
    * Click "FIRST_PRODUCT_ON_LIST"
    * Wait 2 seconds
    * Assert that "STORAGE_64GB_BUTTON_PRODUCT_DETAIL" element is selected

















