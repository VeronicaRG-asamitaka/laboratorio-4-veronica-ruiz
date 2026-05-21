package mx.com.axity.parque_dinosaurio.config;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

class ParkConfigTest {

    @BeforeEach
    void setUp() {
        ParkConfig.resetForTesting();
    }

    @AfterEach
    void tearDown() {
        ParkConfig.resetForTesting();
    }

    @Test
    void testSingleton_ReturnsSameInstance() {
        ParkConfig instance1 = ParkConfig.getInstance();
        ParkConfig instance2 = ParkConfig.getInstance();
        assertSame(instance1, instance2);
    }

    @Test
    void testGetInt_ExistingKey() {
        ParkConfig config = ParkConfig.getInstance();
        // Asumiendo que park.properties tiene simulation.totalSteps=100
        int steps = config.getInt("simulation.totalSteps", 999);
        assertEquals(100, steps);
    }

    @Test
    void testGetInt_DefaultValue() {
        ParkConfig config = ParkConfig.getInstance();
        int value = config.getInt("nonexistent.key", 42);
        assertEquals(42, value);
    }

    @Test
    void testGetDouble_ExistingKey() {
        ParkConfig config = ParkConfig.getInstance();
        double price = config.getDouble("arrival.ticketPrice", 0.0);
        assertEquals(25.0, price);
    }

    @Test
    void testGetSeed() {
        ParkConfig config = ParkConfig.getInstance();
        assertEquals(42L, config.getSeed());
    }

    @Test
    void testGetTotalSteps() {
        ParkConfig config = ParkConfig.getInstance();
        assertEquals(100, config.getTotalSteps());
    }
}