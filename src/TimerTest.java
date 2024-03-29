import org.junit.Test;
import static org.junit.Assert.*;

public class TimerTest {

    @Test
    public void testSettingTimer() {
        Timer timer = new Timer();
        timer.setTimer(10);
        assertEquals(10, timer.getTime());
    }

    @Test
    public void testKillingTimer() {
        Timer timer = new Timer();
        timer.setTimer(10);
        timer.killTimer();
        assertEquals(0, timer.getTime());
    }

    @Test
    public void testTimerActivation() {
        Timer timer = new Timer();
        timer.setTimer(10);
        assertTrue(timer.isActive());
    }

    @Test
    public void testTimerDeactivation() {
        Timer timer = new Timer();
        timer.setTimer(10);
        timer.killTimer();
        assertFalse(timer.isActive());
    }

    // You can similarly implement other test cases

    @Test
    public void testTimerRunning() throws InterruptedException {
        Timer timer = new Timer();
        timer.setTimer(3);
        Thread.sleep(3500); // Wait for timer to run down
        assertEquals(0, timer.getTime());
    }

    @Test
    public void testTimerDeactivationOnTimeout() throws InterruptedException {
        Elevator elevator = new Elevator(150, 600, 502, 305);
        Timer timer = new Timer();
        timer.setElevator(elevator);
        timer.setTimer(1);
        Thread.sleep(1500);
        assertFalse(timer.isActive());
        // You need to check whether elevator is disabled, if disabling is implemented in Elevator class
    }

    @Test
    public void testSettingElevatorReference() {
        Elevator elevator = new Elevator(150, 600, 502, 305);
        Timer timer = new Timer();
        timer.setElevator(elevator);
        assertEquals(elevator, timer.getElevator());
    }
}
