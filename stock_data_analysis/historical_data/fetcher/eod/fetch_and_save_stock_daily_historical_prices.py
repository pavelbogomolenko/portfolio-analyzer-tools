import os
from datetime import datetime
from functools import partial

from dateutil.relativedelta import relativedelta

from historical_data.provider.eod.data import get_stock_daily_price_data_for_symbol
from historical_data.fetcher.eod.fetch_and_save_data_for_symbols import fetch_and_save_data_for_symbols

DIRNAME = os.path.dirname(os.path.abspath(__file__))


if __name__ == '__main__':
    path = os.path.join(DIRNAME, "data/stockprice/daily/")
    years_ago = datetime.now() - relativedelta(years=15)
    date_from = years_ago.strftime("%Y-%m-%d")
    get_stock_daily_price_partial = partial(get_stock_daily_price_data_for_symbol, date_from=date_from)
    fetch_and_save_data_for_symbols(path, get_stock_daily_price_partial)
