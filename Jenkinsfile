pipeline {
    agent any

    tools {
        maven 'Maven'
        jdk 'JDK'
    }

    parameters {
        choice(name: 'BROWSER_PROFILE', choices: ['parallel-browsers', 'chrome', 'firefox', 'edge', 'all-browsers'], description: 'Hangi tarayıcı profili ile testler koşulsun?')
        booleanParam(name: 'USE_GRID', defaultValue: true, description: 'Selenium Grid kullanılsın mı?')
        string(name: 'CUCUMBER_TAGS', defaultValue: '@TEST', description: 'Çalıştırılacak Cucumber tag\'leri (örn: @TEST veya @smoke or @regression)')
    }

    environment {
        SELENIUM_GRID_URL = 'http://selenium-hub:4444/wd/hub'
    }

    stages {
        stage('Checkout') {
            steps {
                checkout scm
                echo "Branch: ${env.BRANCH_NAME ?: 'Detached'}"
                echo "Tarayıcı Profili: ${params.BROWSER_PROFILE}"
                echo "Cucumber Tags: ${params.CUCUMBER_TAGS}"
            }
        }

        stage('Build ve Dependencyleri İndir') {
            steps {
                sh 'mvn dependency:go-offline -B'
            }
        }

        stage('Selenium Grid Kontrol') {
            when {
                expression { params.USE_GRID == true }
            }
            steps {
                script {
                    try {
                        // Docker Compose ile çalışan sistemlerde selenium-hub'ı kontrol et
                        sh "docker ps | grep selenium || true"

                        // Önce selenium-hub hostname ile, sonra localhost ile dene
                        def hubStatus = sh(script: 'curl -s http://selenium-hub:4444/wd/hub/status | grep "ready":true || curl -s http://localhost:4444/wd/hub/status | grep "ready":true', returnStatus: true)

                        if (hubStatus != 0) {
                            echo "Selenium Grid durumu kontrol edilemiyor veya hazır değil."

                            // Grid'i başlatmayı dene (opsiyonel)
                            try {
                                sh """
                                    if [ -f "start-grid.sh" ]; then
                                        chmod +x start-grid.sh
                                        ./start-grid.sh
                                    fi
                                """
                                echo "Grid başlatma komutu çalıştırıldı"
                            } catch (Exception e) {
                                echo "Grid başlatma denemesi başarısız: ${e.message}"
                            }

                            // Docker durumunu göster
                            sh "docker ps | grep selenium || true"

                            // Testlere devam et, grid zaten çalışıyor olabilir
                            echo "Selenium Grid bağlantısı sorunlu ancak testler çalıştırılmaya devam edilecek."
                            echo "Grid bağlantı adresi: ${env.SELENIUM_GRID_URL}"
                        } else {
                            echo "Selenium Grid hazır!"
                        }
                    } catch (Exception e) {
                        echo "Selenium Grid kontrolünde hata: ${e.message}"
                        echo "Docker konteynerları kontrol ediliyor..."
                        sh "docker ps | grep selenium || true"

                        // Hata olabilir ama pipeline'ı durdurmayalım
                        echo "Selenium Grid erişimi sorunu olabilir, ancak testler çalıştırılmaya devam edilecek."
                    }
                }
            }
        }

        stage('Testleri Çalıştır') {
            steps {
                script {
                    try {
                        // Selenium Grid URL'ini belirle
                        def gridUrl = env.SELENIUM_GRID_URL

                        // Düzeltilmiş Maven komutu
                        sh """
                            mvn clean test \
                            -P${params.BROWSER_PROFILE} \
                            -Duse_grid=${params.USE_GRID} \
                            -Dselenium.grid.url=${gridUrl} \
                            -Dcucumber.filter.tags="${params.CUCUMBER_TAGS}" \
                            -Dallure.results.directory=target/allure-results \
                            -Dsurefire.useFile=true
                        """
                    } catch (Exception e) {
                        echo "Test çalıştırma sırasında hata oluştu: ${e.message}"
                        currentBuild.result = 'UNSTABLE'
                    }
                }
            }
            post {
                always {
                    junit allowEmptyResults: true, testResults: '**/target/surefire-reports/**/*.xml'

                    // Allure dizini yoksa oluştur
                    sh 'mkdir -p target/allure-results || true'
                }
                success {
                    echo "Testler başarıyla tamamlandı!"
                }
                unstable {
                    echo "Testler tamamlandı fakat hatalar mevcut!"
                }
            }
        }

        stage('Allure Rapor Oluştur') {
            steps {
                script {
                    // Allure sonuçlarını kontrol et
                    sh 'ls -la target/allure-results || echo "Allure sonuçları bulunamadı"'

                    // Boş bir Allure sonuç dosyası oluştur (dizin boşsa)
                    sh '''
                        if [ -z "$(ls -A target/allure-results 2>/dev/null)" ]; then
                            echo '{"name":"Test Çalıştırma","status":"failed","statusDetails":{"message":"Test sonucu bulunamadı"}}' > target/allure-results/dummy-result.json
                        fi
                    '''

                    // Allure raporu oluştur
                    allure([
                        includeProperties: false,
                        jdk: '',
                        properties: [],
                        reportBuildPolicy: 'ALWAYS',
                        results: [[path: 'target/allure-results']]
                    ])
                }
            }
        }
    }

    post {
        always {
            script {
                try {
                    // Allure raporu oluşturmayı tekrar dene (eğer önceki adımda başarısız olduysa)
                    if (currentBuild.resultIsBetterOrEqualTo('UNSTABLE')) {
                        allure([
                            includeProperties: false,
                            jdk: '',
                            properties: [],
                            reportBuildPolicy: 'ALWAYS',
                            results: [[path: 'target/allure-results']]
                        ])
                    }
                } catch (Exception e) {
                    echo "Allure raporu oluşturulamadı: ${e.message}"
                }

                // Temizlik işlemleri
                cleanWs()
            }
        }
        success {
            echo "Pipeline başarıyla tamamlandı!"
        }
        unstable {
            echo "Pipeline tamamlandı fakat sorunlar var!"
        }
        failure {
            echo "Pipeline başarısız oldu!"
        }
    }
}