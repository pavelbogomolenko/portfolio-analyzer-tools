import os

import requests


API_BASE_URL = "https://eodhistoricaldata.com/api/"
EOD_BASE_URL = API_BASE_URL + "eod/"
FUNDAMENTALS_BASE_URL = API_BASE_URL + "fundamentals/"
EOD_API_KEY = os.environ["EOD_API_KEY"]


def __api_get_request__(endpoint):
    resp = requests.get(endpoint)
    if resp.status_code != 200:
        print("API status_code was not 200. Will try again later")
        return None

    return resp.text


def get_stock_daily_price_data_for_symbol(symbol, **kwargs):
    d_from = kwargs.get("date_from")
    d_to = kwargs.get("date_to")
    url_params = "{}?api_token={}&fmt=json&order=d&period=d".format(symbol, EOD_API_KEY)
    if d_from:
        url_params += "&from={}".format(d_from)
    if d_to:
        url_params += "&to={}".format(d_to)
    return __api_get_request__(EOD_BASE_URL + url_params)


def get_stock_monthly_price_data_for_symbol(symbol):
    url_params = "{}?api_token={}&fmt=json&order=d&period=m".format(symbol, EOD_API_KEY)
    return __api_get_request__(EOD_BASE_URL + url_params)


def get_stock_weekly_price_data_for_symbol(symbol):
    url_params = "{}?api_token={}&fmt=json&order=d&period=w".format(symbol, EOD_API_KEY)
    return __api_get_request__(EOD_BASE_URL + url_params)


def get_stock_fundamental_data_for_symbol(symbol):
    url_params = "{}?api_token={}".format(symbol, EOD_API_KEY)
    return __api_get_request__(FUNDAMENTALS_BASE_URL + url_params)
