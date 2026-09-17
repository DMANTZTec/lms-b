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
                echo '========================================'
                echo 'LMS Backend Jenkins Pipeline'
                echo '========================================'
                echo "Environment     : ${params.ENVIRONMENT}"
                echo "Host Port       : ${params.APP_PORT}"
                echo 'Branch          : devops_ns'
                echo "Image           : ${IMAGE_NAME}"
                echo "Container       : lms-app-${params.ENVIRONMENT}"
                echo "Container Port  : ${CONTAINER_PORT}"
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

                withCredentials([
                    usernamePassword(
                        credentialsId: 'github-lms',
                        usernameVariable: 'DB_USERNAME',
                        passwordVariable: 'DB_PASSWORD'
                    )
                ]) {
                    sh """
                        echo "Stopping existing container if present..."

                        if docker ps -aq -f name=^/lms-app-${params.ENVIRONMENT}\$ | grep -q .; then
                            echo "Existing container found: lms-app-${params.ENVIRONMENT}"
                            docker stop lms-app-${params.ENVIRONMENT} || true
                            docker rm lms-app-${params.ENVIRONMENT} || true
                        fi

                        echo "Starting LMS container..."

                        docker run -d \
                            --name lms-app-${params.ENVIRONMENT} \
                            -p ${params.APP_PORT}:${CONTAINER_PORT} \
                            -e SPRING_PROFILES_ACTIVE=${params.ENVIRONMENT} \
                            -e DB_HOST=103.12.1.147 \
                            -e DB_PORT=3306 \
                            -e DB_NAME=lms \
                            -e DB_USERNAME="\$DB_USERNAME" \
                            -e DB_PASSWORD="\$DB_PASSWORD" \
                            -v /var/log/lms:/logs \
                            --restart unless-stopped \
                            ${IMAGE_NAME}:latest

                        echo "========================================"
                        echo "LMS container started"
                        echo "========================================"

                        docker ps -f name=lms-app-${params.ENVIRONMENT}
                    """
                }
            }
        }

        stage('Verify Container') {
            steps {
                sh """
                    echo "========================================"
                    echo "VERIFYING LMS CONTAINER"
                    echo "========================================"

                    for i in \$(seq 1 12); do

                        if ! docker ps --format '{{.Names}}' | grep -q "^lms-app-${params.ENVIRONMENT}\$"; then
                            echo "Container has stopped."
                            break
                        fi

                        if docker logs lms-app-${params.ENVIRONMENT} 2>&1 | grep -q "Started .*Application"; then
                            echo "========================================"
                            echo "Spring Boot started successfully!"
                            echo "========================================"
                            exit 0
                        fi

                        echo "Waiting for Spring Boot... attempt \$i/12"
                        sleep 5
                    done

                    echo "========================================"
                    echo "APPLICATION FAILED TO START"
                    echo "========================================"

                    docker ps -a -f name=lms-app-${params.ENVIRONMENT}

                    echo "========================================"
                    echo "APPLICATION LOGS"
                    echo "========================================"

                    docker logs lms-app-${params.ENVIRONMENT} --tail 100

                    exit 1
                """
            }
        }
    }

    post {

        success {
            echo """
            ========================================
            Jenkins Pipeline SUCCESS
            ========================================

            Application : LMS Backend
            Environment : ${params.ENVIRONMENT}
            Host Port   : ${params.APP_PORT}
            Container   : lms-app-${params.ENVIRONMENT}
            Image       : ${IMAGE_NAME}:latest

            Swagger:
            http://SERVER_IP:${params.APP_PORT}/lms/swagger-ui/index.html

            ========================================
            """
        }

        failure {
            echo """
            ========================================
            Jenkins Pipeline FAILED
            ========================================

            Application : LMS Backend
            Environment : ${params.ENVIRONMENT}
            Container   : lms-app-${params.ENVIRONMENT}

            Check Jenkins Console Output.

            Docker logs:
            docker logs lms-app-${params.ENVIRONMENT}

            ========================================
            """
        }
    }
}