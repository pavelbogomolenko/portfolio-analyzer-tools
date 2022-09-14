import json

from historical_data.cache.redis_wrapper import RedisWrapper
from historical_data.provider.fixer.xe import latest


def __save_rates__(data):
    ts = data["timestamp"]
    base = data["base"]
    date = data["date"]
    r = RedisWrapper()
    for rate_key in data["rates"].keys():
        id = base + rate_key
        rate = float(data["rates"][rate_key])
        r.add_one_to_hset("XeRate", id, "id", id)
        r.add_one_to_hset("XeRate", id, "ts", ts)
        r.add_one_to_hset("XeRate", id, "date", date)
        r.add_one_to_hset("XeRate", id, "base", base)
        r.add_one_to_hset("XeRate", id, "symbol", rate_key)
        r.add_one_to_hset("XeRate", id, "rate", rate)

        rev_id = rate_key + base
        r.add_one_to_hset("XeRate", rev_id, "id", rev_id)
        r.add_one_to_hset("XeRate", rev_id, "ts", ts)
        r.add_one_to_hset("XeRate", rev_id, "date", date)
        r.add_one_to_hset("XeRate", rev_id, "base", rate_key)
        r.add_one_to_hset("XeRate", rev_id, "symbol", base)
        r.add_one_to_hset("XeRate", rev_id, "rate", round(1.0 / rate, 5))


def fetch_and_save_latest_xe_rates_for_pairs(pairs):
    for cur_base in pairs.keys():
        symbols = pairs[cur_base]
        resp = latest(cur_base, symbols)
        if resp is None:
            print(f"no latest rates response for {cur_base} and {symbols}")
        data = json.loads(resp)
        __save_rates__(data)
