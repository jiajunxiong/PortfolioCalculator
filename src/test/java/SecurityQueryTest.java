import org.PortfolioCalculator.SecurityQuery;
import org.h2.jdbcx.JdbcDataSource;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;

public class SecurityQueryTest {

    private Connection connection;

    @BeforeEach
    public void setUp() throws SQLException {
        JdbcDataSource ds = new JdbcDataSource();
        ds.setURL("jdbc:h2:mem:test;DB_CLOSE_DELAY=-1");
        connection = ds.getConnection();

        try (Statement stmt = connection.createStatement()) {
            stmt.execute("CREATE TABLE securities (" +
                    "id INT PRIMARY KEY, " +
                    "ticker VARCHAR(10), " +
                    "type VARCHAR(10), " +
                    "strike DOUBLE, " +
                    "maturity DATE, " +
                    "volatility DOUBLE, " +
                    "expected_return DOUBLE)");

            stmt.execute("INSERT INTO securities (id, ticker, type, strike, maturity, volatility, expected_return) VALUES " +
                    "(1, 'AAPL', 'stock', 0.0, '2022-12-31', 0.2, 0.1), " +
                    "(2, 'GOOGL', 'stock', 0.0, '2023-12-31', 0.25, 0.15)");
        }
    }

    @AfterEach
    public void tearDown() throws SQLException {
        if (connection != null && !connection.isClosed()) {
            try (Statement stmt = connection.createStatement()) {
                stmt.execute("DROP TABLE securities");
            }
            connection.close();
        }
    }

    @Test
    public void testQuerySecurityByTicker_ValidData() throws SQLException {
        assertTrue(SecurityQuery.querySecurityByTicker(connection, "AAPL"));
    }

    @Test
    public void testQuerySecurityByTicker_NoMatchingData() throws SQLException {
        assertFalse(SecurityQuery.querySecurityByTicker(connection, "MSFT"));
    }
}