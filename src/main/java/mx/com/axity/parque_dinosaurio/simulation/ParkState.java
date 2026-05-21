package mx.com.axity.parque_dinosaurio.simulation;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

import mx.com.axity.parque_dinosaurio.model.*;
import mx.com.axity.parque_dinosaurio.persistance.CsvWriter;
import mx.com.axity.parque_dinosaurio.zone.*;

public class ParkState {

    private final List<Tourist> tourists;
    private final List<Dinosaur> dinosaurs;
    private final List<Worker> workers;
    private final ArrivalZone arrivalZone;
    private final CentralHub centralHub;
    private final BathroomZone bathroomZone;
    private final PowerPlant powerPlant;
    private final List<ObservationEnclosure> enclosures;
    private final CsvWriter csvWriter;
    private final Random rng;
    private long currentStep = 0;
    private double totalRevenue = 0.0;
    private double totalExpenses = 0.0;
    private final List<String> attackLogs = new ArrayList<>();

    public ParkState(List<Tourist> tourists, List<Dinosaur> dinosaurs, List<Worker> workers,
            ArrivalZone arrivalZone, CentralHub centralHub, BathroomZone bathroomZone,
            PowerPlant powerPlant, List<ObservationEnclosure> enclosures,
            CsvWriter csvWriter, Random rng) {
                
        this.tourists = tourists;
        this.dinosaurs = dinosaurs;
        this.workers = workers;
        this.arrivalZone = arrivalZone;
        this.centralHub = centralHub;
        this.bathroomZone = bathroomZone;
        this.powerPlant = powerPlant;
        this.enclosures = enclosures;
        this.csvWriter = csvWriter;
        this.rng = rng;
    }

    public void incrementStep() {
        currentStep++;
    }

    public long getCurrentStep() {
        return currentStep;
    }

    public List<Tourist> getActiveTourists() {
        return tourists.stream()
                .filter(t -> t.getStatus() == TouristStatus.IN_PARK)
                .collect(Collectors.toList());
    }

    public void addRevenue(double amount) {
        totalRevenue += amount;
    }

    public void addExpense(double amount) {
        totalExpenses += amount;
    }

    public double getTotalRevenue() {
        return totalRevenue;
    }

    public double getTotalExpenses() {
        return totalExpenses;
    }

    public void recordAttack(Tourist tourist, Dinosaur dinosaur) {
        attackLogs.add(String.format("Step %d: %s attacked by %s", currentStep, tourist.getName(), dinosaur.getName()));
    }

    // Getters
    public List<Tourist> getTourists() {
        return tourists;
    }

    public List<Dinosaur> getDinosaurs() {
        return dinosaurs;
    }

    public List<Worker> getWorkers() {
        return workers;
    }

    public ArrivalZone getArrivalZone() {
        return arrivalZone;
    }

    public CentralHub getCentralHub() {
        return centralHub;
    }

    public BathroomZone getBathroomZone() {
        return bathroomZone;
    }

    public PowerPlant getPowerPlant() {
        return powerPlant;
    }

    public List<ObservationEnclosure> getEnclosures() {
        return enclosures;
    }

    public CsvWriter getCsvWriter() {
        return csvWriter;
    }

    public Random getRng() {
        return rng;
    }
}