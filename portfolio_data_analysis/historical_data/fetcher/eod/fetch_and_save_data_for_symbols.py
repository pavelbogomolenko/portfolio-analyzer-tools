from historical_data.fetcher.eod.symbols import ALL, CHINA_TECH


def fetch_and_save_data_for_symbols(path, fn):
    for symbol in CHINA_TECH:
        print("Fetching data for {}".format(symbol))
        raw_data = fn(symbol)
        filename = path + symbol + ".json"
        with open(filename, "w") as f:
            print("Writing data: {}".format(filename))
            f.write(raw_data)
