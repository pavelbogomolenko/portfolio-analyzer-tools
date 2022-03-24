import os
import csv

from datetime import datetime as dt

from historical_data.provider.yf.data import get_data_for_symbol
from historical_data.fetcher.yf.symbols import MARKET_INDICIES


DIRNAME = os.path.dirname(os.path.abspath(__file__))


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
    raw_data = get_data_for_symbol(symbol)
    if raw_data is None:
        return

    path_to_file = os.path.join(DIRNAME, "data/stockprice/monthly/{}.json".format(symbol))
    with open(path_to_file, "w") as file:
        file.write(convert_yf_daily_historical_csv_price_data_to_av_monthly_json_format(symbol, raw_data))


def fetch_and_save_data_for_symbols():
    for symbol in MARKET_INDICIES:
        fetch_and_save_data_for_symbol(symbol)


if __name__ == '__main__':
    fetch_and_save_data_for_symbols()
