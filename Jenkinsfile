pipeline {
    agent any

    options {
        timestamps()
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
                script {
                    if (fileExists('gradlew')) {
                        echo "Gradle wrapper detected. Running Gradle build..."
                        sh 'chmod +x ./gradlew'
                        sh './gradlew clean build'
                    } else {
                        echo "No gradle wrapper found. Running sample build step..."
                        sh 'echo Build completed > build_result.txt'
                    }
                }
            }
        }

        stage('Test') {
            steps {
                echo "===== TEST STAGE ====="
                sh """
                    mkdir -p ${ARTIFACT_DIR}

                    echo "===== Git Info ====="
                    git rev-parse HEAD | tee ${ARTIFACT_DIR}/commit.txt

                    if [ -d build/reports/tests/test ]; then
                        cp -r build/reports/tests/test ${ARTIFACT_DIR}/test-report
                    else
                        echo "No test report found" > ${ARTIFACT_DIR}/test-report-missing.txt
                    fi
                """
            }
        }

        stage('Deploy') {
            steps {
                echo "===== DEPLOY STAGE ====="
                echo "This stage simulates deployment."
                sh """
                    echo "Deployment simulated successfully" > ${ARTIFACT_DIR}/deploy.txt
                """
            }
        }
    }

    post {
        success {
            echo "PIPELINE SUCCESS: Build, Test, Deploy completed."
            sh "echo SUCCESS > ${ARTIFACT_DIR}/status.txt"
        }

        failure {
            echo "PIPELINE FAILURE: One or more stages failed."
            sh "mkdir -p ${ARTIFACT_DIR} && echo FAILURE > ${ARTIFACT_DIR}/status.txt"
        }

        always {
            echo "Archiving pipeline results..."
            archiveArtifacts artifacts: "${ARTIFACT_DIR}/**", fingerprint: true

            echo "Publishing test results if available..."
            junit allowEmptyResults: true, testResults: '**/build/test-results/test/*.xml'
        }
    }
}
