import os

from historical_data.av_api_fetcher import data_fetcher, AV_FUNCTION_CASH_FLOW

DIRNAME = os.path.dirname(os.path.abspath(__file__))

if __name__ == '__main__':
    path = os.path.join(DIRNAME, "data/stockcashflow/")
    data_fetcher(AV_FUNCTION_CASH_FLOW, path)
