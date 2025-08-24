def tagName = ""

pipeline {
    agent any

    options {
        skipDefaultCheckout()
        buildDiscarder(logRotator(numToKeepStr: '3'))
    }

    parameters {
        choice(name: 'ENVIRONMENT', choices: ['dev', 'prod'], description: 'Select the environment')
        choice(name: 'MODE', choices: ['release', 'debug'], description: 'Select the mode')
        choice(name: 'DEV_LEVEL', choices: ['0', '1', '2', '3'], description: 'Select the development level. Available options are:\n0 - production mode\n1 - development mode\n2 - mock mode\n3 - benchmark mode')
        booleanParam(name: 'TEST', defaultValue: true, description: 'Enable to run tests and generate coverage report')
        booleanParam(name: 'DISTRIBUTE', defaultValue: false, description: 'Enable to distribute to Firebase')
    }

    stages {
        stage('Init') {
            steps {
                script {
                    tagName = "firebase-distribution-${params.ENVIRONMENT.toLowerCase()}"
                }
            }
        }

        stage('Pre Clean Workspace') {
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

        stage('Prepare config files') {
            steps {
                script {
                    echo 'Decrypting files'
                    withCredentials([string(credentialsId: 'GPG_PASSPHRASE', variable: 'GPG_PASSPHRASE')]) {
                        sh 'gpg -d --passphrase "$GPG_PASSPHRASE" release/app-release.jks.gpg > release/app-release.jks'

                        if (params.ENVIRONMENT == 'dev') {
                            sh 'gpg -d --passphrase "$GPG_PASSPHRASE" release/dev.zip.gpg > release/dev.zip'
                        } else if (params.ENVIRONMENT == 'prod') {
                            sh 'gpg -d --passphrase "$GPG_PASSPHRASE" release/prod.zip.gpg > release/prod.zip'
                        }
                    }
                }
                script {
                    echo 'Unzipping files'

                    if (params.ENVIRONMENT == 'dev') {
                        unzip zipFile: 'release/dev.zip', dir: 'release'
                    } else if (params.ENVIRONMENT == 'prod') {
                        unzip zipFile: 'release/prod.zip', dir: 'release'
                    }
                }
                script {
                    echo 'Moving files to appropriate location'

                    if (params.ENVIRONMENT == 'dev') {
                        sh 'mv release/dev/google-services.json composeApp/'
                        sh 'mv release/dev/local.properties ./'
                    } else if (params.ENVIRONMENT == 'prod') {
                        sh 'mv release/prod/google-services.json composeApp/'
                        sh 'mv release/prod/local.properties ./'
                    }
                }
            }
        }

        stage('Code Linting') {
            steps {
                sh "./gradlew ktlintCommonMainSourceSetCheck"
            }
        }

        stage('Run tests') {
            when {
                expression { params.TEST == true }
            }
            steps {
                script {
                    echo 'Running tests with coverage report'
                    sh "./gradlew koverHtmlReport${params.ENVIRONMENT.capitalize()}${params.MODE.capitalize()}"
                }
                script {
                    echo 'Compressing test coverage report'
                    zip zipFile: 'composeApp/build/reports/kover/coverage.zip', dir: "composeApp/build/reports/kover/html${params.ENVIRONMENT.capitalize()}${params.MODE.capitalize()}"
                }
            }
        }

        stage('Build AAB') {
            steps {
                sh "./gradlew bundle${params.ENVIRONMENT.capitalize()}${params.MODE.capitalize()}Apk -PREVISION=${env.GIT_COMMIT_HASH} -PDEV_LEVEL=${params.DEV_LEVEL}"
            }
        }

        stage('Distribute to Firebase') {
            when {
                expression { params.DISTRIBUTE == true }
            }
            steps {
                script {
                    def releaseNotes = ''

                    sh 'git fetch --tags'

                    def tagExists = sh(
                        script: "git rev-parse -q --verify refs/tags/${tagName}",
                        returnStatus: true
                    ) == 0

                    if (tagExists) {
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
                    } else {
                        releaseNotes = "Initial release.\n"
                    }

                    writeFile file: 'release_notes.txt', text: releaseNotes
                }
                script {
                    echo 'Distributing app to Firebase'

                    sh "./gradlew appDistributionUpload${params.ENVIRONMENT.capitalize()}${params.MODE.capitalize()}"
                }
                script {
                    // Move the tag to the current commit (force move)
                    sh "git tag -f ${tagName} HEAD"

                    // Push the tag update to remote. Adjust 'origin' if necessary
                    withCredentials([usernamePassword(credentialsId: 'f26d5eb8-e0a9-4fce-b99e-7d04216f364e', usernameVariable: 'GIT_USERNAME', passwordVariable: 'GIT_PASSWORD')]) {
                        sh 'git remote set-url origin https://$GIT_USERNAME:$GIT_PASSWORD@github.com/pipiczistvan/bricklog.git'
                        sh "git push -f origin ${tagName}"
                    }
                }
            }
        }

        stage('Archive') {
            steps {
                script {
                    def artifactsToArchive = ""

                    if (params.TEST == true) {
                        artifactsToArchive += "composeApp/build/reports/kover/coverage.zip,"
                    }

                    artifactsToArchive += """
                        composeApp/build/outputs/bundle/${params.ENVIRONMENT.toLowerCase()}${params.MODE.capitalize()}/*.aab,
                        composeApp/build/outputs/apk/${params.ENVIRONMENT.toLowerCase()}/${params.MODE.toLowerCase()}/*.apk
                    """

                    if (params.MODE == 'release') {
                        artifactsToArchive += ",composeApp/build/outputs/mapping/${params.ENVIRONMENT.toLowerCase()}${params.MODE.capitalize()}/mapping.txt"
                    }

                    archiveArtifacts fingerprint: true, onlyIfSuccessful: true, artifacts: artifactsToArchive
                }
            }
        }

        stage('Post Clean Workspace') {
            steps {
                deleteDir()
            }
        }
    }
}
