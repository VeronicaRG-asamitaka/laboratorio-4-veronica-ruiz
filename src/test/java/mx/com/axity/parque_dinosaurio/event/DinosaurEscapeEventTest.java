package mx.com.axity.parque_dinosaurio.event;

import org.junit.jupiter.api.*;
import org.mockito.Mockito;

import mx.com.axity.parque_dinosaurio.model.*;
import mx.com.axity.parque_dinosaurio.simulation.ParkState;

import java.util.List;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

class DinosaurEscapeEventTest {

    private DinosaurEscapeEvent event;
    private ParkState mockState;
    private Random rng;

    @BeforeEach
    void setUp() {
        event = new DinosaurEscapeEvent();
        rng = new Random(42);
        mockState = Mockito.mock(ParkState.class);
    }

    @Test
    void testGetName() {
        assertEquals("ESCAPE_DINOSAURIO", event.getName());
    }

    @Test
    void testExecuteEscapesOneDinosaur() {
        Dinosaur rex = new CarnivoreDinosaur(1, "Rex", "T-Rex");
        Dinosaur trike = new HerbivoreDinosaur(2, "Trike", "Triceratops");
        List<Dinosaur> dinosaurs = List.of(rex, trike);
        Mockito.when(mockState.getDinosaurs()).thenReturn(dinosaurs);
        Mockito.when(mockState.getActiveTourists()).thenReturn(List.of());

        event.execute(mockState, rng);

        long escapedCount = dinosaurs.stream().filter(d -> d.getStatus() == DinosaurStatus.ESCAPED).count();
        assertEquals(1, escapedCount);
    }

    @Test
    void testExecuteWhenNoDinosaurs() {
        Mockito.when(mockState.getDinosaurs()).thenReturn(List.of());
        assertDoesNotThrow(() -> event.execute(mockState, rng));
    }

    @Test
    void testToRecord() {
        var record = event.toRecord(5);
        assertEquals(5, record.step());
        assertEquals("ESCAPE_DINOSAURIO", record.eventName());
    }
}
