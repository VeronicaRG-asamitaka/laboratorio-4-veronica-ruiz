package mx.com.axity.parque_dinosaurio.zone;

import org.junit.jupiter.api.*;
import org.mockito.Mockito;

import mx.com.axity.parque_dinosaurio.persistance.CsvWriter;

import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

class PowerPlantTest {

    private PowerPlant plant;
    private Random rng;
    private CsvWriter mockCsvWriter;

    @BeforeEach
    void setUp() {
        plant = new PowerPlant(100.0, 1.5, 0.05, 200.0, 500.0);
        rng = new Random(42);
        mockCsvWriter = Mockito.mock(CsvWriter.class);
    }

    @Test
    void testInitialState() {
        assertTrue(plant.isOperational());
        assertEquals(100.0, plant.getEnergy());
    }

    @Test
    void testTriggerFailure() {
        plant.triggerFailure(mockCsvWriter);
        assertFalse(plant.isOperational());
        assertEquals(0.0, plant.getEnergy());
        Mockito.verify(mockCsvWriter, Mockito.atLeastOnce()).appendEvent(Mockito.any());
    }

    @Test
    void testRepair() {
        plant.triggerFailure(mockCsvWriter);
        plant.repair();
        assertTrue(plant.isOperational());
        assertEquals(100.0, plant.getEnergy());
    }

    @Test
    void testTickConsumesEnergy() {
        plant.tick(rng, mockCsvWriter);
        assertEquals(100.0 - 1.5, plant.getEnergy());
        assertTrue(plant.isOperational());
    }

    @Test
    void testTickEnergyNotBelowZero() {
        for (int i = 0; i < 70; i++) {
            plant.tick(rng, mockCsvWriter);
        }
        assertEquals(0.0, plant.getEnergy());
        assertFalse(plant.isOperational());
    }
}