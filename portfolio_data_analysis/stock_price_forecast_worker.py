#!/usr/bin/env python
import asyncio

import json
import os
import sys

from common.rabbitmq_wrapper import RabbitBlockingTopicExchangeWrapper
from historical_data import stock_monthly_adj_close_prices
from stock_price_forecasting.arima import *


from worker_config import RABBITMQ_HOST, EXCHANGE, \
    SPF_PUSH_KEY, SPF_PUSH_QUEUE, SPF_PULL_QUEUE, SPF_PULL_KEY


async def async_compute_and_send_forecast_result_message(symbol, ts_data, user_id):
    result = out_of_sample_forecast(ts_data, 3)
    msg_dict = {
        "userId": user_id,
        "symbol": symbol,
        "forecast": result.tolist()
    }
    send_message(json.dumps(msg_dict))


async def run_concurrent_calculate_forecast(payload):
    async_tasks = []
    for s in payload["symbols"]:
        ts_data = stock_monthly_adj_close_prices.get(s)
        async_tasks.append(async_compute_and_send_forecast_result_message(s, ts_data, payload["userId"]))
    await asyncio.gather(*async_tasks)


def async_calculate_forecast_runner(payload):
    asyncio.run(run_concurrent_calculate_forecast(payload))


def send_message(msg):
    rmq_wrapper = RabbitBlockingTopicExchangeWrapper(RABBITMQ_HOST, EXCHANGE)
    rmq_wrapper.send_message_and_close(SPF_PUSH_QUEUE, SPF_PUSH_KEY, msg)


def message_dispatcher(body):
    d = json.loads(body)
    async_calculate_forecast_runner(d)


def consume():
    rmq_wrapper = RabbitBlockingTopicExchangeWrapper(RABBITMQ_HOST, EXCHANGE)
    rmq_wrapper.consume(SPF_PULL_QUEUE, SPF_PULL_KEY, message_dispatcher)


if __name__ == "__main__":
    try:
        consume()
    except KeyboardInterrupt:
        print("Interrupted")
        try:
            sys.exit(0)
        except SystemExit:
            os._exit(0)
