package mx.com.axity.parque_dinosaurio.zone;

import org.junit.jupiter.api.*;
import org.mockito.Mockito;

import mx.com.axity.parque_dinosaurio.model.*;
import mx.com.axity.parque_dinosaurio.persistance.CsvWriter;

import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ArrivalZoneTest {

    private ArrivalZone zone;
    private CsvWriter mockCsvWriter;

    @BeforeEach
    void setUp() throws IOException {
        zone = new ArrivalZone(30, 25.0);
        mockCsvWriter = Mockito.mock(CsvWriter.class);
    }

    @Test
    void testEnterAndProcessBatch() {
        Tourist t1 = new Tourist(1, "Juan");
        Tourist t2 = new Tourist(2, "Maria");

        t1.setStatus(TouristStatus.WAITING);
        t2.setStatus(TouristStatus.WAITING);

        zone.enter(t1);
        zone.enter(t2);
        assertEquals(2, zone.getCurrentOccupancy());

        List<Tourist> arrived = zone.processBatch(2, mockCsvWriter);
        assertEquals(2, arrived.size());
        assertEquals(0, zone.getCurrentOccupancy());

        for (Tourist t : arrived) {
            assertEquals(TouristStatus.IN_PARK, t.getStatus());
            assertEquals(25.0, t.getMoneySpent());
        }
    }

    @Test
    void testProcessBatchRespectsMaxCapacity() {
        for (int i = 0; i < 35; i++) {
            Tourist t = new Tourist(i, "T" + i);
            t.setStatus(TouristStatus.WAITING);
            if (zone.hasCapacity()) {
                zone.enter(t);
            }
        }
        assertEquals(30, zone.getCurrentOccupancy());

        List<Tourist> arrived = zone.processBatch(40, mockCsvWriter);
        assertEquals(30, arrived.size());
        assertEquals(0, zone.getCurrentOccupancy());
    }

    @Test
    void testProcessBatchWhenQueueEmpty() {
        List<Tourist> arrived = zone.processBatch(5, mockCsvWriter);
        assertTrue(arrived.isEmpty());
    }
}