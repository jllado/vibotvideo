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
    }
}
