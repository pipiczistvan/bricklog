pipeline {
    agent any

    options {
        skipDefaultCheckout()
        buildDiscarder(logRotator(numToKeepStr: '3'))
    }

    stages {
        stage('Clean Workspace') {
            steps {
                deleteDir()
            }
        }
        stage('Checkout') {
            steps { checkout scm }
        }

        stage('Decrypt necessary files') {
            steps {
                script {
                    withCredentials([string(credentialsId: 'GPG_PASSPHRASE', variable: 'GPG_PASSPHRASE')]) {
                        sh 'gpg -d --passphrase "$GPG_PASSPHRASE" release/google-services.json.gpg > composeApp/google-services.json'
                        sh 'gpg -d --passphrase "$GPG_PASSPHRASE" release/local.properties.gpg > local.properties'
                        sh 'gpg -d --passphrase "$GPG_PASSPHRASE" release/app-release.jks.gpg > release/app-release.jks'
                        sh 'gpg -d --passphrase "$GPG_PASSPHRASE" release/google-services-credentials.json.gpg > release/google-services-credentials.json'
                    }
                }
            }
        }

        stage('Build AAB') {
            steps {
                sh './gradlew clean bundleReleaseApk'
            }
        }

        stage('Distribute to Firebase') {
            when {
                branch 'develop'
            }
            steps {
                sh './gradlew appDistributionUploadRelease'
            }
        }

        stage('Archive') {
            steps {
                archiveArtifacts fingerprint: true, onlyIfSuccessful: true, artifacts: """
                    composeApp/build/outputs/bundle/release/*.aab,
                    composeApp/build/outputs/apk/release/*.apk,
                    composeApp/build/outputs/mapping/release/mapping.txt
                """
            }
        }
    }
}
