package service;

import domain.ElevatorMovement;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static com.google.common.base.Preconditions.checkArgument;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

class StatisticsServiceTest {

    private StatisticsService statisticsService;

    @BeforeEach
    void before() {
        statisticsService = new StatisticsService(2, 2);
    }

    @Test
    void testIncreaseNumberOfGeneratedPassengers() {
        String floorName = "Floor 0";
        assertThat(statisticsService.getFloorStatistics().get(floorName), is(0));
        statisticsService.increaseNumberOfGeneratedPassengers(floorName);
        assertThat(statisticsService.getFloorStatistics().get(floorName), is(1));
    }

    @Test
    void testIncreaseNumberOfGeneratedPassengersWithWrongName() {
        String floorName = "Floor-0";
        Assertions.assertThrows(IllegalArgumentException.class,
                () -> statisticsService.increaseNumberOfGeneratedPassengers(floorName));
    }

    @Test
    void testAddElevatorMovementNew() {
        String elevatorName = "Elevator 0";
        ElevatorMovement elevatorMovement = new ElevatorMovement(0, 10);
        statisticsService.addElevatorMovement(elevatorName, elevatorMovement);
        assertThat(statisticsService.getElevatorStatistics().get(elevatorName).getElevatorMovements().get(elevatorMovement), is(1));
    }

    @Test
    void testAddElevatorMovementAlreadyExists() {
        String elevatorName = "Elevator 0";
        ElevatorMovement elevatorMovement = new ElevatorMovement(0, 10);
        statisticsService.addElevatorMovement(elevatorName, elevatorMovement);
        statisticsService.addElevatorMovement(elevatorName, elevatorMovement);
        assertThat(statisticsService.getElevatorStatistics().get(elevatorName).getElevatorMovements().get(elevatorMovement), is(2));
    }

    @Test
    void testAddElevatorMovementWithWrongName() {
        String elevatorName = "Elevator-0";
        ElevatorMovement elevatorMovement = new ElevatorMovement(0, 10);
        Assertions.assertThrows(IllegalArgumentException.class,
                () -> statisticsService.addElevatorMovement(elevatorName, elevatorMovement));
    }
}