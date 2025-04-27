# Sahibinden Test Otomasyon Projesi

## Proje Hakkında

Bu proje, Sahibinden.com platformunun test otomasyonunu gerçekleştirmek için oluşturulmuş bir BDD (Behavior Driven Development) tabanlı test çerçevesidir. Java programlama dili ve çeşitli teknolojiler kullanılarak, özellikle yenilenmiş telefon pazarı (Yepy) gibi platformların test edilmesini sağlar.

## Kullanılan Teknolojiler

- **Java:** Projenin ana programlama dili
- **Selenium:** Web tarayıcı otomasyonu için kullanılmaktadır
- **Selenium Grid:** Paralel test çalıştırma için dağıtık test altyapısı
- **Docker:** Konteynerleştirilmiş test ortamları için
- **Jenkins:** Sürekli entegrasyon ve dağıtım (CI/CD) için
- **Maven:** Bağımlılık yönetimi ve proje yapılandırması
- **JUnit 5:** Java test çerçevesi
- **Cucumber:** BDD yaklaşımı ve Gherkin sözdizimi ile test senaryoları oluşturma
- **Spring Boot:** Bağımlılık enjeksiyonu ve test bileşenlerinin yönetimi
- **Log4j:** Loglama için kullanılmaktadır

## Proje Yapısı

```
├── src
│   ├── main
│   │   └── java
│   │       └── com.sahibinden.auto.spring
│   │           └── TestApplication.java
│   └── test
│       ├── java
│       │   ├── annotations
│       │   ├── base
│       │   │   ├── AutomationMethods.java
│       │   │   └── TestBase.java
│       │   ├── objectRepository
│       │   ├── runnerClass
│       │   │   ├── ChromeTestRunner.java
│       │   │   ├── EdgeTestRunner.java
│       │   │   ├── FirefoxTestRunner.java
│       │   │   └── TestRunner.java
│       │   ├── stepDefinitions
│       │   │   ├── CucumberSpringConfig.java
│       │   │   ├── GeneralSteps.java
│       │   │   └── Hooks.java
│       │   └── utils
│       │       ├── AllureReportGenerator.java
│       │       ├── AllureReportLocator.java
│       │       ├── AllureTestRunner.java
│       │       ├── ConfigManager.java
│       │       ├── DataManager.java
│       │       ├── DriverManager.java
│       │       └── ElementManager.java
│       └── resources
│           ├── application.properties
│           ├── allure.properties
│           ├── junit-platform.properties
│           └── yepyAutomation
│               ├── yepyHomepage.feature
│               └── yepyRenewedPhoneFilters.feature
├── docker-compose.yml
├── docker-compose.jenkins.yml
├── Dockerfile.jenkins
├── Jenkinsfile
├── pom.xml
├── start-grid.sh
├── start-grid.bat
├── start-jenkins.sh
├── start-jenkins.bat
├── run-parallel-tests.sh
├── run-parallel-tests.bat
└── Open-reports.sh
```

## Proje Bileşenleri ve Sorumlulukları

### 1. Spring Entegrasyonu

TestApplication.java sınıfı Spring Boot entegrasyonunu sağlayarak, projenin bileşenlerinin etkin bir şekilde yönetilmesine olanak tanır. @SpringBootApplication annotasyonu ile uygulamanın taranacak paketleri belirlenir.

- **Bağımlılık Enjeksiyonu (Dependency Injection):** WebDriver örnekleri ve diğer test bileşenlerinin otomatik olarak yönetilmesi
- **Bileşen Yaşam Döngüsü Yönetimi:** Test bileşenlerinin yaşam döngüsünün (lifecycle) kontrol edilmesi
- **Konfigürasyon Yönetimi:** Test ortamı konfigürasyonlarının merkezi yönetimi
- **Cucumber Entegrasyonu:** Cucumber ile Spring Boot entegrasyonu sağlayarak BDD test adımlarının yönetimi
- 

### 2. Test Koşucuları (Test Runners)

`runnerClass` paketi altında bulunan runner sınıfları, farklı tarayıcılar için test senaryolarını çalıştırmak üzere yapılandırılmıştır:

- **TestRunner:** Genel test koşucusu
- **ChromeTestRunner:** Chrome tarayıcı testleri için
- **EdgeTestRunner:** Edge tarayıcı testleri için
- **FirefoxTestRunner:** Firefox tarayıcı testleri için

JUnit 5 ve Cucumber entegrasyonu ile çalışan bu sınıflar, test senaryolarının paralel olarak çalıştırılmasını ve sonuçların Allure raporlarına aktarılmasını sağlar.

Not:IntelliJ, @Suite ile işaretlenmiş sınıflara sağ tıklayıp Run dediğinde her defasında yeni bir temporary run config oluşturduğu ve VM configleri içermediği için testlerimizi paralel koşuma uygun olması için sağ üstteki dropdown'dan başlatmamız gerekmekte.İlgili run configurationlar .idea/runConfigurations/ içerisinde

### 3. Tarayıcı Yönetimi

`utils` paketi altındaki DriverManager sınıfı, farklı tarayıcılar için WebDriver örneklerinin yönetimini sağlar. Bu sınıf:

- Thread-safe yapı ile paralel test çalıştırma desteği
- Local veya Selenium Grid üzerinde tarayıcı başlatma imkanı
- Chrome, Firefox, Edge, Opera ve IE gibi tarayıcılar için destek sağlar (Projemizde Opera kullanmadım)

### 4. Temel Test Fonksiyonları

`base` paketi altındaki sınıflar, test otomasyonu için temel yapıyı oluşturur:

- **TestBase:** Testlerin yaşam döngüsünü (setup/teardown) yöneten temel sınıf
- **AutomationMethods:** Yaygın otomasyon fonksiyonlarını içeren yardımcı metotları burda topladım

### 5. Test Adımları (Step Definitions)

`stepDefinitions` paketi altındaki sınıflar, Cucumber feature dosyalarındaki adımları gerçek koda dönüştüren metotları içerir:

- **GeneralSteps:** Genel test adımlarını içerir
- **Hooks:** Test senaryoları öncesinde ve sonrasında çalışacak metodları tanımlar
- **CucumberSpringConfig:** Cucumber ve Spring entegrasyonunu sağlar

### 6. Özellik Dosyaları (Feature Files)

`resources/yepyAutomation` dizini altındaki feature dosyaları, Gherkin sözdizimi ile yazılmış test senaryolarını içerir:

- **yepyHomepage.feature:** Yepy ana sayfa testleri
- **yepyRenewedPhoneFilters.feature:** Yenilenmiş telefon filtreleri testleri

### 7. Docker ve Selenium Grid Entegrasyonu

Docker Compose dosyaları (`docker-compose.yml` ve `docker-compose.jenkins.yml`), Selenium Grid ortamını konteynerleştirmek için kullanılır. Bu dosyalar:

- Selenium Hub ve Node'ları yapılandırır
- Chrome, Firefox ve Edge için tarayıcı konteynerleri oluşturur
- Test ortamının tutarlı ve izole çalışmasını sağlar

### 8. Jenkins Entegrasyonu

`Jenkinsfile` ve `Dockerfile.jenkins` dosyaları, CI/CD pipeline'ını yapılandırır:

- Test koşturma parametreleri (tarayıcı profili, Cucumber tag'leri)
- Test raporlama entegrasyonu
- Sonuçların toplanması ve görselleştirilmesi

Not: Güncel projenin durumunda dependency uyumsuzlukları ve proje içerisindeki version uyumsuzlukları dolayısyıla %100 sağlıklı çalışmamaktadır

## Kullanım

### 1. Yerel Ortamda Çalıştırma

Test koşumlarına başlamadan önce testlerimizi neredeyse her zaman IDE üzerinden çalıştırıyoruz. Test Runner kısmında da bahsettiğim gibi IntelliJ, @Suite ile işaretlenmiş sınıflara sağ tıklayıp Run dediğinde her defasında yeni bir temporary run config oluşturduğu ve VM configleri içermediği için testlerimizi paralel koşuma uygun olması için sağ üstteki dropdown'dan başlatmamız gerekiyor aynı durum mvn clean test gibi komutlar için de geçerli.

İlgili runner configürasyonları aşağıdaki dosyada xml olarak tanımlıdır. Örnek;


Runner Configurationları



.idea/runConfiguraitons
```
<component name="ProjectRunConfigurationManager">
  <configuration default="false" name="ChromeTestRunner" type="JUnit" factoryName="JUnit" nameIsGenerated="true">
    <module name="com.sahibinden.auto.spring.new" />
    <extension name="coverage">
      <pattern>
        <option name="PATTERN" value="runnerClass.*" />
        <option name="ENABLED" value="true" />
      </pattern>
    </extension>
    <option name="PACKAGE_NAME" value="runnerClass" />
    <option name="MAIN_CLASS_NAME" value="runnerClass.ChromeTestRunner" />
    <option name="METHOD_NAME" value="" />
    <option name="TEST_OBJECT" value="class" />
    <option name="VM_PARAMETERS" value="-Dbrowser=chrome -Dmax.parallel.browsers=3" />
    <method v="2">
      <option name="Make" enabled="true" />
    </method>
  </configuration>
</component>

```

a)Normal Test Koşumu

Öncelikle feature dosyalarımızdaki her bir senaryomuza eklemiş olduğumuz cucumber filter tag'lerinden hangisini barındıran testleri koşmamız gerektiğiniz junit-platform.properties dosyasında tanımlamamız gerekiyor.


src/test/resources/junit-platform.properties
```
---
cucumber.filter.tags = @TEST
---

```


Koşmak istediğimiz cucumber tag
![image](https://github.com/user-attachments/assets/c92d3c7e-b728-4741-8524-35449b6222f1)


Konfigürasyonları kayıtlı olan runnerclass'larımıza (testi koşmak istediğimiz browsera) IntellijIDE'nin sağ üst tarafındaki dropdown penceresinden erişilip seçim yaparak istenen browserda test koşulabilir. Örn;
FirefoxTestRunner
EdgeTestRunner
ChromeTestRunner

![image](https://github.com/user-attachments/assets/86368290-f505-451b-ac47-1f80b1c7af8b)


b)Browser Bazlı Paralel Test Koşumu

Kullanıcı aynı adımları takip ederek sağ üstteki dropdown menüsü üzerinden "Run All Browsers" seçeneği ile testi başlatırsa her 3 browserda da test koşumu başarıyla başlatılmış olunur.

![image](https://github.com/user-attachments/assets/86368290-f505-451b-ac47-1f80b1c7af8b)

c)Scenario Bazlı Paralel Test Koşumu

Kullanıcı test süreçlerini hızlandırmak için senaryoları aynı anda birden fazla tarayıcıda test etmek isteyebilir. Öncelikle src/test/resources/application.properties ve src/test/resources/junit-platform.properties içerisinde gerekli konfigürasyonları yapmamız gerek.


src/test/resources/junit-platform.properties
```
#feature-scenario bazlı paralellik true/false
cucumber.execution.parallel.enabled = true
```
Senaryolarımızı cucumber ile hazırladığumız için senaryo bazında paralel koşum durumu bu tag'e bağlı.


src/test/resources/application.properties
```
#Feature-scenario bazl? paralel test ko?umunda ayn? browser tipinden aç?lacak maksimum pencere say?s? Örn: 3 chrome 3 edge 3 firefox aç?l?r
# junit-platform.properties dosyas?ndaki cucumber.execution.parallel.enabled = true ?eklinde olmal?
max.parallel.browsers=3
```
Kaynak kullanımını optimize etmek için aynı anda aynı browser türünden maksimum kaç tane pencere açılmasını istediğimizi ise burdan yönetiyoruz.


Bu iki konfigürasyonu ayarladıktan sonra ister tek browserde ister bütün browserlarda hem senaryo bazlı hem de browser bazlı paralel koşum yapabilriiz.


### 2. Selenium Grid ile Çalıştırma

Önce Selenium Grid'i başlatın:

```bash
# Linux/macOS için
./start-grid.sh

# Windows için
start-grid.bat
```

Testleri Grid üzerinde çalıştırmak için:

```bash
mvn clean test -Dcucumber.filter.tags="@TEST" -Duse_grid=true
```

### 3. Jenkins ile Entegrasyon

Jenkins'i başlatmak için:

```bash
# Linux/macOS için
./start-jenkins.sh

# Windows için
start-jenkins.bat
```

Jenkins arayüzünden pipeline'ı çalıştırarak parametrelerini yapılandırabilirsiniz.

### 4. Paralel Test Çalıştırma

Testleri paralel olarak çalıştırmak için:

```bash
# Linux/macOS için
./run-parallel-tests.sh

# Windows için
run-parallel-tests.bat
```

## Raporlama

Testlerin sonuçları Allure Reports ile görselleştirilmektedir. Test çalıştırması tamamlandıktan sonra, raporları açmak için:

```bash
./Open-reports.sh
```

## Proje Özelleştirme

1. **Yeni Test Senaryoları Ekleme**  
   `src/test/resources` altında yeni feature dosyaları oluşturarak testler ekleyebilirsiniz.

2. **Step Tanımları Ekleme**  
   `stepDefinitions` paketinde yeni adımları tanımlayabilirsiniz.

3. **Konfigürasyon Değiştirme**  
   `application.properties` dosyasında test ortamı yapılandırmasını değiştirebilirsiniz.

## İpuçları

- Tarayıcı tercihlerini komut satırı parametresi olarak geçebilirsiniz: `-Dbrowser=firefox`
- Jenkins üzerinden belirli tag'leri çalıştırabilirsiniz: `-Dcucumber.filter.tags="@smoke or @regression"`
- Paralellik seviyesini junit-platform.properties dosyasında ayarlayabilirsiniz 
