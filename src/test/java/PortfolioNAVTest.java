import org.PortfolioCalculator.PortfolioNAV;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class PortfolioNAVTest {

    @Test
    public void testCalculatePortfolioNAV_ValidData() {
        Map<String, Integer> positions = new HashMap<>();
        positions.put("AAPL", 10);
        positions.put("GOOGL", 5);

        Map<String, Double> marketPrices = new HashMap<>();
        marketPrices.put("AAPL", 150.0);
        marketPrices.put("GOOGL", 2800.0);

        double result = PortfolioNAV.calculatePortfolioNAV(positions, marketPrices);
        System.out.println("Calculated NAV: " + result);
        assertEquals(1500.0 + 14000.0, result, 0.001);
    }

    @Test
    public void testCalculatePortfolioNAV_SymbolNotInMarketPrices() {
        Map<String, Integer> positions = new HashMap<>();
        positions.put("AAPL", 10);
        positions.put("MSFT", 5);

        Map<String, Double> marketPrices = new HashMap<>();
        marketPrices.put("AAPL", 150.0);

        double result = PortfolioNAV.calculatePortfolioNAV(positions, marketPrices);
        assertEquals(1500.0, result, 0.001);
    }

    @Test
    public void testCalculatePortfolioNAV_EmptyData() {
        Map<String, Integer> positions = new HashMap<>();
        Map<String, Double> marketPrices = new HashMap<>();

        double result = PortfolioNAV.calculatePortfolioNAV(positions, marketPrices);
        assertEquals(0.0, result, 0.001);
    }
}