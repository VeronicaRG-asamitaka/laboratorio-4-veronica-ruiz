package mx.com.axity.parque_dinosaurio.event;

import java.time.LocalDateTime;
import java.util.Random;

import mx.com.axity.parque_dinosaurio.persistance.EventRecord;
import mx.com.axity.parque_dinosaurio.persistance.ExpenseRecord;
import mx.com.axity.parque_dinosaurio.simulation.ParkState;

public class BlackoutEvent implements SimulationEvent {
    
    private static final String NAME = "APAGON_MASIVO";
    private static final String DESCRIPTION = "Un apagón masivo deja la planta eléctrica inoperativa";
    private static final double EXPENSE_AMOUNT = 2000.0;

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
        // Provocar fallo en la planta
        state.getPowerPlant().triggerFailure(state.getCsvWriter());

        // Registrar gasto adicional de 2000
        ExpenseRecord expense = new ExpenseRecord(
                System.nanoTime(),
                "BLACKOUT_DAMAGE",
                EXPENSE_AMOUNT,
                "Daños por apagón masivo",
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
                "Planta eléctrica",
                LocalDateTime.now());
    }
}
