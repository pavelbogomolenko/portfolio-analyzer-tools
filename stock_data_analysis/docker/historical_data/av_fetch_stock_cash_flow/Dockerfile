FROM python:3

WORKDIR /usr/src/app

COPY requirements.txt ./
RUN pip install --no-cache-dir -r requirements.txt

COPY . .
COPY ./historical_data/fetcher/av/av_fetch_stock_cash_flow.py av_fetch_stock_cash_flow.py

RUN mkdir -p data/stockcashflow

CMD [ "python", "./av_fetch_stock_cash_flow.py" ]
