import os
import sys
from datetime import datetime as dt

from dateutil.relativedelta import relativedelta
import requests

import symbols

AV_API_URL = "https://www.alphavantage.co/"
AV_API_KEY = os.environ['AV_API_KEY']
DIRNAME = os.path.dirname(os.path.abspath(__file__))


def __fetch_data_for_symbol__(symbol):
    avl_fq_url = "{}query?function=TIME_SERIES_MONTHLY_ADJUSTED&symbol={}&apikey={}".format(AV_API_URL, symbol,
                                                                                            AV_API_KEY)
    print("Fetching data for: {}".format(symbol))
    r = requests.get(avl_fq_url)
    if r.status_code != 200:
        print("API status_code was not 200. Will try again later")
        return None

    if len(r.content) < 350:
        json_response = r.json()
        if json_response.get("Error Message") is not None:
            print("API response for: '{}' has errors. Empty data. Skipping...".format(symbol))
            return None
        if json_response.get("Note") is not None:
            print("API limit has been reached")
            sys.exit(0)

        print("API response too small for data for: '{}'. Will try again later".format(symbol))
        return None

    return r.text


def __save_to_file__(f, data):
    with open(f, "w") as file:
        file.write(data)


def __fetch_and_save__(symbol, f):
    data = __fetch_data_for_symbol__(symbol)
    if data is None:
        return
    __save_to_file__(f, data)


def __is_date_first_day_of_month__(date):
    first_day_of_month = dt.today().replace(day=1).date()
    return date == first_day_of_month


def __skip_update__(path_to_file):
    today = dt.today()
    if os.path.exists(path_to_file):
        last_modified = os.path.getmtime(path_to_file)
        dt_delta = relativedelta(today, dt.fromtimestamp(last_modified))
        if __is_date_first_day_of_month__(today.date()) and dt_delta.days > 0:
            return False
        if dt_delta.days < 4:
            print("Skip updating {}. Data is up-to-date".format(path_to_file))
            return True
    return False


def historical_data_fetcher():
    for symbol in symbols.ALL:
        path_to_file = os.path.join(DIRNAME, "data/stockprice/monthly/{}.json".format(symbol))
        if __skip_update__(path_to_file):
            print("Skip updating {}. Data is up-to-date".format(symbol))
            continue
        __fetch_and_save__(symbol, path_to_file)


if __name__ == '__main__':
    historical_data_fetcher()
