import json
import os

DATA_FOLDER = "data/stockprice/monthly/"


def get(symbol):
    result = []
    dirname = os.getcwd()
    path_to_file = os.path.join(dirname, "{}{}.json".format(DATA_FOLDER, symbol))
    with open(path_to_file) as f:
        data = json.load(f)
        monthly_ts = data["Monthly Adjusted Time Series"]
        for k in reversed(monthly_ts.keys()):
            value = float(monthly_ts[k]["5. adjusted close"])
            result.append(value)
    return result[:-1]