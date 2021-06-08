from historical_data.av_api_fetcher import data_fetcher, AV_FUNCTION_TS_MONTHLY_ADJUSTED

if __name__ == '__main__':
    data_fetcher(AV_FUNCTION_TS_MONTHLY_ADJUSTED, "data/stockprice/monthly/")
