from historical_data.provider.av import stock_monthly_adj_close_prices
from stock_price_forecasting.arima import *


if __name__ == '__main__':
    symbol = "T"
    adj_close_ts_data = stock_monthly_adj_close_prices.get(symbol)
    visualize_time_series_patters(adj_close_ts_data)
    visualize_out_of_sample_forecast(adj_close_ts_data, 3)
    # visualize_in_sample_forecast(adj_close_ts_data)