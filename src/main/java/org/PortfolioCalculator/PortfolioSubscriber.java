package org.PortfolioCalculator;

import java.util.Map;

public class PortfolioSubscriber {

    private final Map<String, Integer> positions;

    public PortfolioSubscriber(Map<String, Integer> positions) {
        this.positions = positions;
    }

    public void onPortfolioUpdate(Map<String, Double> marketPrices) {
        System.out.println("Real-Time Portfolio Update:");
        System.out.printf("%-30s%10s%15s%20s%n", "Symbol", "price", "qty", "value");
        for (Map.Entry<String, Integer> entry : positions.entrySet()) {
            String symbol = entry.getKey();
            int positionSize = entry.getValue();
            double price = marketPrices.getOrDefault(symbol, 0.0);
            System.out.printf("%-30s%10.2f%15d%20.2f%n",
                    symbol, price, positionSize, positionSize * price);
        }
        double nav = PortfolioNAV.calculatePortfolioNAV(positions, marketPrices);
        System.out.printf("Total Portfolio %59.2f%n", nav);
        System.out.println("");
    }
}