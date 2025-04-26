pipeline {
    agent any

    tools {
        maven 'Maven'
        jdk 'JDK'
    }

    parameters {
        choice(name: 'BROWSER_PROFILE', choices: ['parallel-browsers', 'chrome', 'firefox', 'edge', 'all-browsers'], description: 'Hangi tarayıcı profili ile testler koşulsun?')
        booleanParam(name: 'USE_GRID', defaultValue: true, description: 'Selenium Grid kullanılsın mı?')
    }

    stages {
        stage('Checkout') {
            steps {
                checkout scm
            }
        }

         stage('Selenium Grid Kontrol') {
            steps {
                script {
                    try {
                        def status = sh(script: 'curl -s http://localhost:4444/wd/hub/status | grep "ready":true', returnStatus: true)
                   if (status != 0) {
                            // Grid başlatma seçeneği
                       echo "Selenium Grid hazır değil, başlatılmaya çalışılıyor..."
                       sh 'sh ./start-grid.sh || echo "Grid başlatılamadı"'
                       // Başlatma sonrası tekrar kontrol
                       status = sh(script: 'curl -s http://localhost:4444/wd/hub/status | grep "ready":true', returnStatus: true)
                       if (status != 0) {
                                error "Selenium Grid başlatılamadı!"
                       }
                   }
               } catch (Exception e) {
                        echo "Grid kontrolünde hata: ${e.message}"
                   // Test adımına devam etmek için hata fırlatmayı kaldırabilirsiniz
                   // veya burada grid'i başlatabilirsiniz
                    }
                }
            }
        }

        stage('Testleri Çalıştır') {
            steps {
                script {
                    def useGrid = params.USE_GRID ? "-Duse_grid=true" : "-Duse_grid=false"
                    
                    withMaven(maven: 'Maven') {
                        sh "mvn clean test ${useGrid} -P${params.BROWSER_PROFILE}"
                    }
                }
            }
        }

        tage('Allure Rapor Oluştur') {
            steps {
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

    post {
        always {
            allure includeProperties: false, 
                  jdk: '', 
                  results: [[path: 'target/allure-results']]
        }
        
        success {
            echo 'Tüm testler başarıyla tamamlandı!'
        }
        
        failure {
            echo 'Test koşumu sırasında hatalar oluştu!'
        }
        
        cleanup {
            cleanWs()
        }
    }
}
