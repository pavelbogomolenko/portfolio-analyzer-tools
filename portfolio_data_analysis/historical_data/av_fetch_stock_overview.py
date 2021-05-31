from historical_data.av_api_fetcher import data_fetcher, AV_FUNCTION_STOCK_OVERVIEW

if __name__ == '__main__':
    data_fetcher(AV_FUNCTION_STOCK_OVERVIEW, "data/stock/overview/")
