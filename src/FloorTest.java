import org.junit.Test;

import static org.junit.jupiter.api.Assertions.*;

public class FloorTest {
    @Test
    public void testFloorInitialization() {
        ElevatorQueue elevatorQueue = new ElevatorQueue();

        // Test creating a floor and check its initial state
        Floor floor = new Floor(3);
        assertEquals(3, floor.getFloorNumber());
        assertFalse(floor.isWaiting());
    }

    @Test
    public void testButtonPushed() {
        ElevatorQueue elevatorQueue = new ElevatorQueue();
        Floor floor = new Floor(5);

        // Test pressing the UP button on the floor
        assertTrue(floor.pushButton(true));
        assertTrue(floor.isWaiting());

        // Test pressing the DOWN button on the floor
        assertTrue(floor.pushButton(false));
        assertTrue(floor.isWaiting());
    }

    @Test
    public void testFloorNotWaitingInitially() {
        ElevatorQueue elevatorQueue = new ElevatorQueue();
        Floor floor = new Floor(2);

        // The floor should not be waiting initially
        assertFalse(floor.isWaiting());
    }

    //Floor Fault

}