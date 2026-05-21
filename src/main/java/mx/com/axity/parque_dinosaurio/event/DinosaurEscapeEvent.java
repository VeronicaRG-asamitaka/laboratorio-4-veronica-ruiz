package mx.com.axity.parque_dinosaurio.event;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

import mx.com.axity.parque_dinosaurio.model.*;
import mx.com.axity.parque_dinosaurio.persistance.EventRecord;
import mx.com.axity.parque_dinosaurio.simulation.ParkState;

public class DinosaurEscapeEvent implements SimulationEvent {

    private static final String NAME = "ESCAPE_DINOSAURIO";
    private static final String DESCRIPTION = "Un dinosaurio escapa de su encierro y puede atacar a turistas";

    @Override
    public String getName() {
        return NAME;
    }

    @Override
    public String getDescription() {
        return DESCRIPTION;
    }

    @Override
    public void execute(ParkState state, Random rng) {
        
        // 1. Elegir un dinosaurio que esté actualmente en encierro 

        List<Dinosaur> enclosuredDinosaurs = state.getDinosaurs().stream()
                .filter(d -> d.getStatus() == DinosaurStatus.IN_ENCLOSURE)
                .collect(Collectors.toList());

        if (enclosuredDinosaurs.isEmpty()) {
            return;
        }

        Dinosaur escaped = enclosuredDinosaurs.get(rng.nextInt(enclosuredDinosaurs.size()));
        escaped.escape();

        // 2. Posible ataque a un turista según nivel de peligro
        List<Tourist> activeTourists = state.getActiveTourists(); // IN_PARK
        if (!activeTourists.isEmpty() && rng.nextDouble() < escaped.getDangerLevel()) {
            Tourist victim = activeTourists.get(rng.nextInt(activeTourists.size()));
            victim.setStatus(TouristStatus.ATTACKED);
            // Registrar en el estado que el turista fue atacado (para monitorización)
            state.recordAttack(victim, escaped);
        }

        // 3. Se registra el evento (se añade al CSV más adelante con toRecord)
        // Nota: El Engine llamará a toRecord y luego a csvWriter.appendEvent
    }

    @Override
    public EventRecord toRecord(long step) {
        return new EventRecord(
                step,
                NAME,
                DESCRIPTION,
                "Dinosaurio escapado",
                LocalDateTime.now());
    }
}