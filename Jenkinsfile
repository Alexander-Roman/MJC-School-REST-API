pipeline {
    agent any

    stages {
        stage('Cleanup Workspace') {
            steps {
                cleanWs()
            }
        }
        stage('Checkout') {
            steps {
                checkout([
                    $class: 'GitSCM',
                    branches: [[name: '**']],
                    userRemoteConfigs: [[url: 'https://github.com/Alexander-Roman/MJC-School-REST-API.git']]
                    ])
            }
        }
        stage('Build') {
            steps {
                bat "gradlew build"
            }
        }
        stage('Integration Tests') {
            steps {
                bat "gradlew integration"
            }
        }
        stage('SonarQube Tests') {
            steps {
                bat "gradlew sonarqube"
            }
        }
        stage('Deploy') {
            when {
                branch 'test-develop'
            }
            steps {
                bat "gradlew bootWar"
                deploy adapters: [tomcat9(url: 'http://localhost:8080/', credentialsId: 'dffaf2e5-06dc-4c21-90f7-e1dbae178b10')],
                                war: 'web/build/libs/web.war'
            }
        }
    }
}
