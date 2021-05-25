package service;

import domain.Floor;
import lombok.Value;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.OptionalInt;

import static com.google.common.base.Preconditions.checkArgument;

@Value
public class FloorService {
    List<Floor> floors;

    public FloorService(List<Floor> floors) {
        this.floors = floors;
    }

    public Floor getFloor(int floorNumber) {
        checkArgument(floorNumber >= 0, "Floor number cannot be negative");
        checkArgument(floorNumber < floors.size(), "Floor number cannot be greater then top floor number");
        return floors.get(floorNumber);
    }

    public int getNearestFloorWithPressedButton(int currentFloor) {
        Optional<Floor> floor = floors.stream()
                .filter(Floor::isAnyButtonPressed)
                .min(Comparator.comparingInt(i -> Math.abs(i.getNumber() - currentFloor)));
        return floor.map(Floor::getNumber).orElse(0);
    }

    public boolean hasAnyPressedButton() {
        return floors.stream()
                .anyMatch(Floor::isAnyButtonPressed);
    }

    public boolean hasAnyPressedButtonUpAbove(int currentFloor) {
        return floors.stream()
                .filter(f -> f.getNumber() >= currentFloor)
                .anyMatch(Floor::isButtonUpPressed);
    }

    public OptionalInt getClosestPressedButtonUpAbove(int currentFloor) {
        return floors.stream()
                .filter(f -> f.getNumber() >= currentFloor)
                .filter(Floor::isButtonUpPressed)
                .mapToInt(Floor::getNumber)
                .min();
    }

    public boolean hasAnyPressedButtonDownBelow(int currentFloor) {
        return floors.stream()
                .filter(f -> f.getNumber() <= currentFloor)
                .anyMatch(Floor::isButtonDownPressed);
    }

    public OptionalInt getClosestPressedButtonDownBelow(int currentFloor) {
        return floors.stream()
                .filter(f -> f.getNumber() <= currentFloor)
                .filter(Floor::isButtonDownPressed)
                .mapToInt(Floor::getNumber)
                .max();
    }

    public OptionalInt getLowestPressedButtonUp() {
        return floors.stream()
                .filter(Floor::isButtonUpPressed)
                .mapToInt(Floor::getNumber)
                .min();
    }

    public OptionalInt getHighestPressedButtonDown() {
        return floors.stream()
                .filter(Floor::isButtonDownPressed)
                .mapToInt(Floor::getNumber)
                .max();
    }
}
