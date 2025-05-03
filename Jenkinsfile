pipeline {
    agent any

    environment {
        DOCKER_USERNAME = 'amit8614136'
        IMAGE_NAME = 'usermanagement'
        FULL_IMAGE_NAME = "${DOCKER_USERNAME}/${IMAGE_NAME}"
        CONTAINER_NAME = 'usermanagement-container'
        PORT = '7002'
        DOCKER_CREDENTIALS_ID = 'docker_hub_credentials'  // Your Docker Hub Jenkins credential ID
    }

    stages {

        stage('Checkout Source Code') {
            steps {
                // Adjust 'url' to your Git repository
               git branch: 'main', url: 'https://github.com/amitkumar5555/usermanagement.git'
            }
        }

        stage('Cleanup Existing Container') {
            steps {
                script {
                    sh """
                    if [ \$(docker ps -aq -f name=${CONTAINER_NAME}) ]; then
                      docker stop ${CONTAINER_NAME} || true
                      docker rm ${CONTAINER_NAME} || true
                    fi
                    """
                }
            }
        }

        stage('Remove Old Local Image') {
            steps {
                script {
                    sh """
                    if [ \$(docker images -q ${FULL_IMAGE_NAME}) ]; then
                      docker rmi ${FULL_IMAGE_NAME} --force || true
                    fi
                    """
                }
            }
        }

        stage('Build Docker Image') {
            steps {
                sh "docker build --platform=linux/amd64 -t ${FULL_IMAGE_NAME} ."
            }
        }

        stage('Push to Docker Hub') {
            steps {
                withCredentials([usernamePassword(credentialsId: "${DOCKER_CREDENTIALS_ID}", usernameVariable: 'DOCKER_USER', passwordVariable: 'DOCKER_PASS')]) {
                    sh """
                    echo "$DOCKER_PASS" | docker login -u "$DOCKER_USER" --password-stdin
                    docker push ${FULL_IMAGE_NAME}
                    docker logout
                    """
                }
            }
        }

        stage('Remove Local Image') {
            steps {
                sh "docker rmi ${FULL_IMAGE_NAME} --force || true"
            }
        }

        stage('Pull from Docker Hub') {
            steps {
                sh "docker pull ${FULL_IMAGE_NAME}"
            }
        }

        stage('Run Docker Container') {
            steps {
                sh """
                docker run --platform=linux/amd64 -d \
                  --name ${CONTAINER_NAME} \
                  -p ${PORT}:7083 \
                  ${FULL_IMAGE_NAME}
                """
            }
        }
    }

    post {
        success {
            echo "✅ App deployed successfully: http://localhost:${PORT}"
        }
        failure {
            echo "❌ Deployment failed!"
        }
    }
}

