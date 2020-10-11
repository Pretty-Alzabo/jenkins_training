#!/usr/bin/groovy

pipeline {
    agent any

    options {
        disableConcurrentBuilds()
    }

    stages {

        stage("Build Docker Image") {
            steps {
                buildApp()
            }
        }

        stage("Deploy - Dev") {
            steps {
                deploy('dev')
            }
        }

    }
}


// steps
def buildApp() {
    dir ('008_cd_docker/app' ) {
        pwsh "docker build `-t cicd/app:${env:BUILD_NUMBER} ."
    }
}

def deploy(environment) {

    def containerName = ''
    def port = ''

    if ("${environment}" == 'dev') {
        containerName = "app_dev"
        port = "8888"
    }
    else {
        println "Environment not valid"
        System.exit(0)
    }

    try {
        pwsh "docker stop $containerName"
        pwsh "docker docker rm $containerName"
    }catch(def e){
        pwsh "Write-Host 'nothing deleted'"
    }
    pwsh "docker run `-d `-p ${port}:5000 `--name ${containerName} cicd/app:${env:BUILD_NUMBER}"

}

