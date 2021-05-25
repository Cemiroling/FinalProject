package domain;

import lombok.Getter;
import lombok.Setter;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import service.PassengerService;
import service.StatisticsService;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.TimeUnit;

@Getter
@Slf4j
public class Floor extends Thread {
    private final float generationInterval;
    private final int number;
    private final Queue<Passenger> upQueue;
    private final Queue<Passenger> downQueue;
    private boolean isStop;
    @Setter
    private PassengerService passengerService;
    @Setter
    private StatisticsService statisticsService;

    public Floor(int number, float generationInterval) {
        this.generationInterval = generationInterval * 1000;
        this.number = number;
        this.setName("Floor " + number);
        upQueue = new ArrayDeque<>();
        downQueue = new ArrayDeque<>();
    }

    @SneakyThrows
    @Override
    public void run() {
        while (!isStop) {
            addPassenger(passengerService.generatePassenger(number));
            statisticsService.increaseNumberOfGeneratedPassengers(this.getName());
            TimeUnit.MILLISECONDS.sleep((long) generationInterval);
        }
    }

    public void addPassenger(Passenger passenger) {
        if (passenger.getDestinationFloor() > number)
            upQueue.add(passenger);
        else
            downQueue.add(passenger);
    }

    private synchronized List<Passenger> getPassengers(int capacity, Queue<Passenger> queue) {
        List<Passenger> passengerList = new ArrayList<>();
        while (queue.peek() != null) {
            if (queue.peek().getWeight() <= capacity) {
                capacity -= queue.peek().getWeight();
                passengerList.add(queue.poll());
            } else {
                log.info("Not enough capacity");
                break;
            }
        }
        return passengerList;
    }

    public List<Passenger> getPassengersFromQueue(int capacity, ElevatorState state) {
        if (state == ElevatorState.MOVING_UP)
            return getPassengers(capacity, upQueue);
        else
            return getPassengers(capacity, downQueue);
    }

    public boolean isButtonUpPressed() {
        return !upQueue.isEmpty();
    }

    public boolean isButtonDownPressed() {
        return !downQueue.isEmpty();
    }

    public boolean isAnyButtonPressed() {
        return isButtonDownPressed() || isButtonUpPressed();
    }

    public void setStop() {
        isStop = true;
    }
}
