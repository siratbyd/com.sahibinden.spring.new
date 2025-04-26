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
            when {
                expression { params.USE_GRID == true }
            }
            steps {
                script {
                    try {
                        sh 'curl -s http://localhost:4444/wd/hub/status | grep "\"ready\":true"'
                        echo "Selenium Grid hazır durumda."
                    } catch (Exception e) {
                        error "Selenium Grid hazır değil! Lütfen grid'in çalıştığından emin olun."
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

        stage('Allure Rapor Oluştur') {
            steps {
                withMaven(maven: 'Maven') {
                    sh 'mvn allure:report'
                }
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