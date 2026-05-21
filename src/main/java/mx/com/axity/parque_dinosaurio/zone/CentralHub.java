package mx.com.axity.parque_dinosaurio.zone;

import java.time.LocalDateTime;
import java.util.Random;

import mx.com.axity.parque_dinosaurio.model.Tourist;
import mx.com.axity.parque_dinosaurio.persistance.CsvWriter;
import mx.com.axity.parque_dinosaurio.persistance.RevenueRecord;

public class CentralHub implements ParkZone {
    
    private final String name = "CentralHub";
    private final double souvenirPrice;
    private final double purchaseProbability;
    private int currentVisitors = 0;
    private final int maxCapacity = Integer.MAX_VALUE; // sin límite práctico

    public CentralHub(double souvenirPrice, double purchaseProbability) {
        this.souvenirPrice = souvenirPrice;
        this.purchaseProbability = purchaseProbability;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public boolean hasCapacity() {
        return true;
    }

    @Override
    public int getCurrentOccupancy() {
        return currentVisitors;
    }

    @Override
    public int getMaxCapacity() {
        return maxCapacity;
    }

    @Override
    public void enter(Tourist tourist) {
        currentVisitors++;
    }

    @Override
    public void exit(Tourist tourist) {
        currentVisitors--;
    }

    public void visit(Tourist tourist, Random rng, CsvWriter csvWriter) {
        enter(tourist);
        tourist.recordVisit(name);

        if (rng.nextDouble() < purchaseProbability) {
            tourist.spend(souvenirPrice);
            RevenueRecord revenue = new RevenueRecord(
                    System.nanoTime(), // id temporal, en producción usar secuencia
                    "SOUVENIR",
                    souvenirPrice,
                    tourist.getId(),
                    name,
                    LocalDateTime.now());
            csvWriter.appendRevenue(revenue);
        }
        exit(tourist);
    }
}