FROM python:3

WORKDIR /usr/src/app

COPY requirements.txt ./
RUN pip install --no-cache-dir -r requirements.txt

COPY . .
COPY ./historical_data/fetcher/av/av_fetch_stock_overview.py av_fetch_stock_overview.py

RUN mkdir -p data/stockoverview

CMD [ "python", "./av_fetch_stock_overview.py" ]
