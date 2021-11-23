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

#### Alphavantage Stock Overview Index

##### Build
```
# Build
docker build -f docker/historical_data/av_stock_overview_index/Dockerfile .
    -t pavlobogomolenko/av_stock_overview_index

# Run
docker run -ti -v $(pwd)/data:/usr/src/app/data -e AV_API_KEY=YOUR_API_KEY pavlobogomolenko/av_stock_overview_index

# Push
docker push pavlobogomolenko/av_stock_overview_index
```

#### Alphavantage Stock Cash Flow

##### Build
```
# Build
docker build -f docker/historical_data/av_fetch_stock_cash_flow/Dockerfile .
    -t pavlobogomolenko/av_fetch_stock_cash_flow

# Run
docker run -ti -v $(pwd)/data:/usr/src/app/data -e AV_API_KEY=YOUR_API_KEY pavlobogomolenko/av_fetch_stock_cash_flow

# Push
docker push pavlobogomolenko/av_fetch_stock_cash_flow
```

#### Alphavantage Stock Balance Sheet

##### Build
```
# Build
docker build -f docker/historical_data/av_fetch_stock_balance_sheet/Dockerfile .
    -t pavlobogomolenko/av_fetch_stock_balance_sheet

# Run
docker run -ti -v $(pwd)/data:/usr/src/app/data -e AV_API_KEY=YOUR_API_KEY pavlobogomolenko/av_fetch_stock_balance_sheet

# Push
docker push pavlobogomolenko/av_fetch_stock_balance_sheet
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

#### treasury.gov fetch daily treasury yields

##### Build
```
# Build
docker build -f docker/historical_data/fetch_us_treasury_yields/Dockerfile . 
    -t pavlobogomolenko/fetch_us_treasury_yields

# Run
docker run -ti -v $(pwd)/data:/usr/src/app/data pavlobogomolenko/fetch_us_treasury_yields

# Push
docker push pavlobogomolenko/fetch_us_treasury_yields
```

### Portfolio Frontier

### Portfolio Simulation

### Stock Price Forecasting
