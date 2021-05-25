package service;

import domain.Passenger;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

class PassengerServiceTest {
    private PassengerService passengerService;

    @BeforeEach
    void before() {
        passengerService = new PassengerService(10, 40, 140);
    }

    @Test
    void testGeneratePassengerAndCheckBorders() {
        Passenger passenger = passengerService.generatePassenger(4);
        assertThat(passenger.getWeight(), is(greaterThan(40)));
        assertThat(passenger.getWeight(), is(lessThan(140)));
        assertThat(passenger.getDestinationFloor(), is(greaterThanOrEqualTo(0)));
        assertThat(passenger.getDestinationFloor(), is(lessThan(10)));
        assertThat(passenger.getDestinationFloor(), is(not(4)));
    }

    @Test
    void testGeneratePassengerWithNegativeFloor() {
        Assertions.assertThrows(IllegalArgumentException.class,
                () -> passengerService.generatePassenger(-1));
    }

    @Test
    void testGeneratePassengerWithBiggerThanMaximumFloor() {
        Assertions.assertThrows(IllegalArgumentException.class,
                () -> passengerService.generatePassenger(10));
    }
}