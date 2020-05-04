pipeline {
    agent any
    environment {
        APP_NAME = "${env.JOB_NAME}"
        APP_PATH = "/opt/servers/jenkins/workspace/${APP_NAME}"
        DEPLOY_VERSION = sh (script: "git log -n 1 --pretty=format:'%H'", returnStdout: true)
    }
    stages {
        stage ('build app') {
            steps {
                sh 'docker-compose -f docker-compose.build.yml up --build --exit-code-from ${APP_NAME}-build'
            }
        }
        stage ('build container') {
            steps {
                sh 'docker-compose build'
            }
        }
        stage ('push container') {
            steps {
                sh 'docker-compose push'
            }
        }
        stage ('deploy') {
            steps {
                sh 'cat deployment.yaml | sed -e "s/latest/${DEPLOY_VERSION}/g" | kubectl apply -f -'
                sh 'kubectl patch svc ${APP_NAME} -p \'{"spec": {"type": "LoadBalancer", "externalIPs":["\'${KUBERNETES_SERVER}\'"]}}\''
                timeout(1) {
                    waitUntil {
                       script {
                         def podsDown = sh script: 'kubectl get pods -l app=${APP_NAME} | grep "0/1" | wc -l | head -c 1', returnStdout: true
                         return (podsDown == '0');
                       }
                    }
                }
            }
        }
    }
    post {
        always {
            script {
                sh "curl -sS -X POST -H \"Content-Type: application/json\" -d '{\"text\": \"${env.JOB_NAME} ${currentBuild.result}\"}' http://${KUBERNETES_SERVER}:50000/notification"
            }
            junit 'build/test-results/**/*.xml'
        }
    }
}
