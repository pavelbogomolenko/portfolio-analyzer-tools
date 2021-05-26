#!/usr/bin/env python
import json
import os
import sys

from common.rabbitmq_wrapper import RabbitBlockingTopicExchangeWrapper
from portfolio_simulation.simulate import portfolio_return_simulate


from worker_config import RABBITMQ_HOST, EXCHANGE, \
    PSIM_PUSH_ROUTING_KEY, PSIM_PUSH_QUEUE, PSIM_PULL_QUEUE, PSIM_PULL_ROUTING_KEY


def send_message(msg):
    rmq_wrapper = RabbitBlockingTopicExchangeWrapper(RABBITMQ_HOST, EXCHANGE)
    rmq_wrapper.send_message_and_close(PSIM_PUSH_QUEUE, PSIM_PUSH_ROUTING_KEY, msg)


def message_dispatcher(body):
    d = json.loads(body)
    simulation_data = portfolio_return_simulate(d["optimal"]["return"], d["optimal"]["stddev"])
    result = {
        "userId": d["userId"],
        "simulation_data": simulation_data
    }
    send_message(json.dumps(result))


def consume():
    rmq_wrapper = RabbitBlockingTopicExchangeWrapper(RABBITMQ_HOST, EXCHANGE)
    rmq_wrapper.consume(PSIM_PULL_QUEUE, PSIM_PULL_ROUTING_KEY, message_dispatcher)


if __name__ == "__main__":
    try:
        consume()
    except KeyboardInterrupt:
        print("Interrupted")
        try:
            sys.exit(0)
        except SystemExit:
            os._exit(0)
