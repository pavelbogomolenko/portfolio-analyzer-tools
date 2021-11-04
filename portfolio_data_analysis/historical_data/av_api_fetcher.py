import os
import sys
import time

import requests

import historical_data.symbols as symbols

AV_API_URL = "https://www.alphavantage.co/"
AV_API_KEY = os.environ['AV_API_KEY']
AV_FUNCTION_TS_MONTHLY_ADJUSTED = "TIME_SERIES_MONTHLY_ADJUSTED"
AV_FUNCTION_STOCK_OVERVIEW = "OVERVIEW"
MIN_SEC_PER_REQUEST = 60 / 75
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


def data_fetcher(av_function_name, storage_path):
    if av_function_name not in AVAILABLE_FUNCTIONS:
        print("Provided not valid AV function name: '{}'".format(av_function_name))
        sys.exit(0)

    if not os.path.exists(storage_path):
        print("Provided storage_path doesnt exists: '{}'".format(storage_path))
        sys.exit(0)

    for symbol in symbols.ALL:
        path_to_file = os.path.join(storage_path, "{}/{}.json".format(storage_path, symbol))
        before_fetch_time = time.time()
        __fetch_and_save__(av_function_name, symbol, path_to_file)
        fetch_and_save_seconds = time.time() - before_fetch_time
        if fetch_and_save_seconds >= MIN_SEC_PER_REQUEST:
            continue
        else:
            sec_to_next_fetch = MIN_SEC_PER_REQUEST - fetch_and_save_seconds
            time.sleep(sec_to_next_fetch)

