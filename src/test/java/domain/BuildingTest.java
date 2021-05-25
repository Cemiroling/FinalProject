package domain;

import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import service.BuildingService;

import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

class BuildingTest {

    @SneakyThrows
    @Test
    void testSimulation() {
        List<Floor> floors = BuildingService.createFloors(10, 5);
        List<Elevator> elevators = BuildingService.createElevators(2, 500, .02f, .01f);

        Building building = new Building(floors, elevators);

        building.start();
        TimeUnit.MILLISECONDS.sleep(1000);
        building.stop();

        int totalGenerated = building.getStatisticsService().getFloorStatistics()
                .values()
                .stream()
                .reduce(0, Integer::sum);
        int totalElevated = building.getStatisticsService()
                .getElevatorStatistics()
                .values()
                .stream()
                .flatMapToInt(e -> IntStream.of(e.getTotalNumberOfMovements()))
                .sum();
        assertThat(totalGenerated, is(equalTo(totalElevated)));
    }
}