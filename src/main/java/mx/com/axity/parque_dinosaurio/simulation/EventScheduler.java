package mx.com.axity.parque_dinosaurio.simulation;

import java.util.*;

import mx.com.axity.parque_dinosaurio.event.*;

public class EventScheduler {
    
    private final Map<Integer, SimulationEvent> scheduledEvents = new HashMap<>();

    public EventScheduler(long seed, int totalSteps) {

        Random rng = new Random(seed);
        List<SimulationEvent> possibleEvents = List.of(
                new DinosaurEscapeEvent(),
                new BlackoutEvent(),
                new StormEvent());

        // Asignar un step aleatorio a cada evento
        for (SimulationEvent event : possibleEvents) {
            int step = rng.nextInt(totalSteps - 1) + 1; // nunca en step 0
            scheduledEvents.put(step, event);
        }
    }

    public Optional<SimulationEvent> checkForEvent(int step) {
        return Optional.ofNullable(scheduledEvents.get(step));
    }
}