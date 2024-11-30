import org.PortfolioCalculator.PortfolioSubscriber;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class PortfolioSubscriberTest {

    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    private final PrintStream originalOut = System.out;

    @BeforeEach
    public void setUpStreams() {
        System.setOut(new PrintStream(outContent));
    }

    @AfterEach
    public void restoreStreams() {
        System.setOut(originalOut);
    }

    @Test
    public void testOnPortfolioUpdate_ValidData() {
        Map<String, Integer> positions = new HashMap<>();
        positions.put("AAPL", 10);
        positions.put("GOOGL", 5);

        Map<String, Double> marketPrices = new HashMap<>();
        marketPrices.put("AAPL", 150.0);
        marketPrices.put("GOOGL", 2800.0);

        PortfolioSubscriber subscriber = new PortfolioSubscriber(positions);
        subscriber.onPortfolioUpdate(marketPrices);

        String output = outContent.toString();
        assertTrue(output.contains("AAPL"));
        assertTrue(output.contains("GOOGL"));
        assertTrue(output.contains("1500.00"));
        assertTrue(output.contains("14000.00"));
        assertTrue(output.contains("Total Portfolio"));
    }

    @Test
    public void testOnPortfolioUpdate_SymbolNotInMarketPrices() {
        Map<String, Integer> positions = new HashMap<>();
        positions.put("AAPL", 10);
        positions.put("MSFT", 5);

        Map<String, Double> marketPrices = new HashMap<>();
        marketPrices.put("AAPL", 150.0);

        PortfolioSubscriber subscriber = new PortfolioSubscriber(positions);
        subscriber.onPortfolioUpdate(marketPrices);

        String output = outContent.toString();
        assertTrue(output.contains("AAPL"));
        assertTrue(output.contains("MSFT"));
        assertTrue(output.contains("1500.00"));
        assertTrue(output.contains("0.00"));
        assertTrue(output.contains("Total Portfolio"));
    }

    @Test
    public void testOnPortfolioUpdate_EmptyData() {
        Map<String, Integer> positions = new HashMap<>();
        Map<String, Double> marketPrices = new HashMap<>();

        PortfolioSubscriber subscriber = new PortfolioSubscriber(positions);
        subscriber.onPortfolioUpdate(marketPrices);

        String output = outContent.toString();
        assertTrue(output.contains("Total Portfolio"));
        assertTrue(output.contains("0.00"));
    }
}