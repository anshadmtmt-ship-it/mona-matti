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

        stage('SonarCloud Analysis') {
            steps {
                withCredentials([string(credentialsId: 'sonarcloud-token', variable: 'SONAR_TOKEN')]) {
                    sh '''
                        mvn clean verify sonar:sonar \
                        -Dsonar.token=$SONAR_TOKEN \
                        -Dsonar.host.url=https://sonarcloud.io \
                        -Dsonar.organization=anshadmtmt-ship-it \
                        -Dsonar.projectKey=anshadmtmt-ship-it_mona-matti
                    '''
                }
            }
        }

        stage('Package') {
            steps {
                sh 'mvn package -DskipTests'
            }
        }

        stage('Docker Login') {
            steps {
                withCredentials([
                    usernamePassword(
                        credentialsId: 'dockerhub',
                        usernameVariable: 'DOCKER_USER',
                        passwordVariable: 'DOCKER_PASS'
                    )
                ]) {
                    sh 'echo $DOCKER_PASS | docker login -u $DOCKER_USER --password-stdin'
                }
            }
        }

        stage('Docker Build') {
            steps {
                sh 'docker build -t mona-matti:${BUILD_NUMBER} .'
            }
        }

        stage('Docker Tag') {
            steps {
                sh 'docker tag mona-matti:${BUILD_NUMBER} ${DOCKER_IMAGE}:${BUILD_NUMBER}'
            }
        }

        stage('Docker Push') {
            steps {
                sh 'docker push ${DOCKER_IMAGE}:${BUILD_NUMBER}'
            }
        }

        stage('Docker Logout') {
            steps {
                sh 'docker logout'
            }
        }
    }

    post {
        success {
            echo "Build Successful - Image: ${DOCKER_IMAGE}:${BUILD_NUMBER}"
        }

        failure {
            echo "Build Failed"
        }

        always {
            cleanWs()
        }
    }
}