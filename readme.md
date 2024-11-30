### README
#### requirement gradle, java-jdk, junit, apache Erf, H2 Database, Cucumber

##### how to run
1. ./gradlew clean
2. ./gradlew build
3. ./gradlew run
4. ./gradlew cucumberCli


Example output:
```
Querying security by ticker 'AAPL':
ID: 1
Ticker: AAPL
Type: Stock
Strike: 0.0
Maturity: null
Volatility: 0.25
Expected Return: 0.05

Querying security by ticker 'AAPL-OCT-2020-110-C':
ID: 2
Ticker: AAPL-OCT-2020-110-C
Type: Call
Strike: 110.0
Maturity: 2020-10-31
Volatility: 0.25
Expected Return: 0.05
## 0 Market Data Update
AAPL change to 115.22776453210484
TELSA change to 467.7997158294821
Real-Time Portfolio Update:
Symbol                             price            qty               value
AAPL                              115.23           1000           115227.76
AAPL-OCT-2020-110-C                 9.26         -20000          -185252.52
AAPL-OCT-2020-110-P                 3.65          20000            73032.88
TELSA                             467.80           -500          -233899.86
TELSA-NOV-2020-400-C               18.41          10000           184076.36
TELSA-DEC-2020-400-P                3.57         -10000           -35669.20
Total Portfolio                                                   -82484.58

## 1 Market Data Update
AAPL change to 75.91596835681187
TELSA change to 484.5829435276412
Real-Time Portfolio Update:
Symbol                             price            qty               value
AAPL                               75.92           1000            75915.97
AAPL-OCT-2020-110-C                 9.97         -20000          -199478.54
AAPL-OCT-2020-110-P                33.01          20000           660228.09
TELSA                             484.58           -500          -242291.47
TELSA-NOV-2020-400-C               75.35          10000           753467.03
TELSA-DEC-2020-400-P                2.14         -10000           -21355.97
Total Portfolio                                                  1026485.11

## 2 Market Data Update
AAPL change to 92.24996738142544
TELSA change to 430.0085650122075
Real-Time Portfolio Update:
Symbol                             price            qty               value
AAPL                               92.25           1000            92249.97
AAPL-OCT-2020-110-C                 0.02         -20000             -437.83
AAPL-OCT-2020-110-P                17.49          20000           349866.37
TELSA                             430.01           -500          -215004.28
TELSA-NOV-2020-400-C               90.70          10000           906986.07
TELSA-DEC-2020-400-P               10.12         -10000          -101175.64
Total Portfolio                                                  1032484.66

## 3 Market Data Update
AAPL change to 109.32939592689888
TELSA change to 413.27897796492954
Real-Time Portfolio Update:
Symbol                             price            qty               value
AAPL                              109.33           1000           109329.40
AAPL-OCT-2020-110-C                 0.84         -20000           -16756.09
AAPL-OCT-2020-110-P                 5.94          20000           118830.51
TELSA                             413.28           -500          -206639.49
TELSA-NOV-2020-400-C               44.11          10000           441061.96
TELSA-DEC-2020-400-P               15.21         -10000          -152094.03
Total Portfolio                                                   293732.26
```