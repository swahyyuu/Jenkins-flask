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
return this