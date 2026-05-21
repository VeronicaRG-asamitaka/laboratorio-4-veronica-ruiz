package mx.com.axity.parque_dinosaurio.zone;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import mx.com.axity.parque_dinosaurio.model.Tourist;
import mx.com.axity.parque_dinosaurio.model.TouristStatus;
import mx.com.axity.parque_dinosaurio.persistance.CsvWriter;
import mx.com.axity.parque_dinosaurio.persistance.RevenueRecord;

public class ArrivalZone implements ParkZone {

    private final String name = "Arrival";
    private final int maxCapacity;
    private final double ticketPrice;
    private final Queue<Tourist> waitingQueue = new LinkedList<>();
    private long nextTicketId = 1;

    public ArrivalZone(int maxCapacity, double ticketPrice) {
        this.maxCapacity = maxCapacity;
        this.ticketPrice = ticketPrice;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public boolean hasCapacity() {
        return waitingQueue.size() < maxCapacity;
    }

    @Override
    public int getCurrentOccupancy() {
        return waitingQueue.size();
    }

    @Override
    public int getMaxCapacity() {
        return maxCapacity;
    }

    @Override
    public void enter(Tourist tourist) {
        if (hasCapacity() && tourist.getStatus() == TouristStatus.WAITING) {
            waitingQueue.offer(tourist);
        }
    }

    @Override
    public void exit(Tourist tourist) {
        waitingQueue.remove(tourist);
    }


    public List<Tourist> processBatch(int batchSize, CsvWriter csvWriter) {
        List<Tourist> arrived = new ArrayList<>();
        int toProcess = Math.min(batchSize, waitingQueue.size());
        for (int i = 0; i < toProcess; i++) {
            Tourist t = waitingQueue.poll();
            if (t != null) {
                t.setStatus(TouristStatus.IN_PARK);
                t.spend(ticketPrice);
                arrived.add(t);
               
                RevenueRecord revenue = new RevenueRecord(
                        nextTicketId++,
                        "TICKET",
                        ticketPrice,
                        t.getId(),
                        name,
                        LocalDateTime.now());
                csvWriter.appendRevenue(revenue);
            }
        }
        return arrived;
    }
}
