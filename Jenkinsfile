#!groovy
script {
   properties(
      [
         parameters([
            string(defaultValue: 'development', name: 'ENV'),
            string(defaultValue: 'channel_name', name: 'CHANNEL_NAME')
         ])
      ]
   )
}

pipeline {
   agent any
   
   parameters {
      string(name: 'USERNAME_ACC', defaultValue: 'conan736', description: 'Username of DockerHub')
   }

   //It will trigger an auto build at 9.15 PM every day
   triggers {
      cron('15 21 * * * ')
   }

   stages {
      script {
         notifyBuild('STARTED')
      }
      stage('Docker Build Image') {
         when { branch 'main' }
         steps {
            sh "docker build -t ${params.USERNAME_ACC}/jenkins:2.0 ."           
         }
      }
      stage('Archive Flask App') {
         steps {
            script {
                  if (params.ENV == 'development') {
                     sh "echo you are in ${ENV} environment"
                  } else {
                     sh "echo you are not in ${ENV} environment, it will quit soon..."
                     currentBuild.result = 'SUCCESS'
                     return
                  }
               }
            sh "cat flask_app/app.py > flask_app/app.txt"
            archiveArtifacts allowEmptyArchive: true, artifacts: 'flask_app/app.txt', followSymlinks: false
            }
         }
      
      stage('Docker Push Image') {
         steps {
            withCredentials([usernamePassword(credentialsId: 'DockerHub', passwordVariable : 'dockerHubPass', usernameVariable: 'dockerHubUser')]) {
               sh "docker login -u ${env.dockerHubUser} -p ${env.dockerHubPass}"
               sh "docker push ${params.USERNAME_ACC}/jenkins:2.0"
            }
         }
      }
      stage('Deploy Container to Localhost') { 
         steps {
            echo "This is your username for DockerHub account : ${params.USERNAME_ACC}"
            sh "docker run -d -p 5002:80 --name flask_from_jenkins ${params.USERNAME_ACC}/jenkins:2.0"
         }
      }
      stage('Remove Deployed Container in Localhost') {
         steps {
            sh """ sleep 10
            docker rm -f flask_from_jenkins
            """
         }
      } 
   }
   post {
      always {
         script {
            if (currentBuild.result == 'SUCCESS') {
               notifyBuild(currentBuild.result)
            } else if (currentBuild.result == 'FAILED') {
               notifyBuild(currentBuild.result)
            } else {
               echo 'Unstable Build....'
            }
         }
      }
   }
}

def notifyBuild (String buildStatus = 'STARTED') {
   buildStatus = buildStatus ?: 'SUCCESS'

   def colorCode = '#FF0000'
   def summary = "${buildStatus} : Job Name '${env.JOB_NAME} | Build Number [${env.BUILD_NUMBER}] | URL : ${env.BUILD_URL}'"

   if (buildStatus == 'STARTED') {
      colorCode = '#FFFF00'
   } else if (buildStatus == 'SUCCESS') {
      colorCode = '#00FF00'
   } else {
      colorCode = '#FF0000'
   }

   slackSend (color: colorCode, channel: params.CHANNEL_NAME, message: summary)
}