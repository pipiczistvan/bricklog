pipeline {
    agent any

    options {
        skipDefaultCheckout()
        buildDiscarder(logRotator(numToKeepStr: '3'))
    }

    parameters {
        choice(name: 'FLAVOR', choices: ['prod', 'dev', 'mock', 'benchmark'], description: 'Select the flavor')
        choice(name: 'MODE', choices: ['release', 'debug'], description: 'Select the mode')
        booleanParam(name: 'DISTRIBUTE', defaultValue: false, description: 'Enable to distribute to Firebase')
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
                    echo 'Decrypting release files'
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
                sh "./gradlew clean bundle${params.MODE.capitalize()}Apk -PREVISION=${env.GIT_COMMIT_HASH} -Pbuildkonfig.flavor=${params.FLAVOR}"
            }
        }

        stage('Distribute to Firebase') {
            when {
                expression { params.DISTRIBUTE == true }
            }
            steps {
                script {
                    def tagName = 'firebase-distribution'
                    def releaseNotes = ''

                    sh 'git fetch --tags'
                    // Fetch commits from the tag to HEAD (exclusive)
                    // This command lists commit messages after the tag up to the current commit
                    def commits = sh(
                        script: "git log ${tagName}..HEAD --pretty=format:'- %s'",
                        returnStdout: true
                    ).trim()

                    if (commits) {
                        releaseNotes = commits + "\n"
                    } else {
                        releaseNotes = "No changes since tag ${tagName}.\n"
                    }

                    writeFile file: 'release_notes.txt', text: releaseNotes

                    // Move the tag to the current commit (force move)
                    sh "git tag -f ${tagName} HEAD"

                    // Push the tag update to remote. Adjust 'origin' if necessary
                    withCredentials([usernamePassword(credentialsId: 'f26d5eb8-e0a9-4fce-b99e-7d04216f364e', usernameVariable: 'GIT_USERNAME', passwordVariable: 'GIT_PASSWORD')]) {
                        sh 'git remote set-url origin https://$GIT_USERNAME:$GIT_PASSWORD@github.com/pipiczistvan/bricklog.git'
                        sh "git push -f origin ${tagName}"
                    }
                }
                script {
                    echo 'Distributing app to Firebase'

                    sh './gradlew appDistributionUploadRelease'
                }
            }
        }

        stage('Archive') {
            steps {
                script {
                    def artifactsToArchive = """
                        composeApp/build/outputs/bundle/${params.MODE.toLowerCase()}/*.aab,
                        composeApp/build/outputs/apk/${params.MODE.toLowerCase()}/*.apk
                    """

                    if (params.MODE == 'release') {
                        artifactsToArchive += ",composeApp/build/outputs/mapping/release/mapping.txt"
                    }

                    archiveArtifacts fingerprint: true, onlyIfSuccessful: true, artifacts: artifactsToArchive
                }
            }
        }
    }
}
