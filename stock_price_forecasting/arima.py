import json
import os

import matplotlib.pyplot as plt
import pandas as pd
from pandas.plotting import lag_plot
from statsmodels.tsa.arima.model import ARIMA
from sklearn.metrics import mean_squared_error

DATA_FOLDER = "data/stockprice/monthly/"


def get_stock_price_historical_data(symbol):
    result = {}
    dirname = os.path.dirname(os.path.abspath(__file__))
    path_to_file = os.path.join(dirname, "{}{}.json".format(DATA_FOLDER, symbol))
    with open(path_to_file) as f:
        data = json.load(f)
        monthly_ts = data["Monthly Adjusted Time Series"]
        for k in reversed(monthly_ts.keys()):
            value = float(monthly_ts[k]["5. adjusted close"])
            result[k] = value
    return result


#  look for patterns in time series data (e.g auto-correlation)
def visualize_ts_patters(symbol):
    plt.figure()
    series = pd.Series(list(get_stock_price_historical_data(symbol).values()))
    lag_plot(series, lag=1)
    plt.show()


def visualize_forecaset_for_stock_in_sample_symbol(symbol):
    data = get_stock_price_historical_data(symbol)
    data_values = list(data.values())
    data_keys = list(data.keys())
    test_data = data_values[int(len(data_values) * 0.7):]
    prediction, error = arima_forecast_in_sample(symbol)
    x_range = data_keys[int(len(data_keys) * 0.7):]
    plt.plot(prediction, color='blue', marker='o', linestyle='dashed', label='Predicted Price')
    plt.plot(test_data, color='red', label='Actual Price')
    plt.title('Stock Price Prediction')
    plt.xlabel('Date')
    plt.ylabel('Prices')
    # plt.xticks(np.arange(881,1259,50), df.Date[881:1259:50])
    plt.legend()
    plt.show()


def visualize_forecaset_for_stock_out_of_sample_symbol(symbol, steps=2):
    history = arima_forecast_out_of_sample(symbol, steps)
    plt.plot(history[0:-2], color='blue', label='Actual Price')
    plt.plot([77, 78, 79], history[-3:], color='grey', marker='o', linestyle='dashed', label='Predicted Price')
    plt.title('Stock Price Prediction')
    plt.xlabel('Date')
    plt.ylabel('Prices')
    plt.legend()
    plt.show()


def arima_forecast_in_sample(symbol):
    data = list(get_stock_price_historical_data(symbol).values())
    # training (70 % ) and test (30%)
    training_data, test_data = data[0:int(len(data) * 0.7)], data[int(len(data) * 0.7):]

    history = [x for x in training_data]
    model_predictions = []
    test_observations_num = len(test_data)
    for time_point in range(test_observations_num):
        model = ARIMA(history, order=(4, 1, 0))
        model_fit = model.fit()
        output = model_fit.forecast()
        yhat = output[0]
        model_predictions.append(yhat)
        true_test_value = test_data[time_point]
        history.append(true_test_value)
    error = mean_squared_error(test_data, model_predictions)

    return model_predictions, error


def arima_forecast_out_of_sample(symbol, steps=1):
    data = list(get_stock_price_historical_data(symbol).values())
    training_data = data[int(len(data) * 0.7):]

    history = [x for x in training_data]
    # history[-1] = 57.5
    history[-1] = 131.46
    model = ARIMA(history, order=(4, 0, 0))
    model_fit = model.fit()
    forecast = model_fit.forecast(steps)
    for yhat in forecast:
        history.append(yhat)
    return history


if __name__ == '__main__':
    symbol = "AAPL"
    # visualize_forecaset_for_stock_in_sample_symbol(symbol)
    visualize_forecaset_for_stock_out_of_sample_symbol(symbol, 2)
