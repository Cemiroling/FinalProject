package service;

import data.TestSamples;
import domain.Floor;
import domain.Passenger;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.OptionalInt;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

class FloorServiceTest {

    private FloorService floorService;
    private List<Floor> floors;

    @BeforeEach
    void before() {
        floors = TestSamples.getFloorList(4, 0);
    }

    @Test
    void testGetFloor() {
        floorService = new FloorService(floors);
        Floor floor = floorService.getFloor(2);

        assertThat(floor.getNumber(), is(2));
    }

    @Test
    void testGetFloorWithNegativeNumber() {
        floorService = new FloorService(floors);

        Assertions.assertThrows(IllegalArgumentException.class,
                () -> floorService.getFloor(-1));
    }

    @Test
    void testGetFloorWithGreaterThenMaximumNumber() {
        floorService = new FloorService(floors);

        Assertions.assertThrows(IllegalArgumentException.class,
                () -> floorService.getFloor(5));
    }

    @Test
    void getNearestFloorWithPressedButton() {
        floors.get(0).addPassenger(TestSamples.getMovingUpPassenger(3));
        floors.get(3).addPassenger(TestSamples.getMovingDownPassenger());
        floorService = new FloorService(floors);

        assertThat(floorService.getNearestFloorWithPressedButton(2), is(3));
    }

    @Test
    void hasAnyPressedButtonTrue() {
        floors.get(0).addPassenger(TestSamples.getMovingUpPassenger(3));
        floorService = new FloorService(floors);

        assertThat(floorService.hasAnyPressedButton(), is(true));
    }

    @Test
    void hasAnyPressedButtonFalse() {
        floorService = new FloorService(floors);

        assertThat(floorService.hasAnyPressedButton(), is(false));
    }

    @Test
    void hasAnyPressedButtonUpAboveTrue() {
        floors.get(0).addPassenger(TestSamples.getMovingUpPassenger(3));
        floorService = new FloorService(floors);

        assertThat(floorService.hasAnyPressedButtonUpAbove(0), is(true));
    }

    @Test
    void hasAnyPressedButtonUpAboveFalse() {
        floorService = new FloorService(floors);

        assertThat(floorService.hasAnyPressedButtonUpAbove(0), is(false));
    }

    @Test
    void getClosestPressedButtonUpAbove() {
        floors.get(1).addPassenger(TestSamples.getMovingUpPassenger(3));
        floors.get(2).addPassenger(TestSamples.getMovingUpPassenger(3));
        floorService = new FloorService(floors);

        assertThat(floorService.getClosestPressedButtonUpAbove(0).getAsInt(), is(1));
    }

    @Test
    void getClosestPressedButtonUpAboveEmpty() {
        floorService = new FloorService(floors);

        assertThat(floorService.getClosestPressedButtonUpAbove(0), is(OptionalInt.empty()));
    }

    @Test
    void hasAnyPressedButtonDownBelowTrue() {
        floors.get(1).addPassenger(TestSamples.getMovingDownPassenger());
        floorService = new FloorService(floors);

        assertThat(floorService.hasAnyPressedButtonDownBelow(3), is(true));
    }

    @Test
    void hasAnyPressedButtonDownBelowFalse() {
        floorService = new FloorService(floors);

        assertThat(floorService.hasAnyPressedButtonDownBelow(3), is(false));
    }

    @Test
    void getClosestPressedButtonDownAbove() {
        floors.get(0).addPassenger(TestSamples.getMovingDownPassenger());
        floors.get(1).addPassenger(TestSamples.getMovingDownPassenger());
        floorService = new FloorService(floors);

        assertThat(floorService.getClosestPressedButtonDownBelow(3).getAsInt(), is(1));
    }

    @Test
    void getClosestPressedButtonDownAboveEmpty() {
        floorService = new FloorService(floors);
        assertThat(floorService.getClosestPressedButtonDownBelow(3), is(OptionalInt.empty()));
    }

    @Test
    void getLowestPressedButtonUp() {
        floors.get(0).addPassenger(TestSamples.getMovingUpPassenger(3));
        floors.get(2).addPassenger(TestSamples.getMovingUpPassenger(3));
        floorService = new FloorService(floors);

        assertThat(floorService.getLowestPressedButtonUp().getAsInt(), is(0));
    }

    @Test
    void getLowestPressedButtonUpEmpty() {
        floorService = new FloorService(floors);

        assertThat(floorService.getLowestPressedButtonUp(), is(OptionalInt.empty()));
    }

    @Test
    void getHighestPressedButtonDown() {
        floors.get(3).addPassenger(TestSamples.getMovingDownPassenger());
        floors.get(1).addPassenger(TestSamples.getMovingDownPassenger());
        floorService = new FloorService(floors);

        assertThat(floorService.getHighestPressedButtonDown().getAsInt(), is(3));
    }

    @Test
    void getHighestPressedButtonDownEmpty() {
        floorService = new FloorService(floors);

        assertThat(floorService.getHighestPressedButtonDown(), is(OptionalInt.empty()));
    }
}