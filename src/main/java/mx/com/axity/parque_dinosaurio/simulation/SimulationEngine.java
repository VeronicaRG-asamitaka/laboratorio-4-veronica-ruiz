package mx.com.axity.parque_dinosaurio.simulation;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.*;

import mx.com.axity.parque_dinosaurio.config.ParkConfig;
import mx.com.axity.parque_dinosaurio.model.*;
import mx.com.axity.parque_dinosaurio.monitoring.ParkMonitor;
import mx.com.axity.parque_dinosaurio.persistance.CsvWriter;
import mx.com.axity.parque_dinosaurio.persistance.ExpenseRecord;
import mx.com.axity.parque_dinosaurio.zone.*;

public class SimulationEngine {
    private final ParkConfig config;
    private final Random rng;
    private final CsvWriter csvWriter;
    private ParkState state;

    public SimulationEngine(ParkConfig config) {
        this.config = config;
        long seed = config.getSeed();
        this.rng = new Random(seed);
        try {
            this.csvWriter = new CsvWriter(config.getOutputDirectory());
        } catch (IOException e) {
            throw new RuntimeException("Failed to initialize CSV writer", e);
        }
    }

    public void run() {
        initializePark();
        int totalSteps = config.getTotalSteps();
        int batchSize = config.getInt("simulation.arrivalBatchSize", 5);
        EventScheduler scheduler = new EventScheduler(config.getSeed(), totalSteps);

        for (int step = 0; step < totalSteps; step++) {
            state.incrementStep();

            // A. Llegadas
            List<Tourist> arrived = state.getArrivalZone().processBatch(batchSize, csvWriter);
            for (Tourist t : arrived) {
                state.addRevenue(config.getDouble("arrival.ticketPrice", 25.0));
            }

            // B. Movimiento de turistas
            List<Tourist> activeTourists = state.getActiveTourists();
            for (Tourist t : activeTourists) {
                state.getCentralHub().visit(t, rng, csvWriter);
                state.getBathroomZone().tryEnter(t, rng, csvWriter);
                if (!state.getEnclosures().isEmpty()) {
                    ObservationEnclosure enc = state.getEnclosures().get(rng.nextInt(state.getEnclosures().size()));
                    enc.visit(t, rng, csvWriter);
                    state.addRevenue(enc.getEntryFee());
                }
            }

            // C. Ticks de zonas
            state.getBathroomZone().tick();
            state.getPowerPlant().tick(rng, csvWriter);

            // D. Evento — la variable local final 'currentStep' soluciona el error
            final int currentStep = step;
            scheduler.checkForEvent(currentStep).ifPresent(event -> {
                event.execute(state, rng);
                csvWriter.appendEvent(event.toRecord(currentStep));
            });

            // E. Trabajadores
            List<Worker> workers = state.getWorkers();
            for (Worker w : workers) {
                if (w instanceof Guard) {
                    ((Guard) w).recaptureEscapedDinosaurs(state.getDinosaurs());
                } else if (w instanceof Technician) {
                    ((Technician) w).repairIfNeeded(state.getPowerPlant());
                }
                ExpenseRecord salaryExpense = new ExpenseRecord(
                        System.nanoTime(),
                        "SALARY",
                        w.getDailySalary(),
                        "Salary for " + w.getRole() + " " + w.getName(),
                        LocalDateTime.now());
                csvWriter.appendExpense(salaryExpense);
                state.addExpense(w.getDailySalary());
            }

            // F. Monitor
            ParkMonitor.displaySnapshot(state);
        }

        printFinalSummary();
    }

    private void initializePark() {
        // Crear turistas
        int touristCount = config.getInt("tourists", 50);
        List<Tourist> tourists = new ArrayList<>();
        for (int i = 1; i <= touristCount; i++) {
            tourists.add(new Tourist(i, "Tourist-" + i));
        }

        // Crear dinosaurios
        List<Dinosaur> dinosaurs = new ArrayList<>();
        int carnivores = config.getInt("dinosaurs.carnivores", 5);
        int herbivores = config.getInt("dinosaurs.herbivores", 15);
        int dinoId = 1;
        for (int i = 0; i < carnivores; i++) {
            dinosaurs.add(new CarnivoreDinosaur(dinoId++, "Carno-" + i, "T-Rex"));
        }
        for (int i = 0; i < herbivores; i++) {
            dinosaurs.add(new HerbivoreDinosaur(dinoId++, "Herbi-" + i, "Triceratops"));
        }

        // Crear trabajadores
        List<Worker> workers = new ArrayList<>();
        int guardsCount = config.getInt("workers.guards", 3);
        int techsCount = config.getInt("workers.technicians", 2);
        double dailySalary = config.getDouble("workers.dailySalary", 150.0);
        for (int i = 0; i < guardsCount; i++) {
            workers.add(new Guard(i + 1, "Guard-" + (i + 1), dailySalary));
        }
        for (int i = 0; i < techsCount; i++) {
            workers.add(new Technician(i + 1 + guardsCount, "Tech-" + (i + 1), dailySalary));
        }

        // Zonas
        ArrivalZone arrivalZone = new ArrivalZone(
                config.getInt("arrival.maxCapacity", 30),
                config.getDouble("arrival.ticketPrice", 25.0));
        CentralHub centralHub = new CentralHub(
                config.getDouble("hub.souvenirPrice", 15.0),
                config.getDouble("hub.souvenirPurchaseProbability", 0.4));
        BathroomZone bathroomZone = new BathroomZone(
                config.getInt("bathroom.maxCapacity", 10),
                config.getInt("bathroom.useDurationSteps", 3),
                config.getDouble("bathroom.spaPrice", 20.0),
                config.getDouble("bathroom.spaPurchaseProbability", 0.2));
        PowerPlant powerPlant = new PowerPlant(
                config.getDouble("powerplant.initialEnergy", 100.0),
                config.getDouble("powerplant.consumptionPerStep", 1.5),
                config.getDouble("powerplant.failureProbability", 0.05),
                config.getDouble("powerplant.maintenanceCost", 200.0),
                config.getDouble("powerplant.repairCost", 500.0));

        List<ObservationEnclosure> enclosures = new ArrayList<>();
        enclosures.add(new ObservationEnclosure("Basic Enclosure", ExperienceType.BASIC));
        enclosures.add(new ObservationEnclosure("Premium Enclosure", ExperienceType.PREMIUM));
        enclosures.add(new ObservationEnclosure("VIP Enclosure", ExperienceType.VIP));

        // Estado
        state = new ParkState(tourists, dinosaurs, workers, arrivalZone, centralHub,
                bathroomZone, powerPlant, enclosures, csvWriter, rng);
    }

    private void printFinalSummary() {
        System.out.println("\n===== SIMULATION FINISHED =====");
        System.out.printf("Total steps: %d\n", config.getTotalSteps());
        System.out.printf("Total revenue: $%.2f\n", state.getTotalRevenue());
        System.out.printf("Total expenses: $%.2f\n", state.getTotalExpenses());
        System.out.printf("Net profit: $%.2f\n", state.getTotalRevenue() - state.getTotalExpenses());
        System.out.println("Active tourists at end: " + state.getActiveTourists().size());
        System.out.println("Dinosaurs in enclosure: " +
                state.getDinosaurs().stream().filter(d -> d.getStatus() == DinosaurStatus.IN_ENCLOSURE).count());
        System.out.println("Output files written to: " + config.getOutputDirectory());
    }
}