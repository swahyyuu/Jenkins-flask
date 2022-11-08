import boto3
import os 
import sys
import argparse


def arguparse():
  my_parser = argparse.ArgumentParser(prog='script',
                                      usage='%(prog)s [email_from] [email_to]',
                                      description='Script for sending success email',
                                      epilog='Thank you for using this script! :-)')

  my_parser.version='1.0'
  
  my_parser.add_argument('From', metavar='[email_from]', type=str, help='email sender')
  my_parser.add_argument('To', metavar='[email_to]', type=str, help='email receiver')

  args = my_parser.parse_args()

  email_from = args.From
  email_to = args.To 

  return email_from, email_to


if __name__ == '__main__':

  email_from, email_to = arguparse()

  ses = boto3.client("ses")

  EMAIL_FROM = email_from
  EMAIL_TO = email_to

  msg = "Jenkins pipeline running successfully"

  ses.send_email(
    Source = EMAIL_FROM,
    Destination = {
      'ToAddresses' : [EMAIL_TO]
    },
    Message = {
      'Subject' : {
        'Data' : ('Notification for Jenkins Pipeline Automatic Build Status')
      },
      'Body' : {
        'Text' : {
          'Data' : msg
        }
      }
    }
  ) 