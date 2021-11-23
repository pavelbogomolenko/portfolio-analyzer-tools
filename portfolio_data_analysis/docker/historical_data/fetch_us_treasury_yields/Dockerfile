FROM python:3

WORKDIR /usr/src/app

COPY requirements.txt ./
RUN pip install --no-cache-dir -r requirements.txt

COPY . .

RUN mkdir -p data/government/bonds/usa/daily/

CMD [ "python", "./fetch_us_treasury_yields.py" ]
