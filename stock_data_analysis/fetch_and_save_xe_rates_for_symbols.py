from historical_data.fetcher.fixer.fetch_and_save_xe_rates import fetch_and_save_latest_xe_rates_for_pairs

PAIRS = {
    "USD": ["EURO", "HKD", "JPY", "MXN", "CHF", "CNY"]
}

if __name__ == '__main__':
    fetch_and_save_latest_xe_rates_for_pairs(PAIRS)
