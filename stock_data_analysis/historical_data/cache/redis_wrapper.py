import sys
import time

import hashlib
import redis


def make_md5_id(s):
    return hashlib.md5(s.lower().encode()).hexdigest()


class RedisWrapper:
    MAX_CONN_RETRIES = 3

    def __init__(self):
        try:
            if self.__conn is not None:
                pass
        except AttributeError:
            self.__conn = self.__connect__()

    def __del__(self):
        print("closing redis connection")
        self.__conn.close()

    def __connect__(self, redis_host="localhost", port=6379, db=0):
        retry_count = 0

        while True:
            try:
                r = redis.Redis(host=redis_host, port=port, db=db)
                return r
            except redis.ConnectionError:
                if retry_count == self.MAX_CONN_RETRIES - 1:
                    print("unable to re-connect to redis after 3 tries")
                    sys.exit(1)
                print("trying to re-connect to redis...")
                time.sleep(5)
                retry_count += 1

    def add_one_to_hset(self, name, id, key, value):
        md5_id = make_md5_id(id)
        self.__conn.hset(f"{name}:{md5_id}", key, value)

    def add_dict_to_hset(self, name, id, dict={}):
        for key in dict.keys():
            self.add_one_to_hset(name, id, key, dict[key])
