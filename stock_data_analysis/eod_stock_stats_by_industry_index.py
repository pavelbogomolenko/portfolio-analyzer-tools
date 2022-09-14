import os

from historical_data.stats.eod.highlights_and_valuation_index import create_df_file_from_fundamentals_index, \
    make_industry_grouped_valuation_index, make_industry_ranked_valuation_index

DIRNAME = os.path.dirname(os.path.abspath(__file__))
INDEX_FILE = "index.json"


if __name__ == '__main__':
    path = os.path.join(DIRNAME, "data/stockfundamentals/")

    df_file_path = create_df_file_from_fundamentals_index(path + INDEX_FILE, path)
    group_ranked_index = make_industry_ranked_valuation_index(df_file_path, path)
    make_industry_grouped_valuation_index(group_ranked_index, path)
