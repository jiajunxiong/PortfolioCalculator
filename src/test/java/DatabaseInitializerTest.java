import org.PortfolioCalculator.DatabaseInitializer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class DatabaseInitializerTest {

    private Connection connection;

    @BeforeEach
    public void setUp() throws SQLException {
        connection = DatabaseInitializer.initializeDatabase();
    }

    @AfterEach
    public void tearDown() throws SQLException {
        if (connection != null && !connection.isClosed()) {
            try (Statement stmt = connection.createStatement()) {
                stmt.execute("DROP TABLE IF EXISTS securities");
            }
            connection.close();
        }
    }

    @Test
    public void testInitializeDatabase() throws SQLException {
        assertNotNull(connection, "Connection should not be null");

        try (Statement stmt = connection.createStatement()) {
            ResultSet rs = stmt.executeQuery("SELECT * FROM INFORMATION_SCHEMA.TABLES WHERE TABLE_NAME = 'SECURITIES'");
            assertTrue(rs.next(), "Table 'securities' should exist");
        }
    }
}