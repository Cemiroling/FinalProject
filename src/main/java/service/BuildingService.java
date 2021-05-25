package service;

import domain.Elevator;
import domain.Floor;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static com.google.common.base.Preconditions.checkArgument;

public class BuildingService {
    public static List<Floor> createFloors(int numberOfFloors, int generationInterval) {
        checkArgument(numberOfFloors > 0, "Number of floors should be greater than 0");
        checkArgument(generationInterval > 0, "Generation interval should be greater than 0");

        return IntStream.range(0, numberOfFloors)
                .mapToObj(i -> new Floor(i, generationInterval))
                .collect(Collectors.toList());
    }

    public static List<Elevator> createElevators(int numberOfElevators, int maxWeight, float speed, float doorSpeed) {
        checkArgument(numberOfElevators > 0, "Number of elevators should be greater than 0");
        checkArgument(maxWeight > 0, "Maximum weight should be greater than 0");
        checkArgument(speed > 0, "Elevator speed should be greater than 0");
        checkArgument(doorSpeed > 0, "Door speed should be greater than 0");

        return IntStream.range(0, numberOfElevators)
                .mapToObj(i -> new Elevator(i, maxWeight, speed, doorSpeed))
                .collect(Collectors.toList());
    }
}
