from flask import Flask 
app = Flask(__name__)

@app.route('/')
def my_flask_application():
   return "Welcome to flask application with jenkins pipeline"

if __name__ == '__main__':
   app.run(host='0.0.0.0', port=80, debug=True)