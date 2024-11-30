package org.PortfolioCalculator;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;

public class PositionReader {

    public static Map<String, Integer> readPositions(String filePath) throws IOException {
        Map<String, Integer> positions = new LinkedHashMap<>();
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            br.readLine(); // Skip header
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                String symbol = parts[0];
                int positionSize = Integer.parseInt(parts[1]);
                positions.put(symbol, positionSize);
            }
        }
        return positions;
    }
}