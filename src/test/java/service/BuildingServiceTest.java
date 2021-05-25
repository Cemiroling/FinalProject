package service;

import domain.Elevator;
import domain.Floor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

class BuildingServiceTest {

    @Test
    void testCreateFloors() {
        List<Floor> floors = BuildingService.createFloors(10, 1);
        assertThat(floors.size(), is(10));
    }

    @Test
    void testCreateFloorsWithWrongNumber() {
        Assertions.assertThrows(IllegalArgumentException.class,
                () -> BuildingService.createFloors(-1, 1));
    }

    @Test
    void testCreateFloorsWithWrongInterval() {
        Assertions.assertThrows(IllegalArgumentException.class,
                () -> BuildingService.createFloors(10, -1));
    }

    @Test
    void testCreateElevators() {
        List<Elevator> elevators = BuildingService.createElevators(10, 500, 10, 10);
        assertThat(elevators.size(), is(10));
    }

    @Test
    void testCreateElevatorsWithWrongNumber() {
        Assertions.assertThrows(IllegalArgumentException.class,
                () -> BuildingService.createElevators(-1, 500, 10, 10));
    }

    @Test
    void testCreateElevatorsWithWrongWeight() {
        Assertions.assertThrows(IllegalArgumentException.class,
                () -> BuildingService.createElevators(10, -1, 10, 10));
    }

    @Test
    void testCreateElevatorsWithWrongSpeed() {
        Assertions.assertThrows(IllegalArgumentException.class,
                () -> BuildingService.createElevators(10, 500, -1, 10));
    }

    @Test
    void testCreateElevatorsWithWrongDoorSpeed() {
        Assertions.assertThrows(IllegalArgumentException.class,
                () -> BuildingService.createElevators(10, 500, 10, -1));
    }
}