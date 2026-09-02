pipeline {
    agent any

    tools {
        maven 'maven'
    }

    environment {
        DOCKER_IMAGE  = "anshadin4k/mona-matti"

        GITOPS_REPO   = "https://github.com/anshadmtmt-ship-it/mona-matti-gitops.git"
        GITOPS_BRANCH = "main"
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
                    sh '''
                        echo "$DOCKER_PASS" | docker login \
                        -u "$DOCKER_USER" \
                        --password-stdin
                    '''
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

        stage('Update GitOps') {
            steps {
                dir('gitops') {

                    deleteDir()

                    withCredentials([
                        usernamePassword(
                            credentialsId: 'github-gitops',
                            usernameVariable: 'GIT_USER',
                            passwordVariable: 'GIT_TOKEN'
                        )
                    ]) {

                        sh '''
                            git clone \
                            https://${GIT_USER}:${GIT_TOKEN}@github.com/anshadmtmt-ship-it/mona-matti-gitops.git \
                            .

                            git checkout main

                            echo "======================================"
                            echo "GitOps BEFORE UPDATE"
                            echo "======================================"

                            cat kustomize/overlays/production/kustomization.yaml

                            echo
                            echo "Updating image tag to ${BUILD_NUMBER}..."

                            sed -i \
                            's|newTag:.*|newTag: "'${BUILD_NUMBER}'"|g' \
                            kustomize/overlays/production/kustomization.yaml

                            echo
                            echo "======================================"
                            echo "GitOps AFTER UPDATE"
                            echo "======================================"

                            cat kustomize/overlays/production/kustomization.yaml
                        '''
                    }
                }
            }
        }

        stage('Commit GitOps') {
            steps {
                dir('gitops') {

                    sh '''
                        git config user.name "Jenkins"
                        git config user.email "jenkins@localhost"

                        git add kustomize/overlays/production/kustomization.yaml

                        git commit \
                        -m "Update Mona-Matti image to ${BUILD_NUMBER}"
                    '''
                }
            }
        }

        stage('Push GitOps') {
            steps {
                dir('gitops') {

                    withCredentials([
                        usernamePassword(
                            credentialsId: 'github-gitops',
                            usernameVariable: 'GIT_USER',
                            passwordVariable: 'GIT_TOKEN'
                        )
                    ]) {

                        sh '''
                            git remote set-url origin \
                            https://${GIT_USER}:${GIT_TOKEN}@github.com/anshadmtmt-ship-it/mona-matti-gitops.git

                            git push origin main
                        '''
                    }
                }
            }
        }
    }

    post {

        success {
            echo "======================================"
            echo "       CI/CD PIPELINE SUCCESS"
            echo "======================================"

            echo "Build Number : ${BUILD_NUMBER}"
            echo "Docker Image : ${DOCKER_IMAGE}:${BUILD_NUMBER}"
            echo "GitOps Repo  : ${GITOPS_REPO}"

            echo "======================================"
        }

        failure {
            echo "======================================"
            echo "           PIPELINE FAILED"
            echo "======================================"
        }

        always {
            cleanWs()
        }
    }
}
