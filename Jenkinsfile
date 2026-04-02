pipeline {
    agent any

    // ── Environment variables ─────────────────────────────────────────────
    environment {
        JAVA_HOME         = tool name: 'JDK17', type: 'jdk'
        MAVEN_HOME        = tool name: 'Maven3', type: 'maven'
        PATH              = "${JAVA_HOME}/bin:${MAVEN_HOME}/bin:${PATH}"
        SLACK_CHANNEL     = '#jenkins-qa'
        EMAIL_RECIPIENTS  = credentials('EMAIL_RECIPIENTS')
    }

    // ── Build triggers ────────────────────────────────────────────────────
    triggers {
        // Triggered by GitHub webhook on every push
        githubPush()
    }

    // ── Options ───────────────────────────────────────────────────────────
    options {
        buildDiscarder(logRotator(numToKeepStr: '10'))
        timestamps()
        timeout(time: 30, unit: 'MINUTES')
    }

    // ── Stages ────────────────────────────────────────────────────────────
    stages {

        stage('Checkout') {
            steps {
                echo '📥 Pulling latest code from GitHub...'
                checkout scm
            }
        }

        stage('Build & Install Dependencies') {
            steps {
                echo '📦 Installing Maven dependencies...'
                sh 'mvn dependency:go-offline -B --no-transfer-progress'
            }
        }

        stage('Run API Tests') {
            steps {
                echo '🧪 Running REST Assured API tests...'
                sh 'mvn test -B --no-transfer-progress -Dmaven.test.failure.ignore=true'
            }
            post {
                always {
                    echo '📊 Collecting JUnit test results...'
                    junit testResults: 'target/surefire-reports/*.xml',
                          allowEmptyResults: true
                }
            }
        }

        stage('Generate Allure Report') {
            steps {
                echo '📈 Generating Allure report...'
                sh 'mvn allure:report -B --no-transfer-progress'
            }
        }

        stage('Publish Reports') {
            steps {
                echo '📁 Publishing test reports...'

                // Archive Surefire XML reports
                archiveArtifacts artifacts: 'target/surefire-reports/**',
                                 allowEmptyArchive: true

                // Publish Allure HTML report via Allure Jenkins Plugin
                allure([
                    includeProperties: false,
                    jdk: '',
                    results: [[path: 'target/allure-results']]
                ])
            }
        }
    }

    // ── Post-build actions ────────────────────────────────────────────────
    post {

        success {
            echo '✅ All tests passed!'

            // Slack success notification
            slackSend(
                channel: env.SLACK_CHANNEL,
                color: 'good',
                message: """
*✅ Jenkins QA Pipeline — PASSED*
*Job:* ${env.JOB_NAME} #${env.BUILD_NUMBER}
*Branch:* ${env.GIT_BRANCH}
*Duration:* ${currentBuild.durationString}
*View:* ${env.BUILD_URL}
                """.stripIndent()
            )

            // Email success notification
            mail(
                to: env.EMAIL_RECIPIENTS,
                subject: "[PASSED] Jenkins QA Tests — Build #${env.BUILD_NUMBER}",
                body: """
Jenkins QA Pipeline — BUILD PASSED ✅

Job:      ${env.JOB_NAME}
Build:    #${env.BUILD_NUMBER}
Branch:   ${env.GIT_BRANCH}
Duration: ${currentBuild.durationString}

View full report: ${env.BUILD_URL}allure/

All API tests passed successfully.
                """.stripIndent()
            )
        }

        failure {
            echo '❌ Build failed!'

            // Slack failure notification
            slackSend(
                channel: env.SLACK_CHANNEL,
                color: 'danger',
                message: """
*❌ Jenkins QA Pipeline — FAILED*
*Job:* ${env.JOB_NAME} #${env.BUILD_NUMBER}
*Branch:* ${env.GIT_BRANCH}
*Duration:* ${currentBuild.durationString}
*View:* ${env.BUILD_URL}
                """.stripIndent()
            )

            // Email failure notification
            mail(
                to: env.EMAIL_RECIPIENTS,
                subject: "[FAILED] Jenkins QA Tests — Build #${env.BUILD_NUMBER}",
                body: """
Jenkins QA Pipeline — BUILD FAILED ❌

Job:      ${env.JOB_NAME}
Build:    #${env.BUILD_NUMBER}
Branch:   ${env.GIT_BRANCH}
Duration: ${currentBuild.durationString}

View console output: ${env.BUILD_URL}console

One or more tests failed. Please review the report and fix the issues.
                """.stripIndent()
            )
        }

        unstable {
            echo '⚠️ Build unstable — some tests failed.'

            slackSend(
                channel: env.SLACK_CHANNEL,
                color: 'warning',
                message: """
*⚠️ Jenkins QA Pipeline — UNSTABLE*
*Job:* ${env.JOB_NAME} #${env.BUILD_NUMBER}
*Branch:* ${env.GIT_BRANCH}
*Duration:* ${currentBuild.durationString}
*View:* ${env.BUILD_URL}
                """.stripIndent()
            )
        }

        always {
            echo '🧹 Cleaning up workspace...'
            cleanWs()
        }
    }
}