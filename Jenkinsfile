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
            steps {
                checkout scm
                script {
                    env.GIT_COMMIT_HASH = sh(script: 'git rev-parse --short HEAD', returnStdout: true).trim()
                    echo "Short Git commit hash: ${env.GIT_COMMIT_HASH}"
                }
             }
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

        stage('Generate Release Notes') {
            steps {
                script {
                    def releaseNotes = ''

                    for (changeSet in currentBuild.changeSets) {
                        for (entry in changeSet.items) {
                            def msg = entry.msg
                            releaseNotes += "- ${entry.msg}\n"
                        }
                    }

                    // Fallback if no changes
                    if (releaseNotes.trim().isEmpty()) {
                        releaseNotes = "No changes since last build.\n"
                    }

                    // Write to file
                    writeFile file: 'release_notes.txt', text: releaseNotes
                }
            }
        }

        stage('Build AAB') {
            steps {
                sh "./gradlew clean bundleReleaseApk -PREVISION=${env.GIT_COMMIT_HASH}"
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
