package service;

import domain.ElevatorMovement;
import domain.ElevatorStatistics;
import lombok.Value;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.IntStream;

import static com.google.common.base.Preconditions.checkArgument;

@Slf4j
@Value
public class StatisticsService {
    Map<String, ElevatorStatistics> elevatorStatistics;
    Map<String, Integer> floorStatistics;

    public StatisticsService(int numberOfFloors, int numberOfElevators) {
        elevatorStatistics = new HashMap<>();
        floorStatistics = new HashMap<>();

        IntStream.range(0, numberOfElevators).forEach(i -> elevatorStatistics.put("Elevator " + i, new ElevatorStatistics()));
        IntStream.range(0, numberOfFloors).forEach(i -> floorStatistics.put("Floor " + i, 0));
    }

    public void increaseNumberOfGeneratedPassengers(String floorName) {
        checkArgument(floorStatistics.containsKey(floorName), "Incorrect floor name");
        floorStatistics.put(floorName, floorStatistics.get(floorName) + 1);
        log.info("Number of generated passengers on {} : {}", floorName, floorStatistics.get(floorName).toString());
    }

    public void addElevatorMovement(String elevatorName, ElevatorMovement elevatorMovement) {
        checkArgument(elevatorStatistics.containsKey(elevatorName), "Incorrect elevator name");
        elevatorStatistics.get(elevatorName).addElevatorMovement(elevatorMovement);
    }

    public void printStatistics() {
        elevatorStatistics.forEach((key, value) -> {
            log.info("{}", key);
            value.printAllMovements();
            value.printTotalMovements();
        });
        floorStatistics.forEach((key, value) -> log.info("Total number of generated passengers on floor {} is: {}", key, value));
    }
}