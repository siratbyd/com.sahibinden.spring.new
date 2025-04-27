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

### a)Normal Test Koşumu

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


### b)Browser Bazlı Paralel Test Koşumu

Kullanıcı aynı adımları takip ederek sağ üstteki dropdown menüsü üzerinden "Run All Browsers" seçeneği ile testi başlatırsa her 3 browserda da test koşumu başarıyla başlatılmış olunur.

![image](https://github.com/user-attachments/assets/86368290-f505-451b-ac47-1f80b1c7af8b)

### c)Scenario Bazlı Paralel Test Koşumu

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

Not: Docker kendi locanizde halihazırda kurulu olmalı.(https://www.docker.com/products/docker-desktop/)

Önce Selenium Grid'i başlatın:

Selanium Grid'i başlatma, container'ların indirilmesi, kurulumu ve ayağa kaldırma vs. gibi ayarları start-grid.sh dosyası otomatize bir şekilde ayarlıyor.


start-grid.sh
```
# Linux/macOS için
# Gerekli pluginler yüklüyse direkt IDE komut satırından çalıştırılabilir.
./start-grid.sh

# Windows için
start-grid.bat
```

![image](https://github.com/user-attachments/assets/bc7fb29f-dc56-4d31-b2c9-8dcae87a723d)


Kurulum tamamlandıktan sonra;

```
http://localhost:4444/ui
```
Linkinden Selenium Grid Hub'ın arayüzüne erişebiliyoruz

![image](https://github.com/user-attachments/assets/e03c82a1-a7b1-4852-ab30-a84deceb3b6d)


### Testleri Grid üzerinde çalıştırma

Testlerimizi grid üzerinde çalıştırmak için öncelikle application.properties üzerindeki "use_grid" konfigürasyonunu ayarlamamız gerek.

src/test/resources/application.properties
```
# Selenium Grid kullanımı (true/false)
use_grid=true
```
Eğer testi başlatmadan önce burayı "true"'ya çekmezsek containerlar ayakta olmasına ve Selenium Grid aktif olmasına rağmen testlerimiz localimizde çalışır.

Burdan sonraki adımlarda gerek tekli koşum gerek browser tabanlı paralel koşum gerekse senaryo bazlı paralel koşumdaki adımlar local test koşma ile birebir aynı.

![image](https://github.com/user-attachments/assets/c6848d3f-ed9d-4401-af4c-3c87917156a7)
Testlerinizi başlattığınızda Selenium HUB üzerinde browserların ayağa kalktığını görebilirsiniz.

### 3. Jenkins ile Entegrasyon

Jenkinsi başlatmak için start-grid.sh'a benzer şekilde start-jenkins-sh'ı çalıştırmamız yeterli olacaktır. Böylelelikle Selenim HUB, browserlar ve Jenkins için gerekti containerlar otomatik olarak pull yapılıp ardından ayağa kalkacaklardı. Jenkins ve Selenium HUB'la ilgili docker configurationları sırasıyla docker-compose.jenkins.yml ve docker-compose.yml dosyaları içerisindedi.r

Jenkins'i başlatmak için:

start-jenkins.sh
```
# Linux/macOS için
./start-jenkins.sh

# Windows için
start-jenkins.bat
```

Jenkins arayüzünden pipeline'ı çalıştırarak parametrelerini yapılandırabilirsiniz.

Not: Dependency uyumsuzlukları ve version problemlerinden dolayı projenin şuanki güncel halinde Jenkins piple kurulumu %100 verimle çalışmayabiliyor.

## Raporlama

Testlerin sonuçları Allure Reports ve Cucumber'ın kendi raporlama sistemi ile görselleştirilmektedir. Test çalıştırması tamamlandıktan sonra, raporları açmak için:

Eğer paralel olmayan bir test koşumu yapıldıysa Allure tercih edilebilir ve raporu almak için;

```
allure serve
```
yazmak yeterlidir.

![image](https://github.com/user-attachments/assets/24a1bbb1-06cc-437e-81b2-19cec5cf0084)


Eğer browser bazlı paralel bir koşum gerçekleştirildiyse Cucumber raporlaması tercih edilmelidir.

```
./Open-reports.sh
```
komutu çalıştırılarak raporlara kolaylıkla erişim sağlanabilir. Bu sh dosyası istenen tek bir tarayıcının veya aynı anda üç tarayıcının da raporlarına seçenek ile erişilebilir.

![image](https://github.com/user-attachments/assets/c750256c-781f-44c0-9a6c-d67d69895c46)

![image](https://github.com/user-attachments/assets/151559cc-9be2-4344-b57c-c653abb5d150)




## Proje Özelleştirme

1. **Yeni Test Senaryoları Ekleme**  
   `src/test/resources` altında yeni feature dosyaları oluşturarak testler ekleyebilirsiniz.

2. **Step Tanımları Ekleme**  
   `stepDefinitions` paketinde yeni adımları tanımlayabilirsiniz.

3. **Konfigürasyon Değiştirme**  
   `application.properties` dosyasında test ortamı yapılandırmasını değiştirebilirsiniz.

4. **Locator Ekleme**  
   `src/test/java/objectRepository` dosyasında locaterlarınızı sınıflandırarak kolaylıkla ekleyebilir ve testlerinizde kullanabilirsiniz.

## Projenin Gelişime Açık yönleri

- Dependency ve version sorunları çözülerek çok daha sağlıklı bir Jenkin kurulumu yapılarak pipelineler hazırlanarak CI/CD süreçleri dahil edilebilir.
- Hazır ve ücretsiz raporlama sistemleri yerine ücretli ama verimli veya özelleştirilmiş raporlar kullanılabilir.
