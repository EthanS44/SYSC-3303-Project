import org.junit.Test;

import static org.junit.jupiter.api.Assertions.*;

public class FloorTest {
    /**
     * Test floor Initialization
     */
    @Test
    public void testFloorInitialization() {


        // Test creating a floor and check its initial state
        Floor floor = new Floor(3);
        assertEquals(3, floor.getFloorNumber());
        assertFalse(floor.isWaiting());
    }

    /**
     * Test button pushed
     */
    @Test
    public void testButtonPushed() {

        Floor floor = new Floor(5);

        // Test pressing the UP button on the floor
        assertTrue(floor.pushButton(true, 0));
        assertTrue(floor.isWaiting());

        // Test pressing the DOWN button on the floor
        assertTrue(floor.pushButton(false, 0));
        assertTrue(floor.isWaiting());
    }

    /**
     * Test button pushed
     */
    @Test
    public void testFloorNotWaitingInitially() {

        Floor floor = new Floor(2);

        // The floor should not be waiting initially
        assertFalse(floor.isWaiting());
    }

    /**
     * Test floor fault
     */
    @Test
    public void testFloorFault() {
        Floor floor = new Floor(2, true);
        Elevator elevator = new Elevator(84);
        Timer timer = new Timer();
        timer.setElevator(elevator);
        timer.setTimer(1);

        //Assume that floor gets stuck on floor 3
        floor.pushButton(true, true);
        elevator.setCurrentFloor(5);
        elevator.runElevator(3);
        timer.runTimer();

        try {
            Thread.sleep(4000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        elevator.setCurrentFloor(-1);

        assertFalse(timer.isActiveTest());

    }
}