pipeline {
    agent any

    parameters {
        choice(
            name: 'ENVIRONMENT',
            choices: ['uat', 'dev', 'prod'],
            description: 'Target environment'
        )

        string(
            name: 'APP_PORT',
            defaultValue: '9600',
            description: 'Host port exposed for the LMS application'
        )
    }

    environment {
        IMAGE_NAME = "lms-springboot-app"
        CONTAINER_PORT = "9090"
        CONTAINER_NAME = "lms-app-${params.ENVIRONMENT}"
    }

    tools {
        maven "M3_HOME"
    }

    options {
        skipDefaultCheckout(true)
    }

    stages {

        stage('Setup') {
            steps {
                echo "========================================"
                echo " LMS Deployment"
                echo " Environment : ${params.ENVIRONMENT}"
                echo " Host Port   : ${params.APP_PORT}"
                echo " Container   : ${CONTAINER_NAME}"
                echo "========================================"
            }
        }

        stage('Checkout') {
            steps {
                echo "Checking out LMS source code..."

                git(
                    branch: 'devops_ns',
                    credentialsId: 'github-lms',
                    url: 'https://github.com/DMANTZTec/lms-b.git'
                )
            }
        }

        stage('Clean & Build') {
            steps {
                echo "Building LMS application..."

                sh '''
                    mvn clean package -DskipTests
                '''
            }
        }

        stage('Docker Build') {
            steps {
                echo "Building Docker image..."

                sh """
                    docker build -t ${IMAGE_NAME}:latest .
                """
            }
        }

        ```groovy
        stage('Deploy Container') {
            steps {
                echo "Deploying LMS container..."

                withCredentials([
                    string(credentialsId: 'twilio-account-sid', variable: 'TWILIO_ACCOUNT_SID'),
                    string(credentialsId: 'twilio-auth-token', variable: 'TWILIO_AUTH_TOKEN'),
                    string(credentialsId: 'twilio-from-number', variable: 'TWILIO_FROM_NUMBER')
                ]) {
                    sh """
                        docker stop ${CONTAINER_NAME} || true
                        docker rm ${CONTAINER_NAME} || true

                        docker run -d \
                            --name ${CONTAINER_NAME} \
                            -p ${params.APP_PORT}:${CONTAINER_PORT} \
                            -e SPRING_PROFILES_ACTIVE=${params.ENVIRONMENT} \
                            -e STRAPI_URL="http://localhost:1337" \
                            -e TWILIO_ACCOUNT_SID="\${TWILIO_ACCOUNT_SID}" \
                            -e TWILIO_AUTH_TOKEN="\${TWILIO_AUTH_TOKEN}" \
                            -e TWILIO_FROM_NUMBER="\${TWILIO_FROM_NUMBER}" \
                            -v /var/log/lms:/logs \
                            --restart unless-stopped \
                            ${IMAGE_NAME}:latest
                    """
                }
            }
        }
        ```


        stage('Verify Container') {
            steps {
                echo "Waiting for LMS application to start..."

                sh '''
                    sleep 20

                    echo "----------------------------------------"
                    echo "Docker Container Status"
                    echo "----------------------------------------"

                    docker ps -a --filter "name=${CONTAINER_NAME}"

                    echo "----------------------------------------"
                    echo "LMS Application Logs"
                    echo "----------------------------------------"

                    docker logs --tail 100 ${CONTAINER_NAME}

                    echo "----------------------------------------"
                    echo "Checking application startup..."
                    echo "----------------------------------------"

                    if docker logs ${CONTAINER_NAME} 2>&1 | grep -q "Started .*Application"; then
                        echo "LMS application started successfully."
                    else
                        echo "LMS application failed to start."
                        echo "Showing complete logs:"
                        docker logs ${CONTAINER_NAME}
                        exit 1
                    fi
                '''
            }
        }
    }

    post {

        success {
            echo """
            ========================================
            LMS DEPLOYMENT SUCCESSFUL
            ========================================
            Environment : ${params.ENVIRONMENT}
            Container   : ${CONTAINER_NAME}
            Host Port   : ${params.APP_PORT}

            Application :
            http://SERVER_IP:${params.APP_PORT}/lms

            Swagger :
            http://SERVER_IP:${params.APP_PORT}/lms/swagger-ui/index.html
            ========================================
            """
        }

        failure {
            echo """
            ========================================
            LMS DEPLOYMENT FAILED
            ========================================
            Container : ${CONTAINER_NAME}

            Check logs using:
            docker logs ${CONTAINER_NAME}
            ========================================
            """
        }
    }
}

