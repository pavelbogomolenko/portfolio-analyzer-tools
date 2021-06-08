FROM python:3

WORKDIR /usr/src/app

COPY requirements.txt ./
RUN pip install --no-cache-dir -r requirements.txt

COPY . .

RUN mkdir -p data/stockprice/monthly

CMD [ "python", "./yf_fetch_stock_monthly_historical_prices.py" ]
