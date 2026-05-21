package mx.com.axity.parque_dinosaurio.zone;

import java.time.LocalDateTime;
import java.util.Random;

import mx.com.axity.parque_dinosaurio.model.SatisfactionSurvey;
import mx.com.axity.parque_dinosaurio.model.Tourist;
import mx.com.axity.parque_dinosaurio.persistance.CsvWriter;
import mx.com.axity.parque_dinosaurio.persistance.RevenueRecord;

public class ObservationEnclosure implements ParkZone {

    private final String name;
    private final ExperienceType type;
    private int currentVisitors;
    private final int maxVisitors;
    private final double entryFee;

    public ObservationEnclosure(String name, ExperienceType type) {
        this.name = name;
        this.type = type;
        this.maxVisitors = type.getMaxVisitors();
        this.entryFee = type.getEntryFee();
        this.currentVisitors = 0;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public boolean hasCapacity() {
        return currentVisitors < maxVisitors;
    }

    @Override
    public int getCurrentOccupancy() {
        return currentVisitors;
    }

    @Override
    public int getMaxCapacity() {
        return maxVisitors;
    }

    @Override
    public void enter(Tourist tourist) {
        if (hasCapacity()) {
            currentVisitors++;
        }
    }

    @Override
    public void exit(Tourist tourist) {
        if (currentVisitors > 0) {
            currentVisitors--;
        }
    }

    public void visit(Tourist tourist, Random rng, CsvWriter csvWriter) {
        if (!hasCapacity())
            return;

        enter(tourist);
        tourist.recordVisit(name);
        tourist.spend(entryFee);

        RevenueRecord revenue = new RevenueRecord(
                System.nanoTime(),
                "ENCLOSURE_ENTRY",
                entryFee,
                tourist.getId(),
                name,
                LocalDateTime.now());
        csvWriter.appendRevenue(revenue);

        /* Encuesta de satisfacción */
        int score = type.getMinScore() + rng.nextInt(type.getMaxScore() - type.getMinScore() + 1);
        SatisfactionSurvey survey = new SatisfactionSurvey(tourist.getId(), name, score);

        exit(tourist);
    }

    public double getEntryFee() {
        return entryFee;
    }
}