package org.PortfolioCalculator;

import org.apache.commons.math3.special.Erf;

public class BlackScholes {

    // Custom method to calculate the CDF of the standard normal distribution using the error function
    private static double normalCdf(double x) {
        return 0.5 * (1 + Erf.erf(x / Math.sqrt(2)));
    }

    // Calculate the call option price using the Black-Scholes formula
    public static double calculateCallPrice(double S, double K, double T, double r, double sigma) {
        double d1 = (Math.log(S / K) + (r + 0.5 * sigma * sigma) * T) / (sigma * Math.sqrt(T));
        double d2 = d1 - sigma * Math.sqrt(T);
        return S * normalCdf(d1) - K * Math.exp(-r * T) * normalCdf(d2);
    }

    // Calculate the put option price using the Black-Scholes formula
    public static double calculatePutPrice(double S, double K, double T, double r, double sigma) {
        double d1 = (Math.log(S / K) + (r + 0.5 * sigma * sigma) * T) / (sigma * Math.sqrt(T));
        double d2 = d1 - sigma * Math.sqrt(T);
        return K * Math.exp(-r * T) * normalCdf(-d2) - S * normalCdf(-d1);
    }
}