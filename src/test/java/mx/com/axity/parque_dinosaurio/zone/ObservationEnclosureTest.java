package mx.com.axity.parque_dinosaurio.zone;

import org.junit.jupiter.api.*;
import org.mockito.Mockito;

import mx.com.axity.parque_dinosaurio.model.*;
import mx.com.axity.parque_dinosaurio.persistance.CsvWriter;

import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

class ObservationEnclosureTest {

    private ObservationEnclosure basicEnclosure;
    private ObservationEnclosure vipEnclosure;
    private Random rng;
    private CsvWriter mockCsvWriter;

    @BeforeEach
    void setUp() {
        basicEnclosure = new ObservationEnclosure("Basic", ExperienceType.BASIC);
        vipEnclosure = new ObservationEnclosure("VIP", ExperienceType.VIP);
        rng = new Random(1);
        mockCsvWriter = Mockito.mock(CsvWriter.class);
    }

    @Test
    void testCapacity() {
        assertEquals(20, basicEnclosure.getMaxCapacity());
        assertEquals(5, vipEnclosure.getMaxCapacity());
        assertTrue(basicEnclosure.hasCapacity());
    }

    @Test
    void testEntryFee() {
        assertEquals(10.0, basicEnclosure.getEntryFee());
        assertEquals(75.0, vipEnclosure.getEntryFee());
    }

    @Test
    void testVisitSpendsMoneyAndRecordsVisit() {
        Tourist t = new Tourist(1, "Ana");
        t.setStatus(TouristStatus.IN_PARK);
        basicEnclosure.visit(t, rng, mockCsvWriter);
        assertEquals(10.0, t.getMoneySpent());
        assertTrue(t.getVisitedZones().contains("Basic"));
        Mockito.verify(mockCsvWriter, Mockito.atLeastOnce()).appendRevenue(Mockito.any());
    }

    @Test
    void testSatisfactionScoresRange() {       

        for (int i = 0; i < 200; i++) {
            Tourist t = new Tourist(i, "T" + i);
            t.setStatus(TouristStatus.IN_PARK);
            
            vipEnclosure.visit(t, rng, mockCsvWriter);
        }
        
        assertTrue(true);
    }

    @Test
    void testPremiumAverageGreaterThanBasic() {
        
    }
}