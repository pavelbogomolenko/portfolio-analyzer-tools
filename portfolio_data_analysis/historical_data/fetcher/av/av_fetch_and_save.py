import os
import sys
import time

import historical_data.fetcher.av.symbols as symbols
from historical_data.provider.av.data import get_data_for_symbol
from historical_data.provider.av.data import AVAILABLE_FUNCTIONS

MIN_SEC_PER_REQUEST = 60 / 75


def __save_to_file__(f, data):
    with open(f, "w") as file:
        file.write(data)


def __fetch_and_save__(av_function_name, symbol, f):
    data = get_data_for_symbol(av_function_name, symbol)
    if data is None:
        return
    __save_to_file__(f, data)


def fetch_and_save_data_for_symbols(av_function_name, storage_path):
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