import org.PortfolioCalculator.GeometricBrownianMotion;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class GeometricBrownianMotionTest {

    private GeometricBrownianMotion gbm;
    private static final double MU = 0.1; // Expected return
    private static final double SIGMA = 0.2; // Volatility
    private static final double DELTA_T = 1.0; // Time step
    private static final double CURRENT_PRICE = 100.0; // Current price

    @BeforeEach
    public void setUp() {
        gbm = new GeometricBrownianMotion(MU, SIGMA);
    }

    @Test
    public void testGetNextPrice_Reproducibility() {
        Random fixedRandom = new Random(42); // Fixed seed for reproducibility
        GeometricBrownianMotion gbmWithFixedRandom = new GeometricBrownianMotion(MU, SIGMA) {
            @Override
            protected Random getRandom() {
                return fixedRandom;
            }
        };

        double nextPrice = gbmWithFixedRandom.getNextPrice(CURRENT_PRICE, DELTA_T);
        assertEquals(132.8, nextPrice, 0.1); // Expected value based on fixed seed
    }

    @Test
    public void testGetNextPrice_NonNegative() {
        for (int i = 0; i < 1000; i++) {
            double nextPrice = gbm.getNextPrice(CURRENT_PRICE, DELTA_T);
            assertTrue(nextPrice >= 0, "Price should be non-negative");
        }
    }
}