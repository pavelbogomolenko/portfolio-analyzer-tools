import requests

USER_AGENT_HEADERS = {"User-Agent": "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_10_1)"
                                    " AppleWebKit/537.36 (KHTML, like Gecko) Chrome/39.0.2171.95 Safari/537.36"}
BASE_URL = "http://data.treasury.gov/feed.svc/DailyTreasuryYieldCurveRateData"


def get_daily_treasury_yield_data():
    print("Fetching data for: {}".format(BASE_URL))
    r = requests.get(BASE_URL, headers=USER_AGENT_HEADERS)
    if r.status_code != 200:
        print("HTTP status_code was not 200. Will try again later")
        print(r.text)
        return None

    return r.text
