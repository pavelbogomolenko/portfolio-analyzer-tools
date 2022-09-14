import glob
import json

INDEX_OUTPUT_FILENAME = "index.json"
EXCLUDE_LIST = ["index.json", "df_index.json", "df_industry_ranked_key_highlights_index.json",
                "industry_grouped_key_valuations_highlights_index.json"]


def __filter_file__(filename):
    if filename.islower() is True:
        return True

    for exclude_file in EXCLUDE_LIST:
        if filename.find(exclude_file) > -1:
            return True

    return False


def create_update_stock_overview_index_file_from_path(path):
    overview_files = glob.glob(path + "*.json")
    json_lines = []
    for overview_file in overview_files:
        if __filter_file__(overview_file) is True:
            continue
        with open(overview_file) as f:
            try:
                print(overview_file)
                overview_content = json.loads(f.read())
                if not overview_content:
                    continue
                general = overview_content["General"]
                highlights = overview_content["Highlights"]
                valuation = overview_content["Valuation"]
                technicals = overview_content["Technicals"]
                stats = overview_content["SharesStats"]
                exch = ".F" if general["Exchange"] == "F" else ""
                code = general["Code"].lower() + exch.lower()
                d = {
                    "General": {
                        "Code": code,
                        "Type": general["Type"],
                        "Name": general["Name"],
                        "Description": "",
                        "Exchange": general["Exchange"],
                        "CurrencyCode": general["CurrencyCode"],
                        "Sector": general["Sector"],
                        "Industry": general["Industry"],
                        "CountryISO": general["CountryISO"],
                    },
                    "Highlights": {
                        "MarketCapitalization": highlights["MarketCapitalization"],
                        "EBITDA": highlights["EBITDA"],
                        "PERatio": highlights["PERatio"],
                        "PEGRatio": highlights["PEGRatio"],
                        "BookValue": highlights["BookValue"],
                        "EarningsShare": highlights["EarningsShare"],
                        "ProfitMargin":	highlights["ProfitMargin"],
                        "OperatingMarginTTM": highlights["OperatingMarginTTM"],
                        "RevenuePerShareTTM": highlights["RevenuePerShareTTM"],
                        "QuarterlyRevenueGrowthYOY": highlights["QuarterlyRevenueGrowthYOY"],
                    },
                    "Valuation": {
                        "PriceSalesTTM": valuation["PriceSalesTTM"],
                        "PriceBookMRQ": valuation["PriceBookMRQ"],
                    },
                    "Technicals": {
                        "Beta": technicals["Beta"],
                    },
                    "SharesStats": {
                        "SharesOutstanding": stats["SharesOutstanding"],
                    },
                }
                json_lines.append(d)
            except json.decoder.JSONDecodeError:
                continue

    index_output_file = path + INDEX_OUTPUT_FILENAME
    with open(index_output_file, "w") as output_file:
        output_file.write(json.dumps(json_lines))
