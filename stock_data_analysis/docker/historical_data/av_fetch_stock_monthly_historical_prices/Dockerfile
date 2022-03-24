FROM python:3

WORKDIR /usr/src/app

COPY requirements.txt ./
RUN pip install --no-cache-dir -r requirements.txt

COPY . .
COPY ./historical_data/fetcher/av/av_fetch_stock_monthly_historical_prices.py av_fetch_stock_monthly_historical_prices.py

RUN mkdir -p data/stockprice/monthly

CMD [ "python", "./av_fetch_stock_monthly_historical_prices.py" ]
