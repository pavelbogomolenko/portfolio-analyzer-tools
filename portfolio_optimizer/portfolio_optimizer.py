import math

import numpy as np
from scipy.optimize import minimize


def sharpe_ratio(rp, rf, sdp):
    return (rp - rf) / sdp


def __portfolio_expected_return_func(average_returns, sign=1.0):
    def f(weights):
        res = sign * np.dot(weights, average_returns)
        return res
    return f


def portfolio_expected_return(average_returns, weights):
    return __portfolio_expected_return_func(average_returns)(weights)


def maximize_portfolio_expected_return(average_returns):
    returns_len = len(average_returns)
    initial_guess = [1 / returns_len] * returns_len
    bounds = ((0, None),) * returns_len
    func = __portfolio_expected_return_func(average_returns, -1.0)
    constraints = ({'type': 'eq', 'fun': lambda w: sum(w) - 1},)

    result = minimize(func, initial_guess, method='SLSQP', bounds=bounds, constraints=constraints)
    if result.success:
        return {'res': abs(result.fun), 'weights': result.x.tolist()}
    return None


def __portfolio_stddev_func(var_covar_matrix):
    def f(weights):
        weights_row = np.array([weights])
        weights_col = np.array(weights).reshape(len(weights), 1)
        return math.sqrt(weights_row.dot(var_covar_matrix).dot(weights_col))
    return f


def portfolio_stddev(var_covar_matrix, weights):
    return __portfolio_stddev_func(var_covar_matrix)(weights)


def minimize_portfolio_stddev(var_covar_matrix):
    var_covar_len = len(var_covar_matrix)
    initial_guess = [1 / var_covar_len] * var_covar_len
    bounds = ((0, None),) * var_covar_len
    constraints = ({'type': 'eq', 'fun': lambda w: sum(w) - 1},)
    func = __portfolio_stddev_func(var_covar_matrix)

    result = minimize(func, initial_guess, method='SLSQP', bounds=bounds, constraints=constraints)
    if result.success:
        return {'res': result.fun, 'weights': result.x.tolist()}
    return None


if __name__ == '__main__':
    P_VAR_COVAR_MATRIX_EX = [
        [0.077954, 0.011816, 0.023064, 0.043168, 0.023529],
        [0.011816, 0.105067, 0.023125, 0.006284, 0.011748],
        [0.023064, 0.023125, 0.174433, 0.008508, 0.019677],
        [0.043168, 0.006284, 0.008508, 0.080181, 0.022632],
        [0.023529, 0.011748, 0.019677, 0.022632, 0.040280]
    ]
    average_annual_returns = [0.32, 0.002, 0.13, 0.126, 0.27]

    user_weights = [0.6, 0.1, 0.1, 0.1, 0.1]
    print("user weight return", portfolio_expected_return(average_annual_returns, user_weights))
    print("user weight (stddev)", portfolio_stddev(P_VAR_COVAR_MATRIX_EX, user_weights))

    eq_weights = [0.2, 0.2, 0.2, 0.2, 0.2]
    print("eq weight return", portfolio_expected_return(average_annual_returns, eq_weights))
    print("eq weight (stddev)", portfolio_stddev(P_VAR_COVAR_MATRIX_EX, eq_weights))

    minimized_std_dev = minimize_portfolio_stddev(P_VAR_COVAR_MATRIX_EX)
    print("efficient return (min stddev)", portfolio_expected_return(average_annual_returns, minimized_std_dev["weights"]))
    print("efficient (min) stddev", minimized_std_dev)

    max_return = maximize_portfolio_expected_return(average_annual_returns)
    print("max return", max_return)
    print("max return (stddev)", portfolio_stddev(P_VAR_COVAR_MATRIX_EX, max_return["weights"]))
