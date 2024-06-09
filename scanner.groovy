/* groovylint-disable CompileStatic, DuplicateStringLiteral, NestedBlockDepth, NoDef, VariableTypeRequired */
@Library(['k8s-release-pipeline', 'k8s-jenkins-pipeline-lib'])
import static com.kubernetes.util.Constants.GIT_NOTIFICATION_SLACK_CHANNEL
import static com.kubernetes.util.Constants.GITHUB_TOOLS_PROD_CONFIG_CREDENTIAL_NAME
import static com.kubernetes.util.Constants.GIT_NOTIFICATION_SLACK_TOKEN_CREDENTIAL_NAME

String podTemplate = libraryResource 'yaml/podTemplateGoldenGOM.yaml'
pipeline {
    options {
        disableConcurrentBuilds()
        buildDiscarder(
            logRotator(numToKeepStr: '10')
        )
        timestamps()
        timeout(time: 15, unit: 'MINUTES')
        ansiColor('xterm')
    }
    agent {
        kubernetes {
            defaultContainer 'jnlp'
            yaml podTemplate
        }
    }
    triggers {
        cron('H 10 * * 1')
    }
    stages {
        stage('Set Build Parameters') {
            steps {
                script {
                    buildUser('k8s-argocd-apps')
                }
            }
        }
        stage('Run Scanner') {
            steps {
                script {
                    gitleaksScanner {
                        checkOutRepo = true
                        repoName = 'k8s-argocd-apps'
                        buildDirectoryName = 'k8s-argocd-apps'
                        branchName = 'master'
                        failOnFormatError = false
                        slackChannelName = GIT_NOTIFICATION_SLACK_CHANNEL
                        gitCredentialName = GITHUB_TOOLS_PROD_CONFIG_CREDENTIAL_NAME
                        slackTokenCredentialName = GIT_NOTIFICATION_SLACK_TOKEN_CREDENTIAL_NAME
                    }
                }
            }
        }
    }
}
