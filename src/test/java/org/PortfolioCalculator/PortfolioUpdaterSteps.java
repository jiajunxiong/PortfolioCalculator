package org.PortfolioCalculator;
import io.cucumber.java.en.*;

import org.junit.jupiter.api.Assertions;

import java.io.*;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class PortfolioUpdaterSteps {

    private Map<String, Integer> positions;
    private Connection connection;
    private Map<String, Double> marketPrices;
    private Thread marketDataThread;

    @Given("the positions file {string} contains:")
    public void the_positions_file_contains(String filePath, io.cucumber.datatable.DataTable dataTable) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            writer.write("Symbol,PositionSize\n");
            for (Map<Object, Object> row : dataTable.asMaps(String.class, String.class)) {
                writer.write(row.get("Symbol") + "," + row.get("PositionSize") + "\n");
            }
        }
        positions = PositionReader.readPositions(filePath);
    }

    @When("I run the PortfolioUpdater")
    public void i_run_the_portfolio_updater() throws SQLException, IOException, InterruptedException {
        connection = DatabaseInitializer.initializeDatabase();

        for (Map.Entry<String, Integer> entry : positions.entrySet()) {
            String ticker = entry.getKey();
            SecurityInserter.insertSecurities(ticker, connection);
        }

        SecurityQuery.querySecurityByTicker(connection, "AAPL");
        SecurityQuery.querySecurityByTicker(connection, "AAPL-OCT-2020-110-C");

        marketPrices = new ConcurrentHashMap<>();
        for (Map.Entry<String, Integer> entry : positions.entrySet()) {
            String ticker = entry.getKey();
            double strikePrice = MarketDataProvider.getStrikePrice(ticker);
            if (!MarketDataProvider.isCommonStock(ticker)) {
                marketPrices.put(ticker, 0.0);
                String stock = ticker.substring(0, ticker.indexOf("-"));
                if (!marketPrices.containsKey(stock)) {
                    double price = strikePrice + (Math.random() * (0.1 + 0.1) - 0.1) * strikePrice;
                    marketPrices.put(stock, price);
                }
            }
        }

        GeometricBrownianMotion gbm = new GeometricBrownianMotion(0.05, 0.2);
        PortfolioSubscriber subscriber = new PortfolioSubscriber(positions);
        MarketDataProvider marketDataProvider = new MarketDataProvider(marketPrices, gbm, subscriber);
        marketDataThread = new Thread(marketDataProvider);
        marketDataThread.start();
    }

    @Then("the database should be initialized")
    public void the_database_should_be_initialized() {
        Assertions.assertNotNull(connection);
    }
    
    @Then("the securities should be inserted")
    public void the_securities_should_be_inserted() throws SQLException {
        for (String ticker : positions.keySet()) {
            Assertions.assertTrue(SecurityQuery.querySecurityByTicker(connection, ticker));
        }
    }

    @Then("the security {string} should be queried")
    public void the_security_should_be_queried(String ticker) throws SQLException {
        Assertions.assertTrue(SecurityQuery.querySecurityByTicker(connection, ticker));
    }

    @Then("the market data provider should start")
    public void the_market_data_provider_should_start() {
        Assertions.assertTrue(marketDataThread.isAlive());
    }

    @Then("the market prices should be updated")
    public void the_market_prices_should_be_updated() {
        for (String ticker : marketPrices.keySet()) {
            Assertions.assertNotEquals(0.0, marketPrices.get(ticker));
        }
    }
}