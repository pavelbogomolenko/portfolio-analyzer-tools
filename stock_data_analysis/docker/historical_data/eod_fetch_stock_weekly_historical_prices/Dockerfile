FROM python:3

WORKDIR /usr/src/app

COPY requirements.txt ./
RUN pip install --no-cache-dir -r requirements.txt

COPY . .
COPY ./historical_data/fetcher/eod/fetch_and_save_stock_weekly_historical_prices.py fetch_and_save_stock_weekly_historical_prices.py

RUN mkdir -p data/stockprice/weekly

CMD [ "python", "./fetch_and_save_stock_weekly_historical_prices.py" ]
