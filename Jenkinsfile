pipeline {
    agent any

    tools {
        maven 'maven'
    }

    environment {
        DOCKER_IMAGE = "anshadin4k/mona-matti"
        CHART_NAME = "mona-matti"
        CHART_PATH = "helm"
        AWS_REGION = "eu-north-1"
        AWS_ECR = "175690104602.dkr.ecr.eu-north-1.amazonaws.com"
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

        stage('Docker Build') {
            steps {
                sh '''
                    docker build -t mona-matti:${BUILD_NUMBER} .
                '''
            }
        }

        stage('Docker Tag') {
            steps {
                sh '''
                    docker tag mona-matti:${BUILD_NUMBER} ${DOCKER_IMAGE}:${BUILD_NUMBER}
                '''
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
                    sh '''
                        echo $DOCKER_PASS | docker login -u $DOCKER_USER --password-stdin
                    '''
                }
            }
        }

        stage('Docker Push') {
            steps {
                sh '''
                    docker push ${DOCKER_IMAGE}:${BUILD_NUMBER}
                '''
            }
        }

        stage('Docker Logout') {
            steps {
                sh 'docker logout'
            }
        }

        stage('Update Helm Values') {
            steps {
                sh """
                    sed -i 's/tag:.*/tag: "${BUILD_NUMBER}"/' ${CHART_PATH}/values.yaml

                    echo "=============================="
                    echo "Updated values.yaml"
                    echo "=============================="
                    grep "tag:" ${CHART_PATH}/values.yaml
                """
            }
        }

        stage('Helm Lint') {
            steps {
                sh '''
                    helm lint ${CHART_PATH}
                '''
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
                withCredentials([[
                    $class: 'AmazonWebServicesCredentialsBinding',
                    credentialsId: 'aws-creds'
                ]]) {
                    sh '''
                        aws ecr get-login-password --region ${AWS_REGION} | \
                        helm registry login \
                        --username AWS \
                        --password-stdin ${AWS_ECR}
                    '''
                }
            }
        }

        stage('Push Helm Chart to ECR') {
            steps {
                sh '''
                    helm push ${CHART_NAME}-1.0.0.tgz \
                    oci://${AWS_ECR}/
                '''
            }
        }
    }

    post {
        success {
            echo '========================================='
            echo 'Build Pipeline Completed Successfully'
            echo "Docker Image : ${DOCKER_IMAGE}:${BUILD_NUMBER}"
            echo "Helm Chart   : ${CHART_NAME}-1.0.0.tgz"
            echo '========================================='
        }

        failure {
            echo '========================================='
            echo 'Build Pipeline Failed'
            echo '========================================='
        }

        always {
            cleanWs()
        }
    }
}
