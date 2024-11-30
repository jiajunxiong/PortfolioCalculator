package org.PortfolioCalculator;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class SecurityInserter {

    private static final String INSERT_SECURITY_SQL = "INSERT INTO securities (ticker, type, strike, maturity, volatility, expected_return) " +
            "VALUES (?, ?, ?, ?, ?, ?)";

    public static void insertSecurities(String ticker, Connection connection) throws SQLException {
        try (PreparedStatement pstmt = connection.prepareStatement(INSERT_SECURITY_SQL)) {
            
            pstmt.setString(1, ticker);
            if(MarketDataProvider.isCommonStock(ticker)){
                pstmt.setString(2, "Stock");
                pstmt.setNull(3, java.sql.Types.DOUBLE); // No strike for stocks
                pstmt.setNull(4, java.sql.Types.DATE); // No maturity for stocks
            }
            else {
                if (MarketDataProvider.isCallOption(ticker)){
                    pstmt.setString(2, "Call");
                } else {
                    pstmt.setString(2, "Option");
                }
                pstmt.setDouble(3, MarketDataProvider.getStrikePrice(ticker)); // Strike price
                pstmt.setDate(4, java.sql.Date.valueOf(MarketDataProvider.getMaturityDate(ticker))); // Maturity date
            }
            pstmt.setDouble(5, 0.25); // Volatility
            pstmt.setDouble(6, 0.05); // Expected return
            pstmt.executeUpdate();
        }
    }
}
