import org.junit.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

public class SchedulerTest {
    @Test
    public void testAcknowledgeRequest() {
        // ElevatorQueue elevatorQueue = new ElevatorQueue();
        Scheduler scheduler = new Scheduler(578, 385,925);

        LocalDateTime currentTime = LocalDateTime.now();
        Request testRequest = new Request(true, currentTime, 1, 1, 5, 5, 0);
    }

    @Test
    public void testHandleRequestEleavtor() {
        ElevatorQueue elevatorQueue = new ElevatorQueue();
        Scheduler scheduler = new Scheduler(453, 234,677);

        LocalDateTime currentTime = LocalDateTime.now();
        Request elevatorRequest = new Request(true, currentTime, 1, 1, 5, 5,0);

        elevatorQueue.putInRequestBox(elevatorRequest);

        scheduler.handleRequest();
    }

    @Test
    public void testHandleRequestFloor() {
        ElevatorQueue elevatorQueue = new ElevatorQueue();
        Scheduler scheduler = new Scheduler(894, 287, 903);

        LocalDateTime currentTime = LocalDateTime.now();
        Request testFloorRequest = new Request(false, currentTime, 2, 1, 3, 3,0);

        elevatorQueue.putInRequestBox(testFloorRequest);

        scheduler.handleRequest();
    }

}