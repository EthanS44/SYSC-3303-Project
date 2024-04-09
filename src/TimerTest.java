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

    @Test
    public void testTimerRunning() throws InterruptedException {
        Timer timer = new Timer();
        timer.setTimer(3);
        timer.runTimer();
        Thread.sleep(3500); // Wait for timer to run down
        assertEquals(0, timer.getTime());
    }

    @Test
    public void testTimerDeactivationOnTimeout() throws InterruptedException {
        Elevator elevator = new Elevator(56);
        Timer timer = new Timer();
        timer.setElevator(elevator);
        timer.setTimer(1);
        timer.runTimer();
        //For sake of testing, assume that it goes to the wrong floor/freezes at floor 4
        elevator.runElevator(4);
        Thread.sleep(5000);

        if(timer.getTime() == 0 && elevator.getCurrentFloor() != 5){
            elevator.setCurrentFloor(-1);
        }
        assertFalse(timer.isActiveTest());
    }

    @Test
    public void testSettingElevatorReference() {
        Elevator elevator = new Elevator(150);
        Timer timer = new Timer();
        timer.setElevator(elevator);
        assertEquals(elevator, timer.getElevator());
    }
}
