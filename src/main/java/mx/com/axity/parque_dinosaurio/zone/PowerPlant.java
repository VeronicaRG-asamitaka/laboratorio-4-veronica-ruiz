package mx.com.axity.parque_dinosaurio.zone;

import java.time.LocalDateTime;
import java.util.Random;

import mx.com.axity.parque_dinosaurio.persistance.CsvWriter;
import mx.com.axity.parque_dinosaurio.persistance.EventRecord;

public class PowerPlant {

    private final String name = "PowerPlant";
    private double energy;
    private final double initialEnergy;
    private final double consumptionPerStep;
    private final double failureProbability;
    private final double maintenanceCost;
    private final double repairCost;
    private boolean operational;

    public PowerPlant(double initialEnergy, double consumptionPerStep, double failureProbability,
            double maintenanceCost, double repairCost) {
        this.initialEnergy = initialEnergy;
        this.energy = initialEnergy;
        this.consumptionPerStep = consumptionPerStep;
        this.failureProbability = failureProbability;
        this.maintenanceCost = maintenanceCost;
        this.repairCost = repairCost;
        this.operational = true;
    }

    public String getName() {
        return name;
    }

    public double getEnergy() {
        return energy;
    }

    public boolean isOperational() {
        return operational;
    }

    public void tick(Random rng, CsvWriter csvWriter) {
        if (!operational)
            return;

        energy -= consumptionPerStep;
        if (energy <= 0) {
            energy = 0;
            triggerFailure(csvWriter);
            return;
        }

        if (rng.nextDouble() < failureProbability) {
            triggerFailure(csvWriter);
        }
    }

    public void triggerFailure(CsvWriter csvWriter) {
        operational = false;
        energy = 0;
        EventRecord event = new EventRecord(
                System.currentTimeMillis(),
                "POWER_FAILURE",
                "Planta eléctrica ha fallado",
                name,
                LocalDateTime.now());
        csvWriter.appendEvent(event);
    }

    public void repair() {
        if (!operational) {
            operational = true;
            energy = initialEnergy;
        }
    }

    public double getMaintenanceCost() {
        return maintenanceCost;
    }

    public double getRepairCost() {
        return repairCost;
    }

    public double getInitialEnergy() {
        return initialEnergy;
    }
}