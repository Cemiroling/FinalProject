package domain;

import lombok.Value;
import service.FloorService;
import service.PassengerService;
import service.StatisticsService;

import java.util.List;

@Value
public class Building {
    List<Floor> floors;
    List<Elevator> elevators;
    StatisticsService statisticsService;

    public Building(List<Floor> floors, List<Elevator> elevators) {
        this.floors = floors;
        this.elevators = elevators;
        statisticsService = new StatisticsService(floors.size(), elevators.size());
    }

    public void start() {
        PassengerService passengerService = new PassengerService(floors.size(), 40, 100);
        FloorService floorService = new FloorService(floors);

        floors.forEach(f -> f.setPassengerService(passengerService));
        floors.forEach(f -> f.setStatisticsService(statisticsService));
        floors.forEach(Thread::start);

        elevators.forEach(e -> e.setFloorService(floorService));
        elevators.forEach(e -> e.setStatisticsService(statisticsService));
        elevators.forEach(Thread::start);
    }

    public void printStatistics() {
        statisticsService.printStatistics();
    }

    public void stop() {
        floors.forEach(Floor::setStop);
        elevators.forEach(Elevator::setStop);
    }
}