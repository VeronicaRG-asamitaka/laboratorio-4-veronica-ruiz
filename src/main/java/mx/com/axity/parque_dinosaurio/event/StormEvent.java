package mx.com.axity.parque_dinosaurio.event;

import java.time.LocalDateTime;
import java.util.Random;

import mx.com.axity.parque_dinosaurio.model.Tourist;
import mx.com.axity.parque_dinosaurio.persistance.EventRecord;
import mx.com.axity.parque_dinosaurio.persistance.ExpenseRecord;
import mx.com.axity.parque_dinosaurio.simulation.ParkState;

public class StormEvent implements SimulationEvent {

    private static final String NAME = "TORMENTA_TORRENCIAL";
    private static final String DESCRIPTION = "Una tormenta torrencial obliga a evacuar a los turistas";
    private static final double EXPENSE_AMOUNT = 500.0;

    @Override
    public String getName() {
        return NAME;
    }

    @Override
    public String getDescription() {
        return DESCRIPTION;
    }

    @Override
    public void execute(ParkState state, Random rng) {

        // Para cada turista en el parque (IN_PARK), registrar visita a "Evacuación"
        for (Tourist t : state.getActiveTourists()) {
            t.recordVisit("Evacuación");
            
        }

        // Registrar gasto de 500
        ExpenseRecord expense = new ExpenseRecord(
                System.nanoTime(),
                "STORM_DAMAGE",
                EXPENSE_AMOUNT,
                "Daños por tormenta torrencial",
                LocalDateTime.now());
        state.getCsvWriter().appendExpense(expense);
        state.addExpense(EXPENSE_AMOUNT);
    }

    @Override
    public EventRecord toRecord(long step) {
        return new EventRecord(
                step,
                NAME,
                DESCRIPTION,
                "Todo el parque",
                LocalDateTime.now());
    }
}