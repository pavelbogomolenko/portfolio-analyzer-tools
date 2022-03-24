FROM python:3

WORKDIR /usr/src/app

COPY requirements.txt ./
RUN pip install --no-cache-dir -r requirements.txt

COPY . .
COPY ./historical_data/fetcher/treasury_gov/fetch_and_save_daily_treasury_yields.py fetch_and_save_daily_treasury_yields.py

RUN mkdir -p data/government/bonds/usa/daily/

CMD [ "python", "./fetch_and_save_daily_treasury_yields.py" ]
