import os

from historical_data.provider.eod.data import get_stock_weekly_price_data_for_symbol
from historical_data.fetcher.eod.fetch_and_save_data_for_symbols import fetch_and_save_data_for_symbols

DIRNAME = os.path.dirname(os.path.abspath(__file__))


if __name__ == '__main__':
    path = os.path.join(DIRNAME, "data/stockprice/weekly/")
    fetch_and_save_data_for_symbols(path, get_stock_weekly_price_data_for_symbol)
