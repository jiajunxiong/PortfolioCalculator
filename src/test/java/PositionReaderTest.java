import org.PortfolioCalculator.PositionReader;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class PositionReaderTest {

    private Path tempFile;

    @BeforeEach
    public void setUp() throws IOException {
        tempFile = Files.createTempFile("positions", ".csv");
    }

    @AfterEach
    public void tearDown() throws IOException {
        Files.deleteIfExists(tempFile);
    }

    @Test
    public void testReadPositions_ValidData() throws IOException {
        try (FileWriter writer = new FileWriter(tempFile.toFile())) {
            writer.write("symbol,positionSize\n");
            writer.write("AAPL,10\n");
            writer.write("GOOGL,5\n");
        }

        Map<String, Integer> positions = PositionReader.readPositions(tempFile.toString());
        assertEquals(2, positions.size());
        assertEquals(10, positions.get("AAPL"));
        assertEquals(5, positions.get("GOOGL"));
    }

    @Test
    public void testReadPositions_EmptyFile() throws IOException {
        try (FileWriter writer = new FileWriter(tempFile.toFile())) {
            writer.write("symbol,positionSize\n");
        }

        Map<String, Integer> positions = PositionReader.readPositions(tempFile.toString());
        assertEquals(0, positions.size());
    }

    @Test
    public void testReadPositions_InvalidData() throws IOException {
        try (FileWriter writer = new FileWriter(tempFile.toFile())) {
            writer.write("symbol,positionSize\n");
            writer.write("AAPL,ten\n");
        }

        assertThrows(NumberFormatException.class, () -> {
            PositionReader.readPositions(tempFile.toString());
        });
    }
}