import json

import numpy as np
import pandas as pd

from historical_data.cache.redis_wrapper import RedisWrapper, make_md5_id


STAT_METHODS = ["mean", "median", "min", "max"]


def create_df_file_from_fundamentals_index(fundamentals_index_file, output_path):
    with open(fundamentals_index_file) as f:
        content = json.loads(f.read())
        json_lines = []
        for line in content:
            general = line["General"]
            highlights = line["Highlights"]
            valuation = line["Valuation"]

            pe_ratio = highlights["PERatio"] if highlights["PERatio"] is not None else 0
            peg_ratio = highlights["PEGRatio"] if highlights["PEGRatio"] is not None else 0
            book = highlights["BookValue"] if highlights["BookValue"] is not None else 0
            eps = highlights["EarningsShare"] if highlights["EarningsShare"] is not None else 0
            pm = highlights["ProfitMargin"] if highlights["ProfitMargin"] is not None else 0
            op = highlights["OperatingMarginTTM"] if highlights["OperatingMarginTTM"] is not None else 0
            rps = highlights["RevenuePerShareTTM"] if highlights["RevenuePerShareTTM"] is not None else 0
            qrg = highlights["QuarterlyRevenueGrowthYOY"] if highlights["QuarterlyRevenueGrowthYOY"] is not None else 0
            ps_ratio = valuation["PriceSalesTTM"] if valuation["PriceSalesTTM"] is not None else 0
            pb_ratio = valuation["PriceBookMRQ"] if valuation["PriceBookMRQ"] is not None else 0

            d = {
                "sector": general["Sector"],
                "industry": general["Industry"],
                "code": general["Code"],
                "peRatio": pe_ratio,
                "pegRatio": peg_ratio,
                "bookValue": book,
                "eps": eps,
                "profitMargin": pm,
                "operatingMargin": op,
                "revenuePerShare": rps,
                "quarterlyRevenueGrowth": qrg,
                "psRatio": ps_ratio,
                "pbRatio": pb_ratio
            }
            json_lines.append(d)

        index_output_file = output_path + "df_index.json"
        with open(index_output_file, "w") as output_file:
            output_file.write(json.dumps(json_lines))
            return index_output_file


def make_industry_grouped_valuation_index(df_index_file, output_path):
    d = {}
    df = pd.read_json(df_index_file)

    dfg = df.groupby(["industry"])
    for col in df.keys():
        if col in ["code", "sector", "industry"]:
            continue

        for stat_method in STAT_METHODS:
            method = getattr(dfg[col], stat_method)
            series = method()
            for group_key in series.keys():
                if group_key not in d:
                    d[group_key] = {}

                prop = f"{col}{stat_method.capitalize()}"
                if prop not in d:
                    d[group_key][prop] = float(series[group_key])
    lines = []
    r = RedisWrapper()
    for industry in d.keys():
        rec = {
            "id": industry,
            "industry": industry,
            "hid": make_md5_id(industry),
            **d[industry]
        }
        lines.append(rec)
        r.add_dict_to_hset("IndustryGroupedKeyHighlightsIndex", industry, rec)

    file = output_path + "industry_grouped_key_valuations_highlights_index.json"
    with open(file, "w") as output_file:
        output_file.write(json.dumps(lines))
        return file


def make_industry_ranked_valuation_index(df_index_file, output_path):
    df = pd.read_json(df_index_file)
    df = df.replace(0, np.NaN)

    dfg = df.groupby(["industry"])
    for col in df.keys():
        if col in ["code", "sector", "industry"]:
            continue

        df_ranked = dfg[col].rank("average")
        df[f"{col}Rank"] = df_ranked

    r = RedisWrapper()
    for index, row in df.iterrows():
        for key in row.keys():
            val = row[key]
            if type(val) != str:
                if np.isnan(val):
                    val = 0
            r.add_one_to_hset("StockOverviewKeyHighlightsIndexWithRanks", row["code"], key, val)

    output_file = output_path + "df_industry_ranked_key_highlights_index.json"
    df.to_json(path_or_buf=output_file, orient="records")
    return output_file
