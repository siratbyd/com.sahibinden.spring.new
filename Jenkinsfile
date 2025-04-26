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
        SELENIUM_GRID_URL = 'http://localhost:4444/wd/hub'
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
                        def gridStatus = sh(script: 'curl -s http://localhost:4444/wd/hub/status | grep "ready":true', returnStatus: true)
                        if (gridStatus != 0) {
                            echo "Selenium Grid hazır değil, başlatılmaya çalışılıyor..."
                            // Grid başlatma
                            sh """
                                if [ -f "start-grid.sh" ]; then
                                    chmod +x start-grid.sh
                                    ./start-grid.sh
                                elif [ -f "start-grid.bat" ]; then
                                    start-grid.bat
                                else
                                    echo "Grid başlatma scriptleri bulunamadı!"
                                    exit 1
                                fi
                            """

                            // Grid hazır olana kadar bekleme
                            timeout(time: 2, unit: 'MINUTES') {
                                waitUntil {
                                    def status = sh(script: 'curl -s http://localhost:4444/wd/hub/status | grep "ready":true', returnStatus: true)
                                    return status == 0
                                }
                            }
                        }

                        echo "Selenium Grid hazır!"
                    } catch (Exception e) {
                        echo "Selenium Grid kontrolünde hata: ${e.message}"
                        error "Selenium Grid hazır değil! Lütfen grid'in çalıştığından emin olun."
                    }
                }
            }
        }

        stage('Testleri Çalıştır') {
            steps {
                script {
                    try {
                        // Surefire plugin sorunu için düzeltilmiş komut
                        sh """
                            mvn clean test \
                            -P${params.BROWSER_PROFILE} \
                            -Duse_grid=${params.USE_GRID} \
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
                    // Test sonuçları
                    junit allowEmptyResults: true, testResults: '**/target/surefire-reports/**/*.xml'

                    // Allure dizini yoksa oluştur
                    sh 'mkdir -p target/allure-results || true'
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