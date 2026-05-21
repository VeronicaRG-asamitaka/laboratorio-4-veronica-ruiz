package mx.com.axity.parque_dinosaurio.event;

import java.util.Random;

import mx.com.axity.parque_dinosaurio.persistance.EventRecord;
import mx.com.axity.parque_dinosaurio.simulation.ParkState;

public interface SimulationEvent {

    String getName();

    String getDescription();

    void execute(ParkState state, Random rng);

    EventRecord toRecord(long step);
}