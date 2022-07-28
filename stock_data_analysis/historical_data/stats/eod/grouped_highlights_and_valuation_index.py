import json


def make_index(group, overview_index_file, output_path):
    with open(overview_index_file) as f:
        content = json.loads(f.read())
        d_count = {}
        d = {}
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
                d_count[general[group]] += 1
            else:
                d_count[general[group]] = 1
                d[general[group]] = {"PERatio": pe, "PEGRatio": peg, "BookValue": book, "EarningsShare": earn_share,
                                     "ProfitMargin": pm, "OperatingMarginTTM": op, "RevenuePerShareTTM": rps,
                                     "QuarterlyRevenueGrowthYOY": qrg, "PriceSalesTTM": ps, "PriceBookMRQ": pb}

        for key in d.keys():
            s = d[key]
            s_count = d_count[key]
            pe = s["PERatio"] / s_count
            peg = s["PEGRatio"] / s_count
            book = s["BookValue"] / s_count
            earn_share = s["EarningsShare"] / s_count
            pm = s["ProfitMargin"] / s_count
            op = s["OperatingMarginTTM"] / s_count
            rps = s["RevenuePerShareTTM"] / s_count
            qrg = s["QuarterlyRevenueGrowthYOY"] / s_count
            ps = s["PriceSalesTTM"] / s_count
            pb = s["PriceBookMRQ"] / s_count
            d[key] = {"PERatio": pe, "PEGRatio": peg, "BookValue": book, "EarningsShare": earn_share,
                      "ProfitMargin": pm, "OperatingMarginTTM": op, "RevenuePerShareTTM": rps,
                      "QuarterlyRevenueGrowthYOY": qrg, "PriceSalesTTM": ps, "PriceBookMRQ": pb}

        index_output_file = output_path + group.lower() + ".json"
        with open(index_output_file, "w") as output_file:
            output_file.write(json.dumps(d))
