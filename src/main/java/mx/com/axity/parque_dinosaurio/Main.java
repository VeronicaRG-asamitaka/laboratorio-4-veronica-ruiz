package mx.com.axity.parque_dinosaurio;

import mx.com.axity.parque_dinosaurio.config.ParkConfig;
import mx.com.axity.parque_dinosaurio.simulation.SimulationEngine;

public class Main {
    public static void main(String[] args) {
        ParkConfig config = ParkConfig.getInstance();
        SimulationEngine engine = new SimulationEngine(config);
        engine.run();
    }
}