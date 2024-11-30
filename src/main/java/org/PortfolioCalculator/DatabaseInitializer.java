package org.PortfolioCalculator;

import org.h2.jdbcx.JdbcDataSource;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseInitializer {

    private static final String CREATE_TABLE_SQL = "CREATE TABLE IF NOT EXISTS securities (" +
            "id INT AUTO_INCREMENT PRIMARY KEY, " +
            "ticker VARCHAR(100) NOT NULL, " +
            "type VARCHAR(10) NOT NULL, " +
            "strike DOUBLE, " +
            "maturity DATE, " +
            "volatility DOUBLE, " +
            "expected_return DOUBLE" +
            ");";

    public static Connection initializeDatabase() throws SQLException {
        JdbcDataSource ds = new JdbcDataSource();
        ds.setURL("jdbc:h2:mem:test;DB_CLOSE_DELAY=-1");
        Connection connection = ds.getConnection();
        try (Statement stmt = connection.createStatement()) {
            stmt.execute(CREATE_TABLE_SQL);
        }
        return connection;
    }
}