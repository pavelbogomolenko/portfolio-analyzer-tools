import numpy as np

NUM_SIMULATIONS = 100
YEARS = 5


def portfolio_return_simulate(expected_portfolio_return, portfolio_return_volatility):
    result = []
    for s in range(NUM_SIMULATIONS):
        return_series = [expected_portfolio_return]
        for m in range(YEARS):
            ret = return_series[m] * (1 + np.random.normal(0, portfolio_return_volatility))
            return_series.append(ret)
        result.append(return_series)
    return result
