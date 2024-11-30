import org.PortfolioCalculator.GeometricBrownianMotion;
import org.PortfolioCalculator.MarketDataProvider;
import org.PortfolioCalculator.PortfolioSubscriber;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class MarketDataProviderTest {

    private MarketDataProvider marketDataProvider;
    private GeometricBrownianMotion gbm;
    private PortfolioSubscriber subscriber;
    private Map<String, Double> marketPrices;

    @BeforeEach
    public void setUp() {
        marketPrices = new HashMap<>();
        marketPrices.put("AAPL", 150.0);
        marketPrices.put("GOOGL", 2800.0);
        marketPrices.put("AAPL-C", 10.0);

        gbm = new GeometricBrownianMotion(0.1, 0.2) {
            @Override
            protected Random getRandom() {
                return new Random(42); // Fixed seed for reproducibility
            }
        };

        subscriber = new PortfolioSubscriber(new HashMap<>()) {
            @Override
            public void onPortfolioUpdate(Map<String, Double> updatedPrices) {
                // Custom implementation for testing
                marketPrices.putAll(updatedPrices);
            }
        };

        marketDataProvider = new MarketDataProvider(marketPrices, gbm, subscriber) {
            @Override
            protected Random getRandom() {
                return new Random(42); // Fixed seed for reproducibility
            }
        };
    }

    @Test
    public void testRun() throws InterruptedException {
        Thread thread = new Thread(marketDataProvider);
        thread.start();

        // Allow the thread to run for a short period
        Thread.sleep(2000);

        // Interrupt the thread to stop the loop
        thread.interrupt();
        thread.join();

        // Verify the market prices were updated
        assertEquals(199.2, marketPrices.get("AAPL"), 0.1);
        assertEquals(3719.4, marketPrices.get("GOOGL"), 0.1);
    }
}