import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class ElevatorButtonTest {

    @Test
    public void testButtonInitialization() {
        Elevator elevator = new Elevator(15, 675, 432, 687);
        ElevatorButton button = new ElevatorButton(1);
        button.setElevator(elevator);
        assertFalse(button.isPressed());
    }

    @Test
    public void testPressButton() {
        Elevator elevator = new Elevator(12, 898, 547, 796);
        ElevatorButton button = new ElevatorButton(1);
        button.setElevator(elevator);

        button.pushButtonTest();

        assertTrue(button.isPressed());
    }

}
