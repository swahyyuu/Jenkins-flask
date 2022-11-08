FROM  python

WORKDIR /app

ADD   ./flask_app /app 

RUN   pip install -r requirements.txt
RUN   pip install --upgrade pip 

EXPOSE 80 

ENV NAME world 

CMD [ "python", "app.py" ]