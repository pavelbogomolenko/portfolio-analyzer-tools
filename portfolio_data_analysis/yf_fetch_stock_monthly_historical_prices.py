import os
import csv

from datetime import datetime as dt
import requests


from historical_data.symbols import MARKET_INDICIES

USER_AGENT_HEADERS = {'User-Agent': 'Mozilla/5.0 (Macintosh; Intel Mac OS X 10_10_1) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/39.0.2171.95 Safari/537.36'}
DIRNAME = os.path.dirname(os.path.abspath(__file__))
BASE_URL = 'https://query1.finance.yahoo.com/v7/finance/download/' \
           + '{}?period1=915145200&period2={}&interval=1d&events=history&includeAdjustedClose=true'


def fetch_raw_data_for_symbol(symbol):
    now_ts = round(dt.today().timestamp())
    url = BASE_URL.format(symbol, now_ts)
    print("Fetching data for: {}".format(url))
    r = requests.get(url, headers=USER_AGENT_HEADERS)
    if r.status_code != 200:
        print("API status_code was not 200. Will try again later")
        print(r.text)
        return None

    return r.text


def convert_yf_daily_historical_csv_price_data_to_av_monthly_json_format(symbol, raw_data):
    monthly_adjusted_ts = []
    prev_ts_date = None
    line = ""
    skip_row = 1
    raw_data_iterable = raw_data.split("\n")
    for row in csv.reader(raw_data_iterable):
        if skip_row == 1:
            skip_row = 0
            continue
        ts_date_row = row[0]
        ts_date = dt.strptime(ts_date_row, "%Y-%m-%d")
        if prev_ts_date is not None:
            if prev_ts_date.month != ts_date.month:
                monthly_adjusted_ts.append(line)
        prev_ts_date = ts_date
        line = '"' + row[0] + '":' + '{' + '"1. open":' + '"' + row[1] + '",' \
            '"2. high":' + '"' + row[2] + '",' \
            '"3. low":' + '"' + row[3] + '",' \
            '"4. close":' + '"' + row[4] + '",' \
            '"5. adjusted close":' + '"' + row[4] + '",' \
            '"6. volume":' + '"' + row[5] + '"' \
            + '}'
    av_json_str = '{"Meta Data": {"1. Information": "", "2. Symbol": "' + symbol + '", "4. Time Zone": "US/Eastern"},' \
                  + '"Monthly Adjusted Time Series": {' \
                  + ",".join(reversed(monthly_adjusted_ts)) + '}}'
    return av_json_str


def fetch_and_save_data_for_symbol(symbol):
    raw_data = fetch_raw_data_for_symbol(symbol)
    if raw_data is None:
        return

    path_to_file = os.path.join(DIRNAME, "data/stockprice/monthly/{}.json".format(symbol))
    with open(path_to_file, "w") as file:
        file.write(convert_yf_daily_historical_csv_price_data_to_av_monthly_json_format(symbol, raw_data))


def fetch_and_process_symbols():
    for symbol in MARKET_INDICIES:
        fetch_and_save_data_for_symbol(symbol)


if __name__ == '__main__':
    fetch_and_process_symbols()
