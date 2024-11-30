package org.PortfolioCalculator;

import java.util.Random;

public class GeometricBrownianMotion {

    private final double mu; // Expected return
    private final double sigma; // Volatility
    private final Random random;

    protected Random getRandom() {
        return random;
    }

    public GeometricBrownianMotion(double mu, double sigma) {
        this.mu = mu;
        this.sigma = sigma;
        this.random = new Random();
    }

    public double getNextPrice(double currentPrice, double deltaT) {
        double epsilon = getRandom().nextGaussian();
        double deltaS = mu * currentPrice * deltaT + sigma * currentPrice * epsilon * Math.sqrt(deltaT);
        return Math.max(0, currentPrice + deltaS); // Ensure price is non-negative
    }
}