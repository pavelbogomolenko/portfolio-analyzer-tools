import os

from historical_data.av_api_fetcher import data_fetcher, AV_FUNCTION_BALANCE_SHEET

DIRNAME = os.path.dirname(os.path.abspath(__file__))

if __name__ == '__main__':
    path = os.path.join(DIRNAME, "data/stockbalancesheet/")
    data_fetcher(AV_FUNCTION_BALANCE_SHEET, path)
