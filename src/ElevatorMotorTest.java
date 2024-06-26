import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class ElevatorMotorTest {

    /**
     * Test motor initialization
     */
    @Test
    public void testMotorInitialization() {
        ElevatorMotor motor = new ElevatorMotor();
        assertEquals(0, motor.getCurrentSpeed());
    }

    /**
     * Test set speed method
     */
    @Test
    public void testSetSpeed() {
        ElevatorMotor motor = new ElevatorMotor();
        motor.setCurrentSpeed(10);
        assertEquals(10, motor.getCurrentSpeed());
    }
}
