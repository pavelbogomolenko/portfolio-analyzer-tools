from datetime import datetime as dt
import requests

USER_AGENT_HEADERS = {'User-Agent': 'Mozilla/5.0 (Macintosh; Intel Mac OS X 10_10_1) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/39.0.2171.95 Safari/537.36'}
BASE_URL = 'https://query1.finance.yahoo.com/v7/finance/download/' \
           + '{}?period1=915145200&period2={}&interval=1d&events=history&includeAdjustedClose=true'


def get_data_for_symbol(symbol):
    now_ts = round(dt.today().timestamp())
    url = BASE_URL.format(symbol, now_ts)
    print("Fetching data for: {}".format(url))
    r = requests.get(url, headers=USER_AGENT_HEADERS)
    if r.status_code != 200:
        print("API status_code was not 200. Will try again later")
        print(r.text)
        return None

    return r.text