package domain;

import data.TestSamples;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import service.FloorService;
import service.PassengerService;
import service.StatisticsService;

import java.util.List;
import java.util.concurrent.TimeUnit;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;

class ElevatorTest {

    private Elevator elevator;
    private List<Floor> floors;
    private StatisticsService statisticsService;
    private FloorService floorService;

    @BeforeEach
    void before() {
        elevator = new Elevator(0, 100, .001f, .001f);
        floors = TestSamples.getFloorList(3, .001f);
        statisticsService = new StatisticsService(3, 1);
        floorService = new FloorService(floors);

        elevator.setFloorService(floorService);
        elevator.setStatisticsService(statisticsService);
    }

    @SneakyThrows
    @Test
    void testRunTakePersonOnElevatorFloor() {
        floors.get(0).addPassenger(TestSamples.getMovingUpPassenger(2));

        assertThat(floorService.hasAnyPressedButton(), is(true));

        elevator.start();
        TimeUnit.MILLISECONDS.sleep(100);
        elevator.setStop();

        assertThat(floorService.hasAnyPressedButton(), is(false));

        ElevatorMovement requiredMovement = new ElevatorMovement(0, 2);

        assertThat(statisticsService.getElevatorStatistics().get(elevator.getName()).getElevatorMovements().size(), is(equalTo(1)));
        assertThat(statisticsService.getElevatorStatistics().get(elevator.getName()).getElevatorMovements().containsKey(requiredMovement), is(true));
        assertThat(statisticsService.getElevatorStatistics().get(elevator.getName()).getElevatorMovements().get(requiredMovement), is(equalTo(1)));
    }

    @SneakyThrows
    @Test
    void testRunTakePersonNotOnElevatorFloor() {
        floors.get(2).addPassenger(TestSamples.getMovingDownPassenger());

        assertThat(floorService.hasAnyPressedButton(), is(true));

        elevator.start();
        TimeUnit.MILLISECONDS.sleep(100);
        elevator.setStop();

        assertThat(floorService.hasAnyPressedButton(), is(false));

        ElevatorMovement requiredMovement = new ElevatorMovement(2, 0);

        assertThat(statisticsService.getElevatorStatistics().get(elevator.getName()).getElevatorMovements().size(), is(equalTo(1)));
        assertThat(statisticsService.getElevatorStatistics().get(elevator.getName()).getElevatorMovements().containsKey(requiredMovement), is(true));
        assertThat(statisticsService.getElevatorStatistics().get(elevator.getName()).getElevatorMovements().get(requiredMovement), is(equalTo(1)));
    }

    @SneakyThrows
    @Test
    void testRunTakePersonsWithinCapacity() {
        floors.get(0).addPassenger(TestSamples.getMovingUpPassenger(2));
        floors.get(0).addPassenger(TestSamples.getMovingUpPassenger(2));

        assertThat(floorService.hasAnyPressedButton(), is(true));

        elevator.start();
        TimeUnit.MILLISECONDS.sleep(100);
        elevator.setStop();

        assertThat(floorService.hasAnyPressedButton(), is(false));

        ElevatorMovement requiredMovement = new ElevatorMovement(0, 2);

        assertThat(statisticsService.getElevatorStatistics().get(elevator.getName()).getElevatorMovements().size(), is(equalTo(1)));
        assertThat(statisticsService.getElevatorStatistics().get(elevator.getName()).getElevatorMovements().containsKey(requiredMovement), is(true));
        assertThat(statisticsService.getElevatorStatistics().get(elevator.getName()).getElevatorMovements().get(requiredMovement), is(equalTo(2)));
    }

    @SneakyThrows
    @Test
    void testRunTakeRandomPersons() {
        PassengerService passengerService = new PassengerService(3, 40, 100);
        floors.forEach(f -> f.setPassengerService(passengerService));
        floors.forEach(f -> f.setStatisticsService(statisticsService));

        floors.forEach(Thread::start);
        TimeUnit.MILLISECONDS.sleep(10);
        floors.forEach(Floor::setStop);

        elevator.start();
        TimeUnit.MILLISECONDS.sleep(400);
        elevator.setStop();

        assertThat(floorService.hasAnyPressedButton(), is(false));
    }
}