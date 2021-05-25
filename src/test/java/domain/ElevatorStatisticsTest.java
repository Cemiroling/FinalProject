package domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

class ElevatorStatisticsTest {

    private ElevatorStatistics elevatorStatistics;

    @BeforeEach
    void before() {
        elevatorStatistics = new ElevatorStatistics();
    }

    @Test
    void testAddElevatorMovementNew() {
        ElevatorMovement movement = new ElevatorMovement(0, 10);
        elevatorStatistics.addElevatorMovement(movement);
        assertThat(elevatorStatistics.getElevatorMovements().get(movement), is(1));
    }

    @Test
    void testAddElevatorMovementAlreadyExists() {
        ElevatorMovement movement = new ElevatorMovement(0, 10);
        elevatorStatistics.addElevatorMovement(movement);
        elevatorStatistics.addElevatorMovement(movement);
        assertThat(elevatorStatistics.getElevatorMovements().get(movement), is(2));
    }
}