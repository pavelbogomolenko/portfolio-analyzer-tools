### Build app
docker build . -t pavlobogomolenko/py_stock_price_historical_data_fetcher
### Run app
```
# create folder
mkdir -p data/stockprice/monthly
# run docker container
docker run -t -i -v $(pwd)/data:/usr/src/app/data \
    -e AV_API_KEY=YOUR_API_KEY pavlobogomolenko/py_stock_price_historical_data_fetcher