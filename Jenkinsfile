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
                echo '========================================'

                sh 'java -version'
                sh 'mvn -version'
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

                sh """
                    mvn clean package -DskipTests
                """
            }
        }
    }

    post {
        success {
            echo '========================================'
            echo "LMS ${params.ENVIRONMENT} build completed successfully."
            echo "Application Port : ${params.APP_PORT}"
            echo '========================================'
        }

        failure {
            echo '========================================'
            echo "LMS ${params.ENVIRONMENT} build failed."
            echo '========================================'
        }
    }
}