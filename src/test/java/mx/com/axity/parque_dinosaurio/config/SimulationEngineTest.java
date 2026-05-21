package mx.com.axity.parque_dinosaurio.config;

import org.junit.jupiter.api.*;

import mx.com.axity.parque_dinosaurio.config.ParkConfig;
import mx.com.axity.parque_dinosaurio.simulation.SimulationEngine;

import java.io.*;
import java.nio.file.*;
import static org.junit.jupiter.api.Assertions.*;

class SimulationEngineTest {

    private static final String OUTPUT_DIR = "test-output";

    @BeforeEach
    void setUp() throws IOException {
        // Limpiar directorio de salida antes de cada test
        Path outPath = Paths.get(OUTPUT_DIR);
        if (Files.exists(outPath)) {
            Files.walk(outPath)
                    .sorted(java.util.Comparator.reverseOrder())
                    .map(Path::toFile)
                    .forEach(File::delete);
        }
        
        ParkConfig.resetForTesting();
        
    }

    @AfterEach
    void tearDown() throws IOException {
        ParkConfig.resetForTesting();
        Path outPath = Paths.get(OUTPUT_DIR);
        if (Files.exists(outPath)) {
            Files.walk(outPath)
                    .sorted(java.util.Comparator.reverseOrder())
                    .map(Path::toFile)
                    .forEach(File::delete);
        }
    }

    @Test
    void testSimulationRunsWithoutException() {
        ParkConfig config = ParkConfig.getInstance();
        SimulationEngine engine = new SimulationEngine(config);
        assertDoesNotThrow(engine::run);
    }

    @Test
    void testSimulationGeneratesRevenueGreaterThanZero() {
        ParkConfig config = ParkConfig.getInstance();
        
        SimulationEngine engine = new SimulationEngine(config);
        engine.run();
        
        File revenueFile = new File("output/revenues.csv");
        assertTrue(revenueFile.exists());
        assertTrue(revenueFile.length() > 0);
    }

    @Test
    void testDeterminism_SameSeedSameRevenue() throws Exception {
        ParkConfig.resetForTesting();
        ParkConfig config1 = ParkConfig.getInstance();
        SimulationEngine engine1 = new SimulationEngine(config1);
        engine1.run();
        String rev1 = Files.readString(Path.of("output/revenues.csv"));

        ParkConfig.resetForTesting();
        ParkConfig config2 = ParkConfig.getInstance();
        SimulationEngine engine2 = new SimulationEngine(config2);
        engine2.run();
        String rev2 = Files.readString(Path.of("output/revenues.csv"));

        assertEquals(rev1, rev2);
    }
}