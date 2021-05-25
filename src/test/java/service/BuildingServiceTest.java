package service;

import domain.Elevator;
import domain.Floor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

class BuildingServiceTest {

    private BuildingService buildingService;

    @BeforeEach
    void before() {
        buildingService = new BuildingService();
    }

    @Test
    void testCreateFloors() {
        List<Floor> floors = buildingService.createFloors(10, 1);
        assertThat(floors.size(), is(10));
    }

    @Test
    void testCreateFloorsWithWrongNumber() {
        Assertions.assertThrows(IllegalArgumentException.class,
                () -> buildingService.createFloors(-1, 1));
    }

    @Test
    void testCreateFloorsWithWrongInterval() {
        Assertions.assertThrows(IllegalArgumentException.class,
                () -> buildingService.createFloors(10, -1));
    }

    @Test
    void testCreateElevators() {
        List<Elevator> elevators = buildingService.createElevators(10, 500, 10, 10);
        assertThat(elevators.size(), is(10));
    }

    @Test
    void testCreateElevatorsWithWrongNumber() {
        Assertions.assertThrows(IllegalArgumentException.class,
                () -> buildingService.createElevators(-1, 500, 10, 10));
    }

    @Test
    void testCreateElevatorsWithWrongWeight() {
        Assertions.assertThrows(IllegalArgumentException.class,
                () -> buildingService.createElevators(10, -1, 10, 10));
    }

    @Test
    void testCreateElevatorsWithWrongSpeed() {
        Assertions.assertThrows(IllegalArgumentException.class,
                () -> buildingService.createElevators(10, 500, -1, 10));
    }

    @Test
    void testCreateElevatorsWithWrongDoorSpeed() {
        Assertions.assertThrows(IllegalArgumentException.class,
                () -> buildingService.createElevators(10, 500, 10, -1));
    }
}