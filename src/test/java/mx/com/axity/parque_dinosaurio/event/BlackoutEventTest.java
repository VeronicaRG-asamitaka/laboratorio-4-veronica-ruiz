package mx.com.axity.parque_dinosaurio.event;

import org.junit.jupiter.api.*;
import org.mockito.Mockito;

import mx.com.axity.parque_dinosaurio.persistance.CsvWriter;
import mx.com.axity.parque_dinosaurio.simulation.ParkState;
import mx.com.axity.parque_dinosaurio.zone.PowerPlant;

import static org.junit.jupiter.api.Assertions.*;

class BlackoutEventTest {

    private BlackoutEvent event;
    private ParkState mockState;
    private PowerPlant mockPlant;
    private CsvWriter mockCsvWriter;

    @BeforeEach
    void setUp() {
        event = new BlackoutEvent();
        mockState = Mockito.mock(ParkState.class);
        mockPlant = Mockito.mock(PowerPlant.class);
        mockCsvWriter = Mockito.mock(CsvWriter.class);
        Mockito.when(mockState.getPowerPlant()).thenReturn(mockPlant);
        Mockito.when(mockState.getCsvWriter()).thenReturn(mockCsvWriter);
    }

    @Test
    void testExecuteMakesPlantNotOperational() {
        event.execute(mockState, new java.util.Random());
        Mockito.verify(mockPlant).triggerFailure(mockCsvWriter);
        Mockito.verify(mockCsvWriter).appendExpense(Mockito.any());
        Mockito.verify(mockState).addExpense(2000.0);
    }

    @Test
    void testGetName() {
        assertEquals("APAGON_MASIVO", event.getName());
    }

    @Test
    void testToRecord() {
        var record = event.toRecord(10);
        assertEquals(10, record.step());
        assertEquals("APAGON_MASIVO", record.eventName());
    }
}