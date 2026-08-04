pipeline {
    agent any

    tools {
        maven 'maven'
    }

    environment {
        DOCKER_IMAGE = "anshadin4k/mona-matti"
        CHART_NAME   = "mona-matti"
        CHART_PATH   = "helm"
        AWS_REGION   = "eu-north-1"
        AWS_ECR      = "175690104602.dkr.ecr.eu-north-1.amazonaws.com"
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

        stage('Helm Lint') {
            steps {
                sh 'helm lint ${CHART_PATH}'
            }
        }

        stage('Package Helm Chart') {
            steps {
                sh '''
                    rm -f ${CHART_NAME}-*.tgz
                    helm package ${CHART_PATH}
                '''
            }
        }

        stage('AWS ECR Login') {
            steps {
                withCredentials([
                    [$class: 'AmazonWebServicesCredentialsBinding',
                    credentialsId: 'aws-creds']
                ]) {
                    sh '''
                        aws ecr get-login-password --region ${AWS_REGION} | \
                        helm registry login \
                        --username AWS \
                        --password-stdin ${AWS_ECR}
                    '''
                }
            }
        }

        stage('Push Helm Chart') {
            steps {
                sh 'helm push ${CHART_NAME}-1.0.0.tgz oci://${AWS_ECR}'
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
