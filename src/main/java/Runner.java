import domain.Building;
import domain.Elevator;
import domain.Floor;
import lombok.SneakyThrows;
import service.BuildingService;

import java.util.List;
import java.util.concurrent.TimeUnit;

public class Runner {
    @SneakyThrows
    public static void main(String[] args) {
        List<Floor> floors = BuildingService.createFloors(10, 10);
        List<Elevator> elevators = BuildingService.createElevators(2, 500, 1, .5f);

        Building building = new Building(floors, elevators);
        building.start();

        TimeUnit.SECONDS.sleep(30);

        building.stop();
        building.printStatistics();
    }
}
