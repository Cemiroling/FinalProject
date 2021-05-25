package service;

import domain.Passenger;
import lombok.Value;
import lombok.extern.slf4j.Slf4j;

import java.util.Random;

import static com.google.common.base.Preconditions.checkArgument;

@Slf4j
@Value
public class PassengerService {
    Random random;
    int maxFloor;
    int minWeight;
    int maxWeight;

    public PassengerService(int maxFloor, int minWeight, int maxWeight) {
        this.maxFloor = maxFloor;
        this.maxWeight = maxWeight;
        this.minWeight = minWeight;
        random = new Random();
    }

    public Passenger generatePassenger(int currentFloor) {
        checkArgument(currentFloor >= 0, "Floor number must be positive");
        checkArgument(currentFloor < maxFloor, "Floor number cannot be greater than top floor");

        int randomFloor = random.nextInt(maxFloor);
        while (currentFloor == randomFloor) {
            randomFloor = random.nextInt(maxFloor);
        }
        Passenger passenger = new Passenger(random.nextInt(maxWeight - minWeight) + minWeight, randomFloor);
        log.info("Generated person at floor {} with weight {} and destination floor {}", currentFloor, passenger.getWeight(), passenger.getDestinationFloor());
        return passenger;
    }
}
