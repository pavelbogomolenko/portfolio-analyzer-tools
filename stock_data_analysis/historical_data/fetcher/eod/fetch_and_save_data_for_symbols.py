import os
import time

from datetime import datetime
from historical_data.fetcher.eod.symbols import ALL


def fetch_and_save_data_for_symbols(path, fn, skip_daily_update=True):
    today = time.strftime("%Y-%m-%d")
    for symbol in ALL:
        filename = path + symbol + ".json"

        if os.path.exists(filename):
            file_stat = os.stat(filename)
            file_dt = datetime.fromtimestamp(file_stat.st_mtime)
            if skip_daily_update and today == file_dt.strftime("%Y-%m-%d"):
                print("Skip updating {}".format(filename))
                continue

        raw_data = fn(symbol)
        if not raw_data:
            continue

        with open(filename, "w") as f:
            print("Writing data: {}".format(filename))
            f.write(raw_data)
