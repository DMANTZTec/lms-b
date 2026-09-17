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
            description: 'Application port'
        )
    }

    environment {
        IMAGE_NAME = "lms-springboot-app"
        CONTAINER_NAME = "lms-app-${params.ENVIRONMENT}"
        CONTAINER_PORT = "9090"
    }

    tools {
        maven "M3_HOME"
    }

    stages {

        stage('Setup') {
            steps {
                echo '========================================'
                echo 'LMS Backend Jenkins Pipeline'
                echo '========================================'
                echo "Environment : ${params.ENVIRONMENT}"
                echo "App Port    : ${params.APP_PORT}"
                echo 'Branch      : devops_ns'
                echo "Image       : ${IMAGE_NAME}"
                echo "Container   : ${CONTAINER_NAME}"
                echo "Container Port : ${CONTAINER_PORT}"
                echo '========================================'

                sh 'java -version'
                sh 'mvn -version'
                sh 'docker --version'
            }
        }

        stage('Checkout') {
            steps {
                echo 'Checking out LMS backend from GitHub...'

                git(
                    branch: 'devops_ns',
                    credentialsId: 'github-lms',
                    url: 'https://github.com/DMANTZTec/lms-b.git'
                )
            }
        }

        stage('Clean & Build') {
            steps {
                echo 'Cleaning and building LMS backend...'

                sh '''
                    mvn clean package -DskipTests
                '''
            }
        }

        stage('Docker Build') {
            steps {
                echo 'Building LMS Docker image...'

                sh """
                    docker build \
                        -t ${IMAGE_NAME}:latest \
                        .
                """
            }
        }

        stage('Deploy Container') {
            steps {
                echo 'Deploying LMS application...'

                sh """
                    if [ \$(docker ps -aq -f name=^/${CONTAINER_NAME}\$) ]; then
                        echo "Existing container found: ${CONTAINER_NAME}"

                        docker stop ${CONTAINER_NAME} || true
                        docker rm ${CONTAINER_NAME} || true
                    fi

                    docker run -d \
                        --name ${CONTAINER_NAME} \
                        -p ${params.APP_PORT}:${CONTAINER_PORT} \
                        -e SPRING_PROFILES_ACTIVE=${params.ENVIRONMENT} \
                        -v /var/log/lms:/logs \
                        --restart unless-stopped \
                        ${IMAGE_NAME}:latest

                    echo "========================================"
                    echo "LMS application started"
                    echo "Profile          : ${params.ENVIRONMENT}"
                    echo "Host Port        : ${params.APP_PORT}"
                    echo "Container Port   : ${CONTAINER_PORT}"
                    echo "Container        : ${CONTAINER_NAME}"
                    echo "========================================"

                    docker ps -f name=${CONTAINER_NAME}
                """
            }
        }

        stage('Verify Container') {
            steps {
                sh """
                    echo "========================================"
                    echo "LMS CONTAINER STATUS"
                    echo "========================================"

                    docker ps -a -f name=${CONTAINER_NAME}

                    echo "========================================"
                    echo "PORT MAPPING"
                    echo "========================================"

                    docker port ${CONTAINER_NAME}

                    echo "========================================"
                    echo "WAITING FOR SPRING BOOT"
                    echo "========================================"

                    for i in \$(seq 1 10); do
                        if docker logs ${CONTAINER_NAME} 2>&1 | grep -q "Started"; then
                            echo "Spring Boot LMS application started successfully."
                            break
                        fi

                        echo "Waiting for Spring Boot... attempt \$i/10"
                        sleep 5
                    done

                    echo "========================================"
                    echo "APPLICATION LOGS"
                    echo "========================================"

                    docker logs ${CONTAINER_NAME} --tail 100
                """
            }
        }
    }

    post {

        success {
            echo """
            ========================================
            Jenkins Pipeline SUCCESS

            Application : LMS Backend
            Environment : ${params.ENVIRONMENT}
            Host Port   : ${params.APP_PORT}
            Container   : ${CONTAINER_NAME}
            Image       : ${IMAGE_NAME}:latest

            Swagger:
            http://SERVER_IP:${params.APP_PORT}/swagger-ui/index.html

            ========================================
            """
        }

        failure {
            echo """
            ========================================
            Jenkins Pipeline FAILED

            Application : LMS Backend
            Environment : ${params.ENVIRONMENT}
            Container   : ${CONTAINER_NAME}

            Check Jenkins Console Output.

            Docker logs:
            docker logs ${CONTAINER_NAME}

            ========================================
            """
        }
    }
}