import sys
import time

import pika
from pika.exceptions import AMQPConnectionError


class RabbitBlockingTopicExchangeWrapper:
    MAX_CONN_RETRIES = 3

    def __init__(self, host, exchange):
        self.host = host
        self.exchange = exchange
        self.__channel, self.__connection = self.__connect()

    def __connect(self):
        retry_count = 0
        while True:
            try:
                connection = pika.BlockingConnection(pika.ConnectionParameters(host=self.host))
                channel = connection.channel()
                channel.exchange_declare(exchange=self.exchange, exchange_type="topic", durable=False, auto_delete=False)
                return channel, connection
            except AMQPConnectionError:
                if retry_count == self.MAX_CONN_RETRIES - 1:
                    print("unable to re-connect to rabbitmq after 3 tries")
                    sys.exit(1)
                print("trying to re-connect to rabbitmq..")
                time.sleep(5)
                retry_count += 1

    def consume(self, queue_name, routing_key, cb):
        queue = self.__channel.queue_declare(queue_name, exclusive=False, durable=False).method.queue
        self.__channel.queue_bind(exchange=self.exchange, queue=queue, routing_key=routing_key)

        self.__channel.basic_consume(queue, on_message_callback=cb, auto_ack=True)

        print(" [*] Waiting for messages.")
        self.__channel.start_consuming()

    def send_message(self, queue_name, routing_key, msg):
        self.__channel.queue_declare(queue=queue_name, exclusive=False, durable=False)

        print(" Sending message %s" % msg)
        self.__channel.basic_publish(exchange=self.exchange, routing_key=routing_key, body=msg)

    def send_message_and_close(self, queue_name, routing_key, msg):
        self.send_message(queue_name, routing_key, msg)
        self.__connection.close()
