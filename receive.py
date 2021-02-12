#!/usr/bin/env python
import sys
import os
import json

import pika

import portfolio_optimizer


def message_dispatcher(body):
    d = json.loads(body)
    print(d['weights'])
    print(d['varCovarMatrix'])
    print(portfolio_optimizer.minimize_portfolio_stddev(d['varCovarMatrix']))


def main():
    connection = pika.BlockingConnection(pika.ConnectionParameters(host='localhost'))
    channel = connection.channel()

    channel.exchange_declare(exchange='portfoliomanager', exchange_type='topic', durable=False, auto_delete=False)
    queue = channel.queue_declare('pcq', exclusive=False, durable=False).method.queue

    channel.queue_bind(exchange='portfoliomanager', queue=queue, routing_key='portfoliomanager.#')

    def callback(ch, method, properties, body):
        decoded_body = body.decode()
        print(" [x] Received %r" % decoded_body)
        message_dispatcher(decoded_body)

    channel.basic_consume(queue, on_message_callback=callback, auto_ack=True)

    print(' [*] Waiting for messages. To exit press CTRL+C')
    channel.start_consuming()



if __name__ == '__main__':
    try:
        main()
    except KeyboardInterrupt:
        print('Interrupted')
        try:
            sys.exit(0)
        except SystemExit:
            os._exit(0)
