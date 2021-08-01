pipeline {

  environment {
    imagename = "rest-service/java-service"
    dockerImage = ''
  }
  agent any

  stages {
        stage('Cloning Git') {
          steps {
            git([url: 'https://github.com/darkkillex/rest-service.git', branch: 'master'])

          }
        }
        stage('Building image') {
          steps{
            script {
              dockerImage = docker.build imagename
            }
          }
        }
        stage('Test') {
            steps {
                echo 'Testing..'
            }
        }
        stage('Deploy') {
            steps {
                echo 'Deploying....'
            }
        }
    }
}
