#!groovy
Map modules = [:]
script {
   properties(
      [
         parameters([
            string(defaultValue: 'development', name: 'ENV'),
            string(defaultValue: 'learning-cicd', name: 'CHANNEL_NAME')
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
      stage('Docker Build Image') {
         steps {
         script {
            modules.first = load "func.groovy"
            modules.first.notifyBuild()
         }            
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
            sh """ sleep 5
            docker rm -f flask_from_jenkins
            """
         }
      } 
   }
   post {
      always {
         script {
            if (currentBuild.result == 'SUCCESS') {
               modules.first.notifyBuild(currentBuild.result)
            } else {
               modules.first.notifyBuild(currentBuild.result)
            }
         }
      }
   }
}