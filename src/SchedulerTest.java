
import org.junit.Test;
import static org.junit.Assert.*;

public class SchedulerTest {

    /**
     * Test Increment Elevator Capacity
     */
     @Test
    public void testIncrementElevatorCapacity() {
         String test = "test";
        Scheduler scheduler = new Scheduler(test);
        int initialCapacity = scheduler.getElevatorCapacity(1);
        scheduler.incrementElevatorCapacity(1);
        int newCapacity = scheduler.getElevatorCapacity(1);
        assertEquals(initialCapacity + 1, newCapacity);
    }

    /**
     * Test Decrement elevator capacity
     */
    @Test
    public void testDecrementElevatorCapacity() {
        String test = "test";
        Scheduler scheduler = new Scheduler(test);
        scheduler.incrementElevatorCapacity(2); // Increment capacity to ensure it's greater than 0
        int initialCapacity = scheduler.getElevatorCapacity(2);
        scheduler.decrementElevatorCapacity(2);
        int newCapacity = scheduler.getElevatorCapacity(2);
        assertEquals(initialCapacity - 1, newCapacity);
    }

    /**Test method for getting the closest elevator
     *
     */
    @Test
    public void testGetClosestElevator() {
        String test = "test";
        Scheduler scheduler = new Scheduler(test);
        scheduler.incrementElevatorCapacity(1);
        scheduler.incrementElevatorCapacity(2);
        scheduler.incrementElevatorCapacity(3);
        scheduler.incrementElevatorCapacity(4);
        scheduler.incrementElevatorCapacity(1);

        // Get the closest elevator for a specific floor
        int closestElevator = scheduler.getClosestElevator(5, 0);
        assertTrue(closestElevator > 0 && closestElevator <= 4);
    }
    /**
     * Test get elevator position
     */
    // Test method for getting the elevator position
    @Test
    public void testGetElevatorPosition() {
        String test = "test";
        Scheduler scheduler = new Scheduler(test);
        // Set elevator positions
        scheduler.setElevatorPosition(1, 5);
        scheduler.setElevatorPosition(2, 8);
        scheduler.setElevatorPosition(3, 3);
        scheduler.setElevatorPosition(4, 10);

        // Test each elevator position
        assertEquals(5, scheduler.getElevatorPosition(1));
        assertEquals(8, scheduler.getElevatorPosition(2));
        assertEquals(3, scheduler.getElevatorPosition(3));
        assertEquals(10, scheduler.getElevatorPosition(4));

        // Test invalid elevator number
        assertEquals(-1, scheduler.getElevatorPosition(5));
    }

    /**
     * Test elevator direction
     */
    @Test
    public void testGetElevatorDirection() {
        Scheduler scheduler = new Scheduler();
        scheduler.setElevatorDirection(1, 1); // Set direction of elevator 1 to up
        scheduler.setElevatorDirection(2, 0); // Set direction of elevator 2 to down
        scheduler.setElevatorDirection(3, 0); // Set direction of elevator 3 to down
        scheduler.setElevatorDirection(4, 1); // Set direction of elevator 4 to up

        // Test direction of each elevator
        assertEquals(1, scheduler.getElevatorDirection(1));
        assertEquals(0, scheduler.getElevatorDirection(2));
        assertEquals(0, scheduler.getElevatorDirection(3));
        assertEquals(1, scheduler.getElevatorDirection(4));
    }

}
