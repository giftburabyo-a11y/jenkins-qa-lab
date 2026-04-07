pipeline {
    agent any

    environment {
        SLACK_CHANNEL = '#all-jolly'
    }

    options {
        buildDiscarder(logRotator(numToKeepStr: '10'))
        timestamps()
        timeout(time: 30, unit: 'MINUTES')
    }

    triggers {
        githubPush()
    }

    stages {

        stage('Checkout') {
            steps {
                echo '📥 Pulling latest code from GitHub...'
                checkout scm
            }
        }

        stage('Build & Install Dependencies') {
            steps {
                echo '📦 Resolving Maven dependencies...'
                bat 'mvn dependency:resolve -B --no-transfer-progress || echo Dependencies already cached'
            }
        }

        stage('Run API Tests') {
            steps {
                echo '🧪 Running REST Assured API tests...'
                bat 'mvn test -B --no-transfer-progress -Dmaven.test.failure.ignore=true'
            }
            post {
                always {
                    junit testResults: 'target/surefire-reports/*.xml',
                          allowEmptyResults: true
                }
            }
        }

        stage('Generate Allure Report') {
            steps {
                echo '📈 Generating Allure report...'
                bat 'mvn allure:report -B --no-transfer-progress || echo Allure report generation skipped'
            }
        }

        stage('Publish Reports') {
            steps {
                echo '📁 Archiving test reports...'
                archiveArtifacts artifacts: 'target/surefire-reports/**',
                                 allowEmptyArchive: true
                allure([
                    includeProperties: false,
                    jdk: '',
                    commandline: 'allure',
                    results: [[path: 'target/allure-results']]
                ])
            }
        }
    }

    post {

        success {
            echo '✅ All tests passed!'
            slackSend(
                channel: env.SLACK_CHANNEL,
                color: 'good',
                message: "✅ *Jenkins QA Pipeline — PASSED*\n*Job:* ${env.JOB_NAME} #${env.BUILD_NUMBER}\n*Duration:* ${currentBuild.durationString}\n*View:* ${env.BUILD_URL}"
            )
            mail(
                to: 'gift.burabyo@amalitechtraining.org',
                subject: "[PASSED] Jenkins QA Tests — Build #${env.BUILD_NUMBER}",
                body: """Jenkins QA Pipeline — BUILD PASSED ✅

Job:      ${env.JOB_NAME}
Build:    #${env.BUILD_NUMBER}
Duration: ${currentBuild.durationString}
View:     ${env.BUILD_URL}

All API tests passed successfully."""
            )
        }

        failure {
            echo '❌ Build failed!'
            slackSend(
                channel: env.SLACK_CHANNEL,
                color: 'danger',
                message: "❌ *Jenkins QA Pipeline — FAILED*\n*Job:* ${env.JOB_NAME} #${env.BUILD_NUMBER}\n*Duration:* ${currentBuild.durationString}\n*View:* ${env.BUILD_URL}"
            )
            mail(
                to: 'gift.burabyo@amalitechtraining.org',
                subject: "[FAILED] Jenkins QA Tests — Build #${env.BUILD_NUMBER}",
                body: """Jenkins QA Pipeline — BUILD FAILED ❌

Job:      ${env.JOB_NAME}
Build:    #${env.BUILD_NUMBER}
Duration: ${currentBuild.durationString}
View:     ${env.BUILD_URL}console

One or more tests failed. Please review the report."""
            )
        }

        unstable {
            echo '⚠️ Build unstable — some tests failed.'
            slackSend(
                channel: env.SLACK_CHANNEL,
                color: 'warning',
                message: "⚠️ *Jenkins QA Pipeline — UNSTABLE*\n*Job:* ${env.JOB_NAME} #${env.BUILD_NUMBER}\n*Duration:* ${currentBuild.durationString}\n*View:* ${env.BUILD_URL}"
            )
        }

        always {
            echo '🧹 Done.'
        }
    }
}