pipeline {
    agent any

    environment {
        EC2_HOST = '13.222.181.0'
        EC2_USER = 'ec2-user'
        APP_NAME = 'demoexample'
        APP_PORT = '9090'
        JAR_NAME = 'demoexample-0.0.1-SNAPSHOT.jar'
    }

    stages {
        stage('Checkout') {
            steps {
                checkout scm
            }
        }

        stage('Build Jar') {
            steps {
                bat '.\\mvnw.cmd clean package -DskipTests'
            }
        }

        stage('Copy Jar to EC2') {
            steps {
                sshagent(credentials: ['ec2-ssh-key']) {
                    bat """
                        scp -o StrictHostKeyChecking=no target\\%JAR_NAME% %EC2_USER%@%EC2_HOST%:/home/%EC2_USER%/app.jar
                    """
                }
            }
        }

        stage('Deploy on EC2') {
            steps {
                sshagent(credentials: ['ec2-ssh-key']) {
                    bat """
                        ssh -o StrictHostKeyChecking=no %EC2_USER%@%EC2_HOST% "mkdir -p ~/deploy"
                        ssh -o StrictHostKeyChecking=no %EC2_USER%@%EC2_HOST% "mv ~/app.jar ~/deploy/app.jar"
                        ssh -o StrictHostKeyChecking=no %EC2_USER%@%EC2_HOST% "cd ~/deploy && printf 'FROM eclipse-temurin:21-jre\\nWORKDIR /app\\nCOPY app.jar app.jar\\nEXPOSE 9090\\nENTRYPOINT [\\\"java\\\", \\\"-jar\\\", \\\"app.jar\\\"]\\n' > Dockerfile"
                        ssh -o StrictHostKeyChecking=no %EC2_USER%@%EC2_HOST% "docker rm -f %APP_NAME% || true"
                        ssh -o StrictHostKeyChecking=no %EC2_USER%@%EC2_HOST% "cd ~/deploy && docker build -t %APP_NAME%:latest ."
                        ssh -o StrictHostKeyChecking=no %EC2_USER%@%EC2_HOST% "docker run -d --name %APP_NAME% -p %APP_PORT%:%APP_PORT% %APP_NAME%:latest"
                    """
                }
            }
        }
    }

    post {
        success {
            echo 'Deployment completed successfully.'
        }
        failure {
            echo 'Deployment failed.'
        }
    }
}