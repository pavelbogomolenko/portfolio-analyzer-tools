import math

import numpy as np
from scipy.optimize import minimize


#           AMZN     MSFT     GOOGL    IBM      CSCO
# AMZN   0.007106 0.003409 0.003438 0.002831 0.002611
# MSFT   0.003409 0.003604 0.002411 0.001941 0.001770
# GOOGL  0.003438 0.002411 0.004139 0.002006 0.001479
# IBM    0.002831 0.001941 0.002006 0.004500 0.002768
# CSCO   0.002611 0.001770 0.001479 0.002768 0.004715
P_VAR_COVAR_MATRIX_EX = [
    [0.007106 * 12, 0.003409 * 12, 0.003438 * 12, 0.002831 * 12, 0.002611 * 12],
    [0.003409 * 12, 0.003604 * 12, 0.002411 * 12, 0.001941 * 12, 0.001770 * 12],
    [0.003438 * 12, 0.002411 * 12, 0.004139 * 12, 0.002006 * 12, 0.001479 * 12],
    [0.002831 * 12, 0.001941 * 12, 0.002006 * 12, 0.004500 * 12, 0.002768 * 12],
    [0.002611 * 12, 0.001770 * 12, 0.001479 * 12, 0.002768 * 12, 0.004715 * 12]
]


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
    average_monthly_returns = [0.15, 0.33, 0.36, 0.1, 0.1]

    user_weights = [0.6, 0.1, 0.1, 0.1, 0.1]
    print("user weight return", portfolio_expected_return(average_monthly_returns, user_weights))
    print("user weight (stddev)", portfolio_stddev(P_VAR_COVAR_MATRIX_EX, user_weights))

    eq_weights = [0.2, 0.2, 0.2, 0.2, 0.2]
    print("eq weight return", portfolio_expected_return(average_monthly_returns, eq_weights))
    print("eq weight (stddev)", portfolio_stddev(P_VAR_COVAR_MATRIX_EX, eq_weights))

    minimized_std_dev = minimize_portfolio_stddev(P_VAR_COVAR_MATRIX_EX)
    print("efficient (min) stddev", minimized_std_dev)
    print("efficient return (min stddev)", portfolio_expected_return(average_monthly_returns, minimized_std_dev["weights"]))

    max_return = maximize_portfolio_expected_return(average_monthly_returns)
    print("max return", max_return)
    print("max return (stddev)", portfolio_stddev(P_VAR_COVAR_MATRIX_EX, max_return["weights"]))
