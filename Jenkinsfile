pipeline {
    agent any

    tools {
        maven 'maven'
    }

    stages {

        stage('Build') {
            steps {
                echo '========== BUILD STAGE =========='
                sh 'mvn clean compile'
            }
        }

        stage('Unit Test') {
            steps {
                echo '========== UNIT TEST STAGE =========='
                sh 'mvn test'
            }
        }
    }

    post {
        success {
            echo 'Pipeline completed successfully.'
        }

        failure {
            echo 'Pipeline failed. Please check the console output.'
        }

        always {
            echo 'Pipeline execution finished.'
        }
    }
}
