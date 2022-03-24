import simulate

import matplotlib
matplotlib.use('TkAgg')
import matplotlib.pyplot as plt
import pandas as pd


if __name__ == '__main__':
    result = simulate.portfolio_return_simulate(0.3, 0.2)
    fig = plt.figure()
    simulation_df = pd.DataFrame()
    for i, _ in enumerate(result):
        simulation_df[i] = result[i]
    plt.plot(simulation_df)
    plt.show()