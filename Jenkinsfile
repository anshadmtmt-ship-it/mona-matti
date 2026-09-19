pipeline {
    agent any

    tools {
        maven 'maven'
    }

    environment {
        DOCKER_IMAGE = "anshadin4k/mona-matti"
    }

    stages {

        stage('Build') {
            steps {
                sh 'mvn clean compile'
            }
        }

        stage('Unit Test') {
            steps {
                sh 'mvn test'
            }
        }

        stage('SonarQube Analysis') {
            steps {
                withSonarQubeEnv('sonarqube') {
                    sh '''
                        mvn clean verify sonar:sonar \
                        -Dsonar.coverage.jacoco.xmlReportPaths=target/site/jacoco/jacoco.xml \
                        -Dsonar.coverage.exclusions=**/MonaMattiApplication.java
                    '''
                }
            }
        }

        stage('Quality Gate') {
            steps {
                timeout(time: 2, unit: 'MINUTES') {
                    waitForQualityGate abortPipeline: true
                }
            }
        }

        stage('Package') {
            steps {
                sh 'mvn package -DskipTests'
            }
        }

        stage('Docker Build & Push') {
            steps {
                script {

                    def image = docker.build(
                        "${DOCKER_IMAGE}:${BUILD_NUMBER}"
                    )

                    docker.withRegistry(
                        'https://index.docker.io/v1/',
                        'dockerhub'
                    ) {
                        image.push()
                    }
                }
            }
        }
    }

    post {

        success {
            echo "======================================"
            echo "          CI PIPELINE SUCCESS"
            echo "======================================"

            echo "Build Number : ${BUILD_NUMBER}"
            echo "Docker Image : ${DOCKER_IMAGE}:${BUILD_NUMBER}"

            echo "======================================"
        }

        failure {
            echo "======================================"
            echo "           CI PIPELINE FAILED"
            echo "======================================"
        }

        always {
            cleanWs()
        }
    }
}