package mx.com.axity.parque_dinosaurio.model;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

class DinosaurTest {

    @Test
    void testCarnivoreDangerLevel() {
        Dinosaur rex = new CarnivoreDinosaur(1, "Pochosaurio", "T-Rex");
        assertEquals(0.9, rex.getDangerLevel());
        assertEquals("CARNIVORE", rex.getDiet());
        assertEquals(DinosaurStatus.IN_ENCLOSURE, rex.getStatus());
    }

    @Test
    void testHerbivoreDangerLevel() {
        Dinosaur trike = new HerbivoreDinosaur(2, "Trike", "Triceratops");
        assertEquals(0.2, trike.getDangerLevel());
        assertEquals("HERBIVORE", trike.getDiet());
    }

    @Test
    void testEscapeAndRecapture() {
        Dinosaur dino = new CarnivoreDinosaur(3, "Spino", "Spinosaurus");
        dino.escape();
        assertEquals(DinosaurStatus.ESCAPED, dino.getStatus());
        dino.recapture();
        assertEquals(DinosaurStatus.RECAPTURED, dino.getStatus());
        dino.returnToEnclosure();
        assertEquals(DinosaurStatus.IN_ENCLOSURE, dino.getStatus());
    }
}
