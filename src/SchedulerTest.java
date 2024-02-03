import org.junit.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

public class SchedulerTest {
    @Test
    public void testAcknowledgeRequest() {
        ElevatorQueue elevatorQueue = new ElevatorQueue();
        Scheduler scheduler = new Scheduler(elevatorQueue);

        LocalDateTime currentTime = LocalDateTime.now();
        Request testRequest = new Request(true, currentTime, 1, 1, 5);

        assertFalse(testRequest.getRequestAcknowledged()); // Initial state is false

        scheduler.acknowledgeRequest(testRequest);

        assertTrue(testRequest.getRequestAcknowledged());
    }

    @Test
    public void testHandleRequestEleavtor() {
        ElevatorQueue elevatorQueue = new ElevatorQueue();
        Scheduler scheduler = new Scheduler(elevatorQueue);

        LocalDateTime currentTime = LocalDateTime.now();
        Request elevatorRequest = new Request(true, currentTime, 1, 1, 5);

        elevatorQueue.putInRequestBox(elevatorRequest);

        scheduler.handleRequest();

        assertTrue(elevatorRequest.getRequestAcknowledged());
    }

    @Test
    public void testHandleRequestFloor() {
        ElevatorQueue elevatorQueue = new ElevatorQueue();
        Scheduler scheduler = new Scheduler(elevatorQueue);

        LocalDateTime currentTime = LocalDateTime.now();
        Request testFloorRequest = new Request(false, currentTime, 2, 1, 3);

        elevatorQueue.putInRequestBox(testFloorRequest);

        scheduler.handleRequest();


        assertTrue(testFloorRequest.getRequestAcknowledged());
    }

}