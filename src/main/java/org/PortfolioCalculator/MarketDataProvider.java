package org.PortfolioCalculator;

import java.util.Map;
import java.util.Random;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Locale;

public class MarketDataProvider implements Runnable {

    private final Map<String, Double> marketPrices;
    private final GeometricBrownianMotion gbm;
    private final PortfolioSubscriber subscriber;
    private final Random random;
    private int count;

    protected Random getRandom() {
        return random;
    }

    public MarketDataProvider(Map<String, Double> marketPrices, GeometricBrownianMotion gbm, PortfolioSubscriber subscriber) {
        this.marketPrices = marketPrices;
        this.gbm = gbm;
        this.subscriber = subscriber;
        this.random = new Random();
        this.count = 0;
    }

    public static int findNthOccurrence(String str, char targetChar, int n) {
        int occurrenceCount = 0;

        for (int i = 0; i < str.length(); i++) {
            if (str.charAt(i) == targetChar) {
                occurrenceCount++;
                if (occurrenceCount == n) {
                    return i; // Return the index of the nth occurrence
                }
            }
        }
        
        return -1; // Return -1 if nth occurrence is not found
    }

    public static boolean isCommonStock(String ticker) {
        return !(ticker.endsWith("-C") || ticker.endsWith("-P"));
    }

    public static boolean isCallOption(String ticker) {
        return ticker.endsWith("-C");
    }

    public static boolean isPutOption(String ticker) {
        return ticker.endsWith("-P");
    }

    public static double getStrikePrice(String ticker) {
        double strikePrice = 0.0;
        if(!isCommonStock(ticker)) {
            int index = findNthOccurrence(ticker, '-', 3);
            strikePrice = Double.parseDouble(ticker.substring(index + 1, ticker.lastIndexOf("-")));
        }
        return strikePrice;
    }

    public static LocalDate getMaturityDate(String ticker) {
        LocalDate date = null;
        if (!isCommonStock(ticker)) {
            int index1 = findNthOccurrence(ticker, '-', 1);
            int index2 = findNthOccurrence(ticker, '-', 2);
            int index3 = findNthOccurrence(ticker, '-', 3);
            String month = ticker.substring(index1 + 1, index2);
            String year = ticker.substring(index2 + 1, index3);
            try {
                // Define the input formatter for 'yyyy MMM' short month name
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy MMM", Locale.ENGLISH);
    
                // Parse the input string to a LocalDate
                YearMonth yearMonth = YearMonth.parse(year + " " + month.substring(0,1) + month.substring(1).toLowerCase(), formatter);
                int lastDayOfMonth = yearMonth.lengthOfMonth();
                
                // Set the date to the last day of the month
                date = yearMonth.atDay(lastDayOfMonth);

            } catch (DateTimeParseException e) {
                System.err.println("Invalid date format: " + e.getMessage());
            }
        }
        return date;
    }

    public double updateOptionPrice(String ticker) {
        String stockSymbol = ticker.substring(0, ticker.indexOf("-"));
        double stockPrice = marketPrices.get(stockSymbol);
        
        double strikePrice = getStrikePrice(ticker);
        double price = 0.0;
        if (isCallOption(ticker)) {
            price = BlackScholes.calculateCallPrice(stockPrice, strikePrice, 0.5, 0.02, 0.2);
        } else if (isPutOption(ticker)) {
            price = BlackScholes.calculatePutPrice(stockPrice, strikePrice, 0.5, 0.02, 0.2);
        }
        return price;
    }

    @Override
    public void run() {
        while (true) {
            try {
                // Simulate random interval between 0.5 to 2 seconds
                long sleepTime = 500 + getRandom().nextInt(1500);
                Thread.sleep(sleepTime);

                System.out.println("## " + count + " Market Data Update");
                // Update stock prices using Geometric Brownian Motion
                for (Map.Entry<String, Double> entry : marketPrices.entrySet()) {
                    String symbol = entry.getKey();
                    if(isCommonStock(symbol)) {
                        double currentPrice = entry.getValue();
                        double newPrice = gbm.getNextPrice(currentPrice, 1.0);
                        marketPrices.put(symbol, newPrice);
                        System.out.println(symbol + " change to " + newPrice);
                    } else {
                        double newPrice = updateOptionPrice(symbol);
                        marketPrices.put(symbol, newPrice);
                    }
                }
                count++;

                // Notify the portfolio subscriber about the price update
                subscriber.onPortfolioUpdate(marketPrices);

            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                System.out.println("Market data provider thread interrupted.");
                break;
            }
        }
    }
}
