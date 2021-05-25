package domain;

import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import service.PassengerService;
import service.StatisticsService;

import java.util.List;
import java.util.concurrent.TimeUnit;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

class FloorTest {

    private Floor floor;

    @BeforeEach
    void before() {
        floor = new Floor(1, 0.9f);
    }

    @SneakyThrows
    @Test
    void run() {
        PassengerService passengerService = new PassengerService(3, 0, 100);
        floor.setPassengerService(passengerService);

        StatisticsService statisticsService = new StatisticsService(3, 1);
        floor.setStatisticsService(statisticsService);

        floor.start();
        TimeUnit.MILLISECONDS.sleep(100);
        floor.setStop();
        assertThat(floor.isAnyButtonPressed(), is(true));

    }

    @Test
    void testAddPassengerUp() {
        Passenger passenger = new Passenger(100, 2);
        floor.addPassenger(passenger);
        assertThat(floor.getUpQueue().peek(), is(passenger));
    }

    @Test
    void testAddPassengerDown() {
        Passenger passenger = new Passenger(100, 0);
        floor.addPassenger(passenger);
        assertThat(floor.getDownQueue().peek(), is(passenger));
    }

    @Test
    void getPassengersFromUpQueueWithCapacity() {
        Passenger passenger = new Passenger(100, 2);
        floor.addPassenger(passenger);
        List<Passenger> passengers = floor.getPassengersFromQueue(200, ElevatorState.MOVING_UP);
        assertThat(passengers.contains(passenger), is(true));
    }

    @Test
    void getPassengersFromDownQueueWithCapacity() {
        Passenger passenger = new Passenger(100, 0);
        floor.addPassenger(passenger);
        List<Passenger> passengers = floor.getPassengersFromQueue(200, ElevatorState.MOVING_DOWN);
        assertThat(passengers.contains(passenger), is(true));
    }

    @Test
    void getPassengersFromUpQueueWithoutCapacity() {
        Passenger passenger = new Passenger(100, 2);
        floor.addPassenger(passenger);
        List<Passenger> passengers = floor.getPassengersFromQueue(50, ElevatorState.MOVING_UP);
        assertThat(passengers.isEmpty(), is(true));
    }

    @Test
    void getPassengersFromDownQueueWithoutCapacity() {
        Passenger passenger = new Passenger(100, 0);
        floor.addPassenger(passenger);
        List<Passenger> passengers = floor.getPassengersFromQueue(50, ElevatorState.MOVING_DOWN);
        assertThat(passengers.isEmpty(), is(true));
    }

    @Test
    void isButtonUpPressedTrue() {
        Passenger passenger = new Passenger(100, 2);
        floor.addPassenger(passenger);

        assertThat(floor.isButtonUpPressed(), is(true));
    }

    @Test
    void isButtonUpPressedFalse() {
        assertThat(floor.isButtonUpPressed(), is(false));
    }

    @Test
    void isButtonDownPressedTrue() {
        Passenger passenger = new Passenger(100, 0);
        floor.addPassenger(passenger);

        assertThat(floor.isButtonDownPressed(), is(true));
    }

    @Test
    void isButtonDownPressedFalse() {
        assertThat(floor.isButtonDownPressed(), is(false));
    }

    @Test
    void isAnyButtonPressedTrue() {
        Passenger passenger = new Passenger(100, 0);
        floor.addPassenger(passenger);

        assertThat(floor.isAnyButtonPressed(), is(true));
    }

    @Test
    void isAnyButtonPressedFalse() {
        assertThat(floor.isAnyButtonPressed(), is(false));
    }

    @Test
    void setPassengerService() {
        PassengerService passengerService = new PassengerService(3, 0, 100);
        floor.setPassengerService(passengerService);
        assertThat(floor.getPassengerService(), is(equalTo(passengerService)));
    }

    @Test
    void setStatisticsService() {
        StatisticsService statisticsService = new StatisticsService(3, 1);
        floor.setStatisticsService(statisticsService);
        assertThat(floor.getStatisticsService(), is(equalTo(statisticsService)));
    }
}