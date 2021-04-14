import numpy as np
import matplotlib
matplotlib.use('TkAgg')
import matplotlib.pyplot as plt
import pandas as pd

num_simulations = 100
years = 5
base_expected_portfolio_return = 0.30
portfolio_return_volatility = 0.2

simulation_df = pd.DataFrame()

for s in range(num_simulations):
    return_series = [base_expected_portfolio_return]
    for m in range(years):
        ret = return_series[m] * (1 + np.random.normal(0, portfolio_return_volatility))
        return_series.append(ret)

    simulation_df[s] = return_series

fig = plt.figure()
plt.plot(simulation_df)
plt.show()