import os

from historical_data.av_stock_overview_index import create_update_stock_overview_index_file_from_path

DIRNAME = os.path.dirname(os.path.abspath(__file__))

if __name__ == '__main__':
    path = os.path.join(DIRNAME, "data/stockoverview/")
    create_update_stock_overview_index_file_from_path(path)
