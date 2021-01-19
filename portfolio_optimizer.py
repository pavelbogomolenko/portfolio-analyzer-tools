import math

import numpy as np
from scipy.optimize import minimize


#           AMZN     MSFT     GOOGL    IBM      CSCO
# AMZN   0.007106 0.003409 0.003438 0.002831 0.002611
# MSFT   0.003409 0.003604 0.002411 0.001941 0.001770
# GOOGL  0.003438 0.002411 0.004139 0.002006 0.001479
# IBM    0.002831 0.001941 0.002006 0.004500 0.002768
# CSCO   0.002611 0.001770 0.001479 0.002768 0.004715
P_VAR_COVAR_MATRIX = np.array([
    [0.007106 * 12, 0.003409 * 12, 0.003438 * 12, 0.002831 * 12, 0.002611 * 12],
    [0.003409 * 12, 0.003604 * 12, 0.002411 * 12, 0.001941 * 12, 0.001770 * 12],
    [0.003438 * 12, 0.002411 * 12, 0.004139 * 12, 0.002006 * 12, 0.001479 * 12],
    [0.002831 * 12, 0.001941 * 12, 0.002006 * 12, 0.004500 * 12, 0.002768 * 12],
    [0.002611 * 12, 0.001770 * 12, 0.001479 * 12, 0.002768 * 12, 0.004715 * 12]
])


def portfolio_sd_func(weights):
    weights_row = np.array([weights])
    weights_col = np.array(weights).reshape(len(weights), 1)

    return math.sqrt(weights_row.dot(P_VAR_COVAR_MATRIX).dot(weights_col))


def minimze_sd_func():
    row, col = P_VAR_COVAR_MATRIX.shape
    initial_guess = [1 / row] * row
    bounds = ((0, None),) * row
    constraints = ({'type': 'eq', 'fun': lambda w: sum(w) - 1},)

    return minimize(portfolio_sd_func, initial_guess, method='SLSQP', bounds=bounds, constraints=constraints)


if __name__ == '__main__':
    print(minimze_sd_func())
