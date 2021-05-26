import matplotlib.pyplot as plt
import pandas as pd
from pandas.plotting import lag_plot
from statsmodels.tsa.arima.model import ARIMA
from sklearn.metrics import mean_squared_error


TRAINING_DATA_SPLIT_RATIO = 0.5
LAG_OBSERVATIONS_NUM = 4
DIFFERENCING_DEGREE = 1
MOVING_AV_WINDOW = 0
ARIMA_ORDER = (LAG_OBSERVATIONS_NUM, DIFFERENCING_DEGREE, MOVING_AV_WINDOW)


#  look for patterns in time series data (e.g auto-correlation)
def visualize_time_series_patters(ts_data):
    plt.figure()
    series = pd.Series(ts_data)
    lag_plot(series, lag=1)
    plt.show()


def visualize_in_sample_forecast(ts_data):
    history = ts_data[int(len(ts_data) * TRAINING_DATA_SPLIT_RATIO):]
    prediction, error = in_sample_forecast(ts_data)
    print("error", error)
    plt.plot(range(0, len(history)), prediction, color='blue', marker='o', linestyle='dashed', label='Predicted Price')
    plt.plot(range(0, len(history)), history, color='red', marker='o', label='Actual Price')
    plt.title('Stock Price Prediction')
    plt.xlabel('Date')
    plt.ylabel('Prices')
    plt.legend()
    plt.show()


def visualize_out_of_sample_forecast(ts_data, steps=2):
    history = ts_data[int(len(ts_data) * TRAINING_DATA_SPLIT_RATIO):]
    forecast = out_of_sample_forecast(ts_data, steps)
    plt.plot(history, color='blue', marker='o', label='Actual Price')
    plt.plot(range(len(history), len(history) + steps), forecast, color='grey', marker='o', linestyle='dashed', label='Predicted Price')
    plt.title('Stock Price Prediction')
    plt.xlabel('Date')
    plt.ylabel('Prices')
    plt.legend()
    plt.show()


def in_sample_forecast(ts_data):
    # training (70 % ) and test (30%)
    training_data, test_data = ts_data[0:int(len(ts_data) * TRAINING_DATA_SPLIT_RATIO)], ts_data[int(len(ts_data) * TRAINING_DATA_SPLIT_RATIO):]
    history = [x for x in training_data]
    model_predictions = []
    test_observations_num = len(test_data)
    for time_point in range(test_observations_num):
        model = ARIMA(history, order=ARIMA_ORDER)
        model_fit = model.fit()
        output = model_fit.forecast()
        yhat = output[0]
        model_predictions.append(yhat)
        actual_value = test_data[time_point]
        print(actual_value, yhat)
        history.append(actual_value)
    error = mean_squared_error(test_data, model_predictions)

    return model_predictions, error


def out_of_sample_forecast(ts_data, steps=1):
    training_data = ts_data[int(len(ts_data) * TRAINING_DATA_SPLIT_RATIO):]
    model = ARIMA(training_data, order=ARIMA_ORDER)
    model_fit = model.fit()
    return model_fit.forecast(steps)
