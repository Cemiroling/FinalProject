package data;

import domain.Floor;
import domain.Passenger;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class TestSamples {
    public static List<Floor> getFloorList(int floors, float interval) {
        return IntStream.range(0, floors)
                .mapToObj(i -> new Floor(i, interval))
                .collect(Collectors.toList());
    }

    public static Passenger getMovingUpPassenger(int floor) {
        return new Passenger(100, floor);
    }

    public static Passenger getMovingDownPassenger() {
        return new Passenger(100, 0);
    }
}
