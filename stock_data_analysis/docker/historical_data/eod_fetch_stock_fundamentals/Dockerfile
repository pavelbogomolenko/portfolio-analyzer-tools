FROM python:3

WORKDIR /usr/src/app

COPY requirements.txt ./
RUN pip install --no-cache-dir -r requirements.txt

COPY . .
COPY ./historical_data/fetcher/eod/fetch_and_save_stock_fundamentals.py fetch_and_save_stock_fundamentals.py

RUN mkdir -p data/stockfundamentals

CMD [ "python", "./fetch_and_save_stock_fundamentals.py" ]
