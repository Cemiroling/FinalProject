package domain;

import lombok.Value;

@Value
public class ElevatorMovement {
    int startFloor;
    int endFloor;

    public ElevatorMovement(int startFloor, int endFloor) {
        this.startFloor = startFloor;
        this.endFloor = endFloor;
    }
}
