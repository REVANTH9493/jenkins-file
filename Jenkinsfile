pipeline {
    agent any

    options {
        timestamps()
        skipDefaultCheckout(true)
    }

    environment {
        ARTIFACT_DIR = "pipeline-output"
    }

    stages {

        stage('Checkout') {
            steps {
                checkout scm
                echo "Repository checked out successfully."
            }
        }

        stage('Build') {
            steps {
                echo "===== BUILD STAGE ====="
                sh 'chmod +x ./gradlew || true'
                sh './gradlew clean build -x test'
            }
        }

        stage('Unit Test') {
            steps {
                echo "===== UNIT TEST STAGE ====="
                sh './gradlew test'
            }
        }

        stage('Deploy') {
            steps {
                echo "===== DEPLOY STAGE ====="
                sh """
                    mkdir -p ${ARTIFACT_DIR}
                    echo "Deployment simulated successfully" > ${ARTIFACT_DIR}/deploy.txt
                """
            }
        }
    }

    post {
        always {
            echo "Archiving pipeline results..."
            sh "mkdir -p ${ARTIFACT_DIR} || true"
            sh "git rev-parse HEAD > ${ARTIFACT_DIR}/commit.txt || true"

            archiveArtifacts artifacts: "${ARTIFACT_DIR}/**", fingerprint: true

            echo "Publishing unit test results..."
            junit allowEmptyResults: true, testResults: '**/build/test-results/test/*.xml'
        }

        success {
            echo "PIPELINE SUCCESS"
            sh "echo SUCCESS > ${ARTIFACT_DIR}/status.txt"
        }

        failure {
            echo "PIPELINE FAILURE"
            sh "echo FAILURE > ${ARTIFACT_DIR}/status.txt"
        }
    }
}
