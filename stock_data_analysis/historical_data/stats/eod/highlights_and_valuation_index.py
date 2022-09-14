import json


def make_index(group, overview_index_file, output_path):
    with open(overview_index_file) as f:
        content = json.loads(f.read())
        d_count = {}
        d = {}
        pm_s = []
        for line in content:
            general = line["General"]
            highlights = line["Highlights"]
            valuation = line["Valuation"]

            if general[group] not in d_count:
                d_count[general[group]] = {"PERatio": 0, "PEGRatio": 0, "BookValue": 0, "EarningsShare": 0,
                                           "ProfitMargin": 0, "OperatingMarginTTM": 0, "RevenuePerShareTTM": 0,
                                           "QuarterlyRevenueGrowthYOY": 0, "PriceSalesTTM": 0, "PriceBookMRQ": 0}

            pe = highlights["PERatio"] if highlights["PERatio"] is not None else 0
            if pe != 0:
                d_count[general[group]]["PERatio"] += 1
            peg = highlights["PEGRatio"] if highlights["PEGRatio"] is not None else 0
            if peg != 0:
                d_count[general[group]]["PEGRatio"] += 1
            book = highlights["BookValue"] if highlights["BookValue"] is not None else 0
            if book != 0:
                d_count[general[group]]["BookValue"] += 1
            earn_share = highlights["EarningsShare"] if highlights["EarningsShare"] is not None else 0
            if earn_share != 0:
                d_count[general[group]]["EarningsShare"] += 1
            pm = highlights["ProfitMargin"] if highlights["ProfitMargin"] is not None else 0
            if pm != 0:
                d_count[general[group]]["ProfitMargin"] += 1
            op = highlights["OperatingMarginTTM"] if highlights["OperatingMarginTTM"] is not None else 0
            if op != 0:
                d_count[general[group]]["OperatingMarginTTM"] += 1
            rps = highlights["RevenuePerShareTTM"] if highlights["RevenuePerShareTTM"] is not None else 0
            if rps != 0:
                d_count[general[group]]["RevenuePerShareTTM"] += 1
            qrg = highlights["QuarterlyRevenueGrowthYOY"] if highlights["QuarterlyRevenueGrowthYOY"] is not None else 0
            if qrg != 0:
                d_count[general[group]]["QuarterlyRevenueGrowthYOY"] += 1
            ps = valuation["PriceSalesTTM"] if valuation["PriceSalesTTM"] is not None else 0
            if ps != 0:
                d_count[general[group]]["PriceSalesTTM"] += 1
            pb = valuation["PriceBookMRQ"] if valuation["PriceBookMRQ"] is not None else 0
            if pb != 0:
                d_count[general[group]]["PriceBookMRQ"] += 1

            if general[group] == "Energy":
                pm_s.append(pm)
                print("ProfitMargin", pm)
                print("OperatingMarginTTM", op)

            if general[group] in d:
                sector = d[general[group]]
                pe += sector["PERatio"]
                peg += sector["PEGRatio"]
                book += sector["BookValue"]
                earn_share += sector["EarningsShare"]
                pm = sector["ProfitMargin"]
                op = sector["OperatingMarginTTM"]
                rps = sector["RevenuePerShareTTM"]
                qrg = sector["QuarterlyRevenueGrowthYOY"]
                ps += sector["PriceSalesTTM"]
                pb += sector["PriceBookMRQ"]
                d[general[group]] = {"PERatio": pe, "PEGRatio": peg, "BookValue": book, "EarningsShare": earn_share,
                                     "ProfitMargin": pm, "OperatingMarginTTM": op, "RevenuePerShareTTM": rps,
                                     "QuarterlyRevenueGrowthYOY": qrg, "PriceSalesTTM": ps, "PriceBookMRQ": pb}
            else:
                d[general[group]] = {"PERatio": pe, "PEGRatio": peg, "BookValue": book, "EarningsShare": earn_share,
                                     "ProfitMargin": pm, "OperatingMarginTTM": op, "RevenuePerShareTTM": rps,
                                     "QuarterlyRevenueGrowthYOY": qrg, "PriceSalesTTM": ps, "PriceBookMRQ": pb}

        for key in d.keys():
            s = d[key]
            s_count = d_count[key]
            if key == "Energy":
                print(key, s)
                print("-----------")
                print(key, s_count)
                print(pm_s)

            pe = s["PERatio"] / s_count["PERatio"] if s_count["PERatio"] > 0 else 1
            peg = s["PEGRatio"] / s_count["PEGRatio"] if s_count["PEGRatio"] > 0 else 1
            book = s["BookValue"] / s_count["BookValue"] if s_count["BookValue"] > 0 else 1
            earn_share = s["EarningsShare"] / s_count["EarningsShare"] if s_count["EarningsShare"] > 0 else 1
            pm = s["ProfitMargin"] / s_count["ProfitMargin"] if s_count["ProfitMargin"] > 0 else 1
            op = s["OperatingMarginTTM"] / s_count["OperatingMarginTTM"] if s_count["OperatingMarginTTM"] > 0 else 1
            rps = s["RevenuePerShareTTM"] / s_count["RevenuePerShareTTM"] if s_count["RevenuePerShareTTM"] > 0 else 1
            qrg = s["QuarterlyRevenueGrowthYOY"] / s_count["QuarterlyRevenueGrowthYOY"] if s_count["QuarterlyRevenueGrowthYOY"] > 0 else 1
            ps = s["PriceSalesTTM"] / s_count["PriceSalesTTM"] if s_count["PriceSalesTTM"] > 0 else 1
            pb = s["PriceBookMRQ"] / s_count["PriceBookMRQ"] if s_count["PriceBookMRQ"] > 0 else 1
            d[key] = {"PERatio": pe, "PEGRatio": peg, "BookValue": book, "EarningsShare": earn_share,
                      "ProfitMargin": pm, "OperatingMarginTTM": op, "RevenuePerShareTTM": rps,
                      "QuarterlyRevenueGrowthYOY": qrg, "PriceSalesTTM": ps, "PriceBookMRQ": pb}

        index_output_file = output_path + group.lower() + ".json"
        with open(index_output_file, "w") as output_file:
            output_file.write(json.dumps(d))


def convert_to_df_compatible(overview_index_file, output_path):
    with open(overview_index_file) as f:
        content = json.loads(f.read())
        json_lines = []
        for line in content:
            general = line["General"]
            highlights = line["Highlights"]
            valuation = line["Valuation"]

            pe = highlights["PERatio"] if highlights["PERatio"] is not None else 0
            peg = highlights["PEGRatio"] if highlights["PEGRatio"] is not None else 0
            book = highlights["BookValue"] if highlights["BookValue"] is not None else 0
            earn_share = highlights["EarningsShare"] if highlights["EarningsShare"] is not None else 0
            pm = highlights["ProfitMargin"] if highlights["ProfitMargin"] is not None else 0
            op = highlights["OperatingMarginTTM"] if highlights["OperatingMarginTTM"] is not None else 0
            rps = highlights["RevenuePerShareTTM"] if highlights["RevenuePerShareTTM"] is not None else 0
            qrg = highlights["QuarterlyRevenueGrowthYOY"] if highlights["QuarterlyRevenueGrowthYOY"] is not None else 0
            ps = valuation["PriceSalesTTM"] if valuation["PriceSalesTTM"] is not None else 0
            pb = valuation["PriceBookMRQ"] if valuation["PriceBookMRQ"] is not None else 0

            d = {
                "Sector": general["Sector"],
                "Industry": general["Industry"],
                "Code": general["Code"],
                "PERatio": pe, "PEGRatio": peg, "BookValue": book, "EarningsShare": earn_share,
                "ProfitMargin": pm, "OperatingMarginTTM": op, "RevenuePerShareTTM": rps,
                "QuarterlyRevenueGrowthYOY": qrg, "PriceSalesTTM": ps, "PriceBookMRQ": pb
            }
            json_lines.append(d)

        index_output_file = output_path + "df.json"
        with open(index_output_file, "w") as output_file:
            output_file.write(json.dumps(json_lines))