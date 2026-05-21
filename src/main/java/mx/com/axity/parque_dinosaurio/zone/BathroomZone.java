package mx.com.axity.parque_dinosaurio.zone;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import mx.com.axity.parque_dinosaurio.model.Tourist;
import mx.com.axity.parque_dinosaurio.persistance.CsvWriter;
import mx.com.axity.parque_dinosaurio.persistance.RevenueRecord;

public class BathroomZone implements ParkZone {

    private final String name = "Bathroom";
    private final int maxCapacity;
    private final int useDurationSteps;
    private final double spaPrice;
    private final double spaProbability;
    private final Map<Tourist, Integer> occupationRemaining = new HashMap<>();

    public BathroomZone(int maxCapacity, int useDurationSteps, double spaPrice, double spaProbability) {
        this.maxCapacity = maxCapacity;
        this.useDurationSteps = useDurationSteps;
        this.spaPrice = spaPrice;
        this.spaProbability = spaProbability;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public boolean hasCapacity() {
        return occupationRemaining.size() < maxCapacity;
    }

    @Override
    public int getCurrentOccupancy() {
        return occupationRemaining.size();
    }

    @Override
    public int getMaxCapacity() {
        return maxCapacity;
    }

    @Override
    public void enter(Tourist tourist) {

    }

    @Override
    public void exit(Tourist tourist) {
        occupationRemaining.remove(tourist);
    }

    public boolean tryEnter(Tourist tourist, Random rng, CsvWriter csvWriter) {
        if (!hasCapacity())
            return false;

        boolean buysSpa = rng.nextDouble() < spaProbability;
        if (buysSpa) {
            tourist.spend(spaPrice);
            RevenueRecord revenue = new RevenueRecord(
                    System.nanoTime(),
                    "SPA",
                    spaPrice,
                    tourist.getId(),
                    name,
                    LocalDateTime.now());
            csvWriter.appendRevenue(revenue);
        }

        occupationRemaining.put(tourist, useDurationSteps);
        tourist.recordVisit(name);
        return true;
    }

    public void tick() {
        occupationRemaining.entrySet().removeIf(entry -> {
            int remaining = entry.getValue() - 1;
            if (remaining <= 0) {
                return true; 
            } else {
                entry.setValue(remaining);
                return false;
            }
        });
    }
}
