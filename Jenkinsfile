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
                        // Düzeltilmiş Maven komutu
                        sh """
                            mvn clean test \
                            -P${params.BROWSER_PROFILE} \
                            -Duse_grid=${params.USE_GRID} \
                            -Dcucumber.filter.tags="${params.CUCUMBER_TAGS}" \
                            -Dallure.results.directory=target/allure-results \
                            -Dcucumber.plugin=io.qameta.allure.cucumber7jvm.AllureCucumber7Jvm
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
            when {
                expression { fileExists('target/allure-results') }
            }
            steps {
                script {
                    try {
                        allure([
                            includeProperties: false,
                            jdk: '',
                            properties: [],
                            reportBuildPolicy: 'ALWAYS',
                            results: [[path: 'target/allure-results']]
                        ])
                    } catch (Exception e) {
                        echo "Allure raporu oluşturulamadı: ${e.message}"
                        unstable(message: "Allure raporu oluşturulamadı")
                    }
                }
            }
        }
    }

    post {
        always {
            script {
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