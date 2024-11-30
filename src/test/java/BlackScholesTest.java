import static org.junit.jupiter.api.Assertions.assertEquals;

import org.PortfolioCalculator.BlackScholes;
import org.junit.jupiter.api.Test;

public class BlackScholesTest {

    @Test
    public void testCalculateCallPrice() {
        // Test case 1
        double S = 100;  // Current stock price
        double K = 100;  // Strike price
        double T = 1;    // Time to expiration in years
        double r = 0.05; // Risk-free interest rate
        double sigma = 0.2; // Volatility

        double expectedCallPrice = 10.4506; // Expected value (derived from a reliable source)
        double actualCallPrice = BlackScholes.calculateCallPrice(S, K, T, r, sigma);
        
        // Allowable error margin
        double delta = 0.0001;
        assertEquals(expectedCallPrice, actualCallPrice, delta);
    }

    @Test
    public void testCalculatePutPrice() {
        // Test case 1
        double S = 100;  // Current stock price
        double K = 100;  // Strike price
        double T = 1;    // Time to expiration in years
        double r = 0.05; // Risk-free interest rate
        double sigma = 0.2; // Volatility

        double expectedPutPrice = 5.5735; // Expected value (derived from a reliable source)
        double actualPutPrice = BlackScholes.calculatePutPrice(S, K, T, r, sigma);
        
        // Allowable error margin
        double delta = 0.0001;
        assertEquals(expectedPutPrice, actualPutPrice, delta);
    }
}