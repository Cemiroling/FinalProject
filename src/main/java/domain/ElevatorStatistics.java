package domain;

import lombok.Value;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Value
public class ElevatorStatistics {
    Map<ElevatorMovement, Integer> elevatorMovements;

    public ElevatorStatistics() {
        elevatorMovements = new HashMap<>();
    }

    public void addElevatorMovement(ElevatorMovement movement) {
        elevatorMovements.putIfAbsent(movement, 0);
        elevatorMovements.put(movement, elevatorMovements.get(movement) + 1);
    }

    public void printAllMovements() {
        elevatorMovements.forEach((key, value)
                -> log.info("Number of elevations from floor {} to floor {} is: {}", key.getStartFloor(), key.getEndFloor(), value));
    }

    public void printTotalMovements() {
        log.info("Total number of elevations: {}\n", getTotalNumberOfMovements());
    }

    public int getTotalNumberOfMovements() {
        return elevatorMovements.values().stream().reduce(0, Integer::sum);
    }
}
