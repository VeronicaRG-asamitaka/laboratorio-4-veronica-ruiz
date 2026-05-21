package mx.com.axity.parque_dinosaurio.zone;

import mx.com.axity.parque_dinosaurio.model.Tourist;

public interface ParkZone {
    String getName();

    boolean hasCapacity();

    int getCurrentOccupancy();

    int getMaxCapacity();

    void enter(Tourist tourist);

    void exit(Tourist tourist);
}
