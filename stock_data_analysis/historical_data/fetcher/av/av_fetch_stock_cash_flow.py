import os

from historical_data.fetcher.av.av_fetch_and_save import fetch_and_save_data_for_symbols
from historical_data.provider.av.data import AV_FUNCTION_CASH_FLOW

DIRNAME = os.path.dirname(os.path.abspath(__file__))

if __name__ == '__main__':
    path = os.path.join(DIRNAME, "data/stockcashflow/")
    fetch_and_save_data_for_symbols(AV_FUNCTION_CASH_FLOW, path)
