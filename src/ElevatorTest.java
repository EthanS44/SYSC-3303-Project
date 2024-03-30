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
        Thread elevator1Thread = new Thread(elevator1);
        elevator1Thread.start();

        Instruction instruction = new Instruction(true, 3);
        elevator1.getInstructionBox().add(instruction);
        elevator1.calculateNextFloor();

        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        assertEquals(3, elevator1.getCurrentFloor());
    }

    @Test
    public void testElevatorMoveDown() {
        Elevator elevator1 = new Elevator(7, 77, 66, 90);
        Thread elevator1Thread = new Thread(elevator1);
        elevator1Thread.start();

        elevator1.setCurrentFloor(5);
        Instruction instruction = new Instruction(false, 2);
        elevator1.getInstructionBox().add(instruction);
        elevator1.calculateNextFloor();

        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        assertEquals(2, elevator1.getCurrentFloor());
    }

    //Elevator Fault
    @Test
    public void testElevatorFault() {
        Elevator elevator1 = new Elevator(7);
        Timer timer = new Timer();
        timer.setElevator(elevator1);

        elevator1.setCurrentFloor(5);
        Instruction instruction = new Instruction(false, 2);
        elevator1.getInstructionBox().add(instruction);
        elevator1.runElevator(3);
        timer.runTimer();

        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        if (elevator1.getCurrentFloor() != 2) {
            elevator1.setCurrentFloor(-1);
        }

        assertFalse(timer.isActiveTest());
    }

}
