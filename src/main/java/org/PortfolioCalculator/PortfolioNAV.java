package org.PortfolioCalculator;

import java.util.Map;

public class PortfolioNAV {

    public static double calculatePortfolioNAV(Map<String, Integer> positions, Map<String, Double> marketPrices) {
        double totalNAV = 0.0;
        for (Map.Entry<String, Integer> entry : positions.entrySet()) {
            String symbol = entry.getKey();
            int positionSize = entry.getValue();
            double price = marketPrices.getOrDefault(symbol, 0.0);
            totalNAV += positionSize * price;
        }
        return totalNAV;
    }
}