package mx.com.axity.parque_dinosaurio.monitoring;

import mx.com.axity.parque_dinosaurio.model.DinosaurStatus;
import mx.com.axity.parque_dinosaurio.simulation.ParkState;

public class ParkMonitor {
    public static void displaySnapshot(ParkState state) {

        System.out.println("=== STEP " + state.getCurrentStep() + " ===");
        System.out.printf("Active tourists: %d\n", state.getActiveTourists().size());
        long inEnclosure = state.getDinosaurs().stream()
                .filter(d -> d.getStatus() == DinosaurStatus.IN_ENCLOSURE).count();
        System.out.printf("Dinosaurs in enclosure: %d\n", inEnclosure);
        System.out.printf("Power plant energy: %.1f / %.1f (operational: %s)\n",
                state.getPowerPlant().getEnergy(),
                state.getPowerPlant().getInitialEnergy(),
                state.getPowerPlant().isOperational());
        System.out.printf("Revenue: $%.2f | Expenses: $%.2f | Profit: $%.2f\n",
                state.getTotalRevenue(), state.getTotalExpenses(),
                state.getTotalRevenue() - state.getTotalExpenses());
        System.out.println();
    }
}