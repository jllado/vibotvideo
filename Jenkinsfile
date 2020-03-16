pipeline {
    agent any
    stages {
        stage ('Build') {
            steps {
                sh 'export APP_PATH=/opt/servers/jenkins/workspace/vibot-video && docker-compose -f docker-compose.build.yml up --build --exit-code-from vibot-video-build'
            }
        }
        stage ('Deploy') {
            steps {
                sh 'docker-compose up -d --build'
            }
        }
    }
    post {
        always {
            junit 'build/test-results/**/*.xml'
        }
        success {
            script {
                sh "curl -X POST -H \"Content-Type: application/json\" -d '{\"text\": \"${env.JOB_NAME} SUCCESS\"}' http://172.17.0.1:50000/notification"
            }
        }
        unstable {
            script {
                sh "curl -X POST -H \"Content-Type: application/json\" -d '{\"text\": \"${env.JOB_NAME} UNSTABLE\"}' http://172.17.0.1:50000/notification"
            }
        }
        failure {
            script {
                sh "curl -X POST -H \"Content-Type: application/json\" -d '{\"text\": \"${env.JOB_NAME} FAILURE\"}' http://172.17.0.1:50000/notification"
            }
        }
    }
}
