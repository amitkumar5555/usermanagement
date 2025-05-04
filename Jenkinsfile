pipeline {
    agent any

    environment {
        DOCKER_USERNAME = 'amit8614136'
        IMAGE_NAME = 'usermanagement'
        IMAGE_TAG = "${env.BUILD_NUMBER}"  // used build number in tag
        FULL_IMAGE_NAME = "${DOCKER_USERNAME}/${IMAGE_NAME}:${IMAGE_TAG}"
        CONTAINER_NAME = 'usermanagement-container'
      //  PORT = '7002'
        DOCKER_CREDENTIALS_ID = 'docker_hub_credentials'  // Your Docker Hub Jenkins credential ID
        KUBECONFIG_CREDENTIALS_ID = 'kubeconfig_id'         // Jenkins credential ID for kubeconfig file
    }

    tools {
        // Define the Maven version to use in Jenkins
        maven 'Maven_3.9.9'  // Replace with the name of your Maven installation in Jenkins
    }

    stages {

        stage('Checkout Source Code') {
            steps {
                // Adjust 'url' to your Git repository
               git branch: 'main', url: 'https://github.com/amitkumar5555/usermanagement.git'
            }
        }

        // stage('Cleanup Existing Container') {
        //     steps {
        //         script {
        //             sh """
        //             if [ \$(docker ps -aq -f name=${CONTAINER_NAME}) ]; then
        //               docker stop ${CONTAINER_NAME} || true
        //               docker rm ${CONTAINER_NAME} || true
        //             fi
        //             """
        //         }
        //     }
        // }

        // stage('Remove Old Local Image') {
        //     steps {
        //         script {
        //             sh """
        //             if [ \$(docker images -q ${FULL_IMAGE_NAME}) ]; then
        //               docker rmi ${FULL_IMAGE_NAME} --force || true
        //             fi
        //             """
        //         }
        //     }
        // }

         // Stage to package the application using Maven
        stage('Package Application with Maven') {
            steps {
                // Run Maven clean package command
                sh 'mvn clean package -DskipTests'
            }
        }

        // Stage to build Docker image
        stage('Build Docker Image') {
            steps {
                // Build Docker image after Maven has packaged the app
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

        // stage('Remove Local Image') {
        //     steps {
        //         sh "docker rmi ${FULL_IMAGE_NAME} --force || true"
        //     }
        // }

        // stage('Pull from Docker Hub') {
        //     steps {
        //         sh "docker pull ${FULL_IMAGE_NAME}"
        //     }
        // }

        // stage('Run Docker Container') {
        //     steps {
        //         sh """
        //         docker run --platform=linux/amd64 -d \
        //           --name ${CONTAINER_NAME} \
        //           -p ${PORT}:7083 \
        //           ${FULL_IMAGE_NAME}
        //         """
        //     }
        // }

        stage('Deploy to Kubernetes') {
            steps {
                withCredentials([file(credentialsId: "${KUBECONFIG_CREDENTIALS_ID}", variable: 'KUBECONFIG_FILE')]) {
                    sh '''
                    export KUBECONFIG=$KUBECONFIG_FILE

                    # Apply deployment and service YAMLs
                    kubectl apply -f deployment.yaml
                    kubectl apply -f service.yaml
                    '''
                }
            }
        }

    }

    post {
        success {
          //  echo "✅ App deployed successfully: http://localhost:${PORT}"
            echo "✅ Application deployed to Kubernetes successfully."
        }
        failure {
            echo "❌ Deployment failed!"
        }
    }
}

