# Portfolio analysis

Project contains set of services that help to get, analyze and compare portfolio
historical data as long as portfolio individual components (stocks, funds, etfs)
from multiple sources

## Services overview

### Historical Data

#### Alphavantage Monthly Historical Prices

##### Build
```
# Build
docker build -f docker/historical_data/av_fetch_stock_monthly_historical_prices/Dockerfile . 
    -t pavlobogomolenko/av_fetch_stock_monthly_historical_prices
    
# Run
docker run -ti -v $(pwd)/data:/usr/src/app/data -e AV_API_KEY=YOUR_API_KEY pavlobogomolenko/av_fetch_stock_monthly_historical_prices

# Push
docker push pavlobogomolenko/av_fetch_stock_monthly_historical_prices
```    

#### Alphavantage Stock Overview

##### Build
```
# Build
docker build -f docker/historical_data/av_fetch_stock_overview/Dockerfile . 
    -t pavlobogomolenko/av_fetch_stock_overview
    
# Run
docker run -ti -v $(pwd)/data:/usr/src/app/data -e AV_API_KEY=YOUR_API_KEY pavlobogomolenko/av_fetch_stock_overview

# Push
docker push pavlobogomolenko/av_fetch_stock_overview
```    

#### Yahoo Finance Monthly Historical Prices

##### Build
```
# Build
docker build -f docker/historical_data/yf_fetch_stock_monthly_historical_prices/Dockerfile . 
    -t pavlobogomolenko/yf_fetch_stock_monthly_historical_prices
    
# Run
docker run -ti -v $(pwd)/data:/usr/src/app/data pavlobogomolenko/yf_fetch_stock_monthly_historical_prices

# Push
docker push pavlobogomolenko/yf_fetch_stock_monthly_historical_prices
```    

### Portfolio Frontier

### Portfolio Simulation

### Stock Price Forecasting
