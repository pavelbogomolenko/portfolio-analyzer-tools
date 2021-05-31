import os
import sys
from datetime import datetime as dt

from dateutil.relativedelta import relativedelta
import requests

import symbols

AV_API_URL = "https://www.alphavantage.co/"
AV_API_KEY = os.environ['AV_API_KEY']
DIRNAME = os.path.dirname(os.path.abspath(__file__))
AV_FUNCTION_TS_MONTHLY_ADJUSTED = "TIME_SERIES_MONTHLY_ADJUSTED"
AV_FUNCTION_STOCK_OVERVIEW = "OVERVIEW"
AVAILABLE_FUNCTIONS = [AV_FUNCTION_TS_MONTHLY_ADJUSTED, AV_FUNCTION_STOCK_OVERVIEW]


def __fetch_data_for_symbol__(av_function_name, symbol):
    avl_fq_url = "{}query?function={}&symbol={}&apikey={}".format(AV_API_URL, av_function_name, symbol, AV_API_KEY)
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


def __fetch_and_save__(av_function_name, symbol, f):
    data = __fetch_data_for_symbol__(av_function_name, symbol)
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


def data_fetcher(av_function_name, storage_path):
    if av_function_name not in AVAILABLE_FUNCTIONS:
        print("Provided not valid AV function name: '{}'".format(av_function_name))
        sys.exit(0)

    if not os.path.exists(os.path.join(DIRNAME, storage_path)):
        print("Provided storage_path doesnt exists: '{}'".format(storage_path))
        sys.exit(0)

    for symbol in symbols.ALL:
        path_to_file = os.path.join(DIRNAME, "{}/{}.json".format(storage_path, symbol))
        if __skip_update__(path_to_file):
            print("Skip updating {}. Data is up-to-date".format(symbol))
            continue
        __fetch_and_save__(av_function_name, symbol, path_to_file)
