pipeline {
    agent any

    tools {
        maven 'maven'
    }

    environment {
        DOCKER_IMAGE = "anshadin4k/mona-matti"
        AWS_REGION   = "eu-north-1"
        S3_BUCKET    = "mona-matti-kustomize-artifacts"
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

        stage('Update Kustomize Image Tag') {
            steps {
                sh """
                    sed -i 's|newTag:.*|newTag: "${BUILD_NUMBER}"|g' \
                    kustomize/overlays/dev/kustomization.yaml
                """

                sh """
                    echo "Updated kustomization.yaml"
                    cat kustomize/overlays/dev/kustomization.yaml
                """
            }
        }

        stage('Archive Kustomize') {
            steps {
                sh """
                    tar -czf kustomize-${BUILD_NUMBER}.tar.gz kustomize
                """
            }
        }

        stage('Upload Kustomize to S3') {
            steps {
                withCredentials([
                    [$class: 'AmazonWebServicesCredentialsBinding',
                    credentialsId: 'aws-creds']
                ]) {
                    sh """
                        aws s3 cp \
                        kustomize-${BUILD_NUMBER}.tar.gz \
                        s3://${S3_BUCKET}/
                    """
                }
            }
        }
    }

    post {
        success {
            echo "====================================="
            echo "Build Successful"
            echo "Docker Image : ${DOCKER_IMAGE}:${BUILD_NUMBER}"
            echo "Kustomize Archive : kustomize-${BUILD_NUMBER}.tar.gz"
            echo "Uploaded to : s3://${S3_BUCKET}/"
            echo "====================================="
        }

        failure {
            echo "Build Failed"
        }

        always {
            cleanWs()
        }
    }
}