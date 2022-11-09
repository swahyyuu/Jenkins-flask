import java.text.SimpleDateFormat
def dateFormat
def date 
def formattedDate

def notifyBuild (String buildStatus = 'STARTED') {
   buildStatus = buildStatus ?: 'SUCCESS'

   def dateTime = dateFormatted()
   def colorCode = '#FF0000'
   def summary = "${buildStatus} : Job Name '${env.JOB_NAME} | Build Number [${env.BUILD_NUMBER}] | URL : ${env.BUILD_URL} | Time : ${dateTime}'"

   if (buildStatus == 'STARTED') {
      colorCode = '#FFFF00'
   } else if (buildStatus == 'SUCCESS') {
      colorCode = '#00FF00'
   } else {
      colorCode = '#FF0000'
   }

   slackSend (color: colorCode, channel: params.CHANNEL_NAME, message: summary)
}

def dateFormatted () {
  dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss")
  date = new Date()
  formattedDate = dateFormat.format(date)
  return formattedDate
}

return this

