package org.PortfolioCalculator;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class PortfolioUpdater {

    public static void main(String[] args) throws SQLException, IOException {
        Map<String, Integer> positions = PositionReader.readPositions("src/main/resources/positions.csv");

        try {
            // Initialize the database
            Connection connection = DatabaseInitializer.initializeDatabase();

            // Insert some securities
            for (Map.Entry<String, Integer> entry : positions.entrySet()) {
                String ticker = entry.getKey();
                SecurityInserter.insertSecurities(ticker, connection);
            }
            
            // Query a security by ticker
            System.out.println("Querying security by ticker 'AAPL':");
            SecurityQuery.querySecurityByTicker(connection, "AAPL");

            System.out.println("\nQuerying security by ticker 'AAPL-OCT-2020-110-C':");
            SecurityQuery.querySecurityByTicker(connection, "AAPL-OCT-2020-110-C");
            System.out.println("");

        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        // Initialize market prices (starting prices)
        Map<String, Double> marketPrices = new ConcurrentHashMap<>();
        for (Map.Entry<String, Integer> entry : positions.entrySet()) {
            String ticker = entry.getKey();
            double strikePrice = MarketDataProvider.getStrikePrice(ticker);
            if(!MarketDataProvider.isCommonStock(ticker)) {
                marketPrices.put(ticker, 0.0);
                String stock = ticker.substring(0, ticker.indexOf("-"));
                if (!marketPrices.containsKey(stock)) {
                    // Generate random price for the underlying stock
                    double price = strikePrice + (Math.random() * (0.1 + 0.1) - 0.1) * strikePrice;
                    marketPrices.put(stock, price);
                }
            }
        }
        
        // Initialize Geometric Brownian Motion for stock price simulation
        GeometricBrownianMotion gbm = new GeometricBrownianMotion(0.05, 0.2);

        // Initialize portfolio subscriber
        PortfolioSubscriber subscriber = new PortfolioSubscriber(positions);

        // Start market data provider in a separate thread
        MarketDataProvider marketDataProvider = new MarketDataProvider(marketPrices, gbm, subscriber);
        Thread marketDataThread = new Thread(marketDataProvider);
        marketDataThread.start();

        // Keep the main thread alive to allow real-time updates
        try {
            marketDataThread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}