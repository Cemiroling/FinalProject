package domain;

import lombok.Setter;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import service.FloorService;
import service.StatisticsService;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

import static domain.ElevatorState.*;

@Slf4j
public class Elevator extends Thread {

    private final int maxWeight;
    private final float speed;
    private final float doorSpeed;
    private ElevatorState state;
    private final List<Passenger> passengers;
    private int currentFloor;
    private int destinationFloor;

    private boolean isDoorOpen;
    private boolean isStop;
    @Setter
    private FloorService floorService;
    @Setter
    private StatisticsService statisticsService;

    public Elevator(int number, int maxWeight, float speed, float doorSpeed) {
        this.maxWeight = maxWeight;
        this.speed = speed * 1000;
        this.doorSpeed = doorSpeed * 1000;
        this.setName("Elevator " + number);
        state = IDLE;
        isDoorOpen = true;
        passengers = new ArrayList<>();
    }

    @Override
    public void run() {
        while (!isStop) {
            if (state == IDLE) {
                if (floorService.hasAnyPressedButton()) {
                    destinationFloor = floorService.getNearestFloorWithPressedButton(currentFloor);
                    updateState();
                }
            } else {
                if (!passengers.isEmpty())
                    dropOffPassengers();
                if (hasPassengersWithSameDirection())
                    pickupPassengers();
                if (currentFloor != destinationFloor)
                    moveToNextFloor();
                else {
                    getNextDestination();
                }
            }
        }
    }

    private void getNextDestination() {
        if (floorService.hasAnyPressedButton()) {
            if (state == MOVING_UP) {
                if (floorService.hasAnyPressedButtonUpAbove(currentFloor))
                    floorService.getClosestPressedButtonUpAbove(currentFloor).ifPresent(e -> destinationFloor = e);
                else
                    floorService.getHighestPressedButtonDown().ifPresent(e -> destinationFloor = e);
            } else if (floorService.hasAnyPressedButtonDownBelow(currentFloor))
                floorService.getClosestPressedButtonDownBelow(currentFloor).ifPresent(e -> destinationFloor = e);
            else
                floorService.getLowestPressedButtonUp().ifPresent(e -> destinationFloor = e);
            updateState();
        } else state = IDLE;
    }

    @SneakyThrows
    private void openDoor() {
        TimeUnit.MILLISECONDS.sleep((long) doorSpeed);
        log.info("Door in {} opened", this.getName());
        isDoorOpen = true;
    }

    @SneakyThrows
    private void closeDoor() {
        TimeUnit.MILLISECONDS.sleep((long) doorSpeed);
        log.info("Door in {} closed", this.getName());
        isDoorOpen = false;
    }

    public void dropOffPassengers() {
        if (passengers.stream().anyMatch(p -> p.getDestinationFloor() == currentFloor))
            if (!isDoorOpen)
                openDoor();
        passengers.removeIf(p -> p.getDestinationFloor() == currentFloor);
        log.info("TotalWeight: {}", getTotalWeight());
    }

    private int getTotalWeight() {
        return passengers.stream().mapToInt(Passenger::getWeight).sum();
    }

    private void updateState() {
        if (destinationFloor < currentFloor)
            state = MOVING_DOWN;
        else if (destinationFloor > currentFloor)
            state = MOVING_UP;
        else {
            if (floorService.getFloor(currentFloor).isButtonUpPressed())
                state = MOVING_UP;
            else if (floorService.getFloor(currentFloor).isButtonDownPressed())
                state = MOVING_DOWN;
            else state = IDLE;
        }
    }

    @SneakyThrows
    private void moveToNextFloor() {
        if (isDoorOpen)
            closeDoor();
        if (state == MOVING_UP)
            currentFloor++;
        if (state == MOVING_DOWN)
            currentFloor--;
        TimeUnit.MILLISECONDS.sleep((long) speed);
        log.info("{} arrived at floor {}", this.getName(), currentFloor);
    }

    private boolean hasPassengersWithSameDirection() {
        return state == MOVING_UP && floorService.getFloor(currentFloor).isButtonUpPressed()
                || state == MOVING_DOWN && floorService.getFloor(currentFloor).isButtonDownPressed();
    }

    private void pickupPassengers() {
        if (!isDoorOpen)
            openDoor();

        List<Passenger> passengerList = floorService.getFloor(currentFloor).getPassengersFromQueue(maxWeight - getTotalWeight(), state);
        passengerList.removeIf(Objects::isNull);
        passengerList.forEach(p -> statisticsService.addElevatorMovement(this.getName(), new ElevatorMovement(currentFloor, p.getDestinationFloor())));

        passengers.addAll(passengerList);
        if (!passengers.isEmpty())
            checkDestinationFloor();
    }

    private void checkDestinationFloor() {
        if (state == MOVING_UP)
            passengers.stream()
                    .mapToInt(Passenger::getDestinationFloor)
                    .max()
                    .ifPresent(max -> destinationFloor = max);
        if (state == MOVING_DOWN)
            passengers.stream()
                    .mapToInt(Passenger::getDestinationFloor)
                    .min()
                    .ifPresent(min -> destinationFloor = min);
    }

    public void setStop() {
        isStop = true;
    }
}