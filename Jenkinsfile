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
                    if (fileExists('gradlew') || fileExists('gradlew.bat')) {
                        echo "Gradle wrapper detected. Running Gradle build..."
                        // Windows + Linux compatible
                        bat 'gradlew.bat clean build'  // for Windows Jenkins
                        // sh './gradlew clean build'   // for Linux Jenkins
                    } else {
                        echo "No gradle wrapper found. Running sample build step..."
                        bat 'echo Build completed > build_result.txt'
                    }
                }
            }
        }

        stage('Test') {
            steps {
                echo "===== TEST STAGE ====="

                script {
                    // Create output folder
                    bat "if not exist %ARTIFACT_DIR% mkdir %ARTIFACT_DIR%"

                    // Git commit info
                    bat "git rev-parse HEAD > %ARTIFACT_DIR%\\commit.txt"

                    // If Gradle test report exists, keep it
                    bat """
                    if exist build\\reports\\tests\\test (
                        xcopy /E /I /Y build\\reports\\tests\\test %ARTIFACT_DIR%\\test-report
                    ) else (
                        echo No test report found > %ARTIFACT_DIR%\\test-report-missing.txt
                    )
                    """
                }
            }
        }

        stage('Deploy') {
            steps {
                echo "===== DEPLOY STAGE ====="
                echo "This stage simulates deployment."

                script {
                    bat """
                    echo Deployment simulated successfully > %ARTIFACT_DIR%\\deploy.txt
                    """
                }
            }
        }
    }

    post {

        success {
            echo "✅ PIPELINE SUCCESS: Build, Test, Deploy completed."
            bat "echo SUCCESS > %ARTIFACT_DIR%\\status.txt"
        }

        failure {
            echo "❌ PIPELINE FAILURE: One or more stages failed."
            bat "echo FAILURE > %ARTIFACT_DIR%\\status.txt"
        }

        always {
            echo "📦 Archiving pipeline results..."
            archiveArtifacts artifacts: "${ARTIFACT_DIR}/**", fingerprint: true

            echo "Publishing test results if available..."
            junit allowEmptyResults: true, testResults: '**/build/test-results/test/*.xml'
        }
    }
}
