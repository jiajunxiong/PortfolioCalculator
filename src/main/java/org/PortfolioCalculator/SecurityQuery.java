package org.PortfolioCalculator;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class SecurityQuery {

    private static final String SELECT_SECURITY_BY_TICKER_SQL = "SELECT * FROM securities WHERE ticker = ?";
    public static boolean querySecurityByTicker(Connection connection, String ticker) throws SQLException {        
        try (PreparedStatement pstmt = connection.prepareStatement(SELECT_SECURITY_BY_TICKER_SQL)) {
            pstmt.setString(1, ticker);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    // A security with the given ticker was found
                    System.out.println("ID: " + rs.getInt("id"));
                    System.out.println("Ticker: " + rs.getString("ticker"));
                    System.out.println("Type: " + rs.getString("type"));
                    System.out.println("Strike: " + rs.getDouble("strike"));
                    System.out.println("Maturity: " + rs.getDate("maturity"));
                    System.out.println("Volatility: " + rs.getDouble("volatility"));
                    System.out.println("Expected Return: " + rs.getDouble("expected_return"));
                    return true; // Return true if a result is found
                } else {
                    // No security found with the given ticker
                    return false; // Return false if no result is found
                }
            }
        }
    }
}
