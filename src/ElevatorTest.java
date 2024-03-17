import org.junit.Test;
import static org.junit.Assert.*;

public class ElevatorTest {

    @Test
    public void testElevatorInitialization() {
        Elevator elevator = new Elevator(10, 567, 234, 604);
        assertEquals(10, elevator.getElevatorID());
        assertEquals(1, elevator.getCurrentFloor());
    }

    @Test
    public void testElevatorMoveUp() {
        Elevator elevator1 = new Elevator(7, 77, 66, 90);
        Instruction instruction = new Instruction(true, 3);
        elevator1.getInstructionBox().add(instruction);
        elevator1.calculateNextFloor();
        assertEquals(3, elevator1.getCurrentFloor());
    }

    @Test
    public void testElevatorMoveDown() {
        Elevator elevator1 = new Elevator(7, 77, 66, 90);
        elevator1.setCurrentFloor(5);
        Instruction instruction = new Instruction(false, 2);
        elevator1.getInstructionBox().add(instruction);
        elevator1.calculateNextFloor();
        assertEquals(2, elevator1.getCurrentFloor());
    }

}
