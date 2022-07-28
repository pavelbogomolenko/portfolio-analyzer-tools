import os

from historical_data.stats.eod.grouped_highlights_and_valuation_index import make_index

DIRNAME = os.path.dirname(os.path.abspath(__file__))
INDEX_FILE = "index.json"
GROUPS = ["Sector", "Industry"]


if __name__ == '__main__':
    path = os.path.join(DIRNAME, "data/stockfundamentals/")
    for group in GROUPS:
        make_index(group, path + INDEX_FILE, path)
