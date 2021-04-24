#!/usr/bin/env python
import json
import os
import sys

from common.rabbitmq_wrapper import RabbitBlockingTopicExchangeWrapper
import frontier_compute.portfolio_optimizer as po

from worker_config import RABBITMQ_HOST, EXCHANGE, FRONTIER_PULL_ROUTING_KEY, \
    FRONTIER_PUSH_ROUTING_KEY, FRONTIER_PULL_QUEUE, FRONTIER_PUSH_QUEUE, \
    RISK_FREE_RATE, PSIM_PULL_QUEUE, PSIM_PULL_ROUTING_KEY


def send_message(msg, queue, routing_key):
    rmq_wrapper = RabbitBlockingTopicExchangeWrapper(RABBITMQ_HOST, EXCHANGE)
    rmq_wrapper.send_message_and_close(queue, routing_key, msg)


def message_dispatcher(body):
    d = json.loads(body)
    optimal_stddev = po.minimize_portfolio_stddev(d["varCovarMatrix"])
    optimal_return = po.portfolio_expected_return(d["returns"], optimal_stddev["weights"])
    optimal_sharpe_ratio = po.sharpe_ratio(optimal_return, RISK_FREE_RATE, optimal_stddev["res"])
    optimal_return_d = {
        "return": optimal_return,
        "stddev": optimal_stddev["res"],
        "weights": optimal_stddev["weights"],
        "sharpe_ratio": optimal_sharpe_ratio
    }

    max_return = po.maximize_portfolio_expected_return(d["returns"])
    max_return_stddev = po.portfolio_stddev(d["varCovarMatrix"], max_return["weights"])
    max_return_sharpe_ratio = po.sharpe_ratio(max_return["res"], RISK_FREE_RATE, max_return_stddev)
    max_return_d = {
        "return": max_return["res"],
        "stddev": max_return_stddev,
        "weights": max_return["weights"],
        "sharpe_ratio": max_return_sharpe_ratio
    }

    eq_weights = [1 / len(d["returns"])] * len(d["returns"])
    eq_weighted_return = po.portfolio_expected_return(d["returns"], eq_weights)
    eq_weighted_return_stddev = po.portfolio_stddev(d["varCovarMatrix"], eq_weights)
    eq_weighted_return_sharpe_ratio = po.sharpe_ratio(eq_weighted_return, RISK_FREE_RATE, eq_weighted_return_stddev)
    eq_weighted_return_d = {
        "return": eq_weighted_return,
        "stddev": eq_weighted_return_stddev,
        "weights": eq_weights,
        "sharpe_ratio": eq_weighted_return_sharpe_ratio
    }

    user_return = po.portfolio_expected_return(d["returns"], d["weights"])
    user_stddev = po.portfolio_stddev(d["varCovarMatrix"], d["weights"])
    user_sharpe_ratio = po.sharpe_ratio(user_return, RISK_FREE_RATE, user_stddev)
    user_return_d = {
        "return": user_return,
        "stddev": user_stddev,
        "weights": d["weights"],
        "sharpe_ratio": user_sharpe_ratio
    }

    frontier_dict = {
        "userId": d["userId"],
        "optimal": optimal_return_d,
        "max": max_return_d,
        "eq_weighted": eq_weighted_return_d,
        "user": user_return_d
    }
    send_message(json.dumps(frontier_dict), FRONTIER_PUSH_QUEUE, FRONTIER_PUSH_ROUTING_KEY)
    send_message(json.dumps(frontier_dict), PSIM_PULL_QUEUE, PSIM_PULL_ROUTING_KEY)


def consume():
    def callback(ch, method, properties, body):
        decoded_body = body.decode()
        print(" [x] Received %r" % decoded_body)
        message_dispatcher(decoded_body)

    rmq_wrapper = RabbitBlockingTopicExchangeWrapper(RABBITMQ_HOST, EXCHANGE)
    rmq_wrapper.consume(FRONTIER_PULL_QUEUE, FRONTIER_PULL_ROUTING_KEY, callback)


if __name__ == "__main__":
    try:
        consume()
    except KeyboardInterrupt:
        print("Interrupted")
        try:
            sys.exit(0)
        except SystemExit:
            os._exit(0)
