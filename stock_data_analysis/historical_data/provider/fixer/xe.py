import os

import requests

API_BASE_URL = "https://api.apilayer.com/fixer/"
FIXER_API_KEY = os.environ["FIXER_API_KEY"]


def __api_get_request__(url):
    headers = {
        "apikey": FIXER_API_KEY
    }
    r = requests.request("GET", API_BASE_URL + url, headers=headers)
    if r.status_code != 200:
        print("API status_code was not 200. Will try again later")
        return None

    return r.text


def convert(frm, to, amount):
    url = f"convert?to={to}&from={frm}&amount={amount}"
    return __api_get_request__(url)


def latest(base, symbols=[], amount=1):
    s = ",".join(symbols)
    url = f"latest?base={base}&symbols={s}&amount={amount}"
    return __api_get_request__(url)
