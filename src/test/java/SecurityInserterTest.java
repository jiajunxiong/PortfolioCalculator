import org.PortfolioCalculator.SecurityInserter;
import org.h2.jdbcx.JdbcDataSource;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class SecurityInserterTest {

    private Connection connection;

    @BeforeEach
    public void setUp() throws SQLException {
        JdbcDataSource ds = new JdbcDataSource();
        ds.setURL("jdbc:h2:mem:test;DB_CLOSE_DELAY=-1");
        connection = ds.getConnection();

        try (Statement stmt = connection.createStatement()) {
            stmt.execute("CREATE TABLE securities (" +
                    "ticker VARCHAR(100), " +
                    "type VARCHAR(10), " +
                    "strike DOUBLE, " +
                    "maturity DATE, " +
                    "volatility DOUBLE, " +
                    "expected_return DOUBLE)");
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
    public void testInsertCommonStock() throws SQLException {
        String ticker = "AAPL";
        SecurityInserter.insertSecurities(ticker, connection);

        try (Statement stmt = connection.createStatement()) {
            ResultSet rs = stmt.executeQuery("SELECT * FROM securities WHERE ticker = 'AAPL'");
            if (rs.next()) {
                assertEquals("Stock", rs.getString("type"));
                assertEquals(0.25, rs.getDouble("volatility"));
                assertEquals(0.05, rs.getDouble("expected_return"));
            }
        }
    }

    @Test
    public void testInsertCallOption() throws SQLException {
        String ticker = "AAPL-DEC-2023-150-C";
        SecurityInserter.insertSecurities(ticker, connection);

        try (Statement stmt = connection.createStatement()) {
            ResultSet rs = stmt.executeQuery("SELECT * FROM securities WHERE ticker = 'AAPL-DEC-2023-150-C'");
            if (rs.next()) {
                assertEquals("Call", rs.getString("type"));
                assertEquals(150.0, rs.getDouble("strike"));
                assertEquals(java.sql.Date.valueOf("2023-12-31"), rs.getDate("maturity"));
                assertEquals(0.25, rs.getDouble("volatility"));
                assertEquals(0.05, rs.getDouble("expected_return"));
            }
        }
    }
}