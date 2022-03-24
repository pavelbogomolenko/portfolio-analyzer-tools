import os
import sys

import requests

AV_API_URL = "https://www.alphavantage.co/"
AV_API_KEY = os.environ['AV_API_KEY']
AV_FUNCTION_TS_MONTHLY_ADJUSTED = "TIME_SERIES_MONTHLY_ADJUSTED"
AV_FUNCTION_STOCK_OVERVIEW = "OVERVIEW"
AV_FUNCTION_CASH_FLOW = "CASH_FLOW"
AV_FUNCTION_BALANCE_SHEET = "BALANCE_SHEET"
AVAILABLE_FUNCTIONS = [AV_FUNCTION_TS_MONTHLY_ADJUSTED, AV_FUNCTION_STOCK_OVERVIEW,
                       AV_FUNCTION_CASH_FLOW, AV_FUNCTION_BALANCE_SHEET]


def get_data_for_symbol(av_function_name, symbol):
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
