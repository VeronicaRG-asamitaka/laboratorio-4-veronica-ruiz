package mx.com.axity.parque_dinosaurio.model;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;
import java.util.List;

class TouristTest {

    private Tourist tourist;

    @BeforeEach
    void setUp() {
        tourist = new Tourist(1, "Alluka");
    }

    @Test
    void testInitialStatus() {
        assertEquals(TouristStatus.WAITING, tourist.getStatus());
    }

    @Test
    void testSpend() {
        tourist.spend(25.0);
        assertEquals(25.0, tourist.getMoneySpent());
        tourist.spend(10.5);
        assertEquals(35.5, tourist.getMoneySpent());
    }

    @Test
    void testRecordVisit() {
        tourist.recordVisit("Entrada");
        tourist.recordVisit("Tienda");
        List<String> visits = tourist.getVisitedZones();
        assertEquals(2, visits.size());
        assertTrue(visits.contains("Entrada"));
        assertTrue(visits.contains("Tienda"));
    }

    @Test
    void testSetStatus() {
        tourist.setStatus(TouristStatus.IN_PARK);
        assertEquals(TouristStatus.IN_PARK, tourist.getStatus());
        tourist.setStatus(TouristStatus.ATTACKED);
        assertEquals(TouristStatus.ATTACKED, tourist.getStatus());
    }
}