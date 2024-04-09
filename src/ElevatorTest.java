import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.*;

public class ElevatorTest {

    @Test
    public void testElevatorInitialization() {
        Elevator elevator = new Elevator(10, 567, 234, 604, 125);
        assertEquals(10, elevator.getElevatorID());
        assertEquals(1, elevator.getCurrentFloor());
    }


    @Test
    public void testElevatorMoveUp() {
        Elevator elevator1 = new Elevator(7, 77, 66, 90, 126);
        Thread elevator1Thread = new Thread(elevator1);
        elevator1Thread.start();

        Instruction instruction = new Instruction(true, 3,0);
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
        Elevator elevator1 = new Elevator(7, 77, 66, 90, 127);
        Thread elevator1Thread = new Thread(elevator1);
        elevator1Thread.start();

        elevator1.setCurrentFloor(5);
        Instruction instruction = new Instruction(false, 2,0);
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
        ArrayList<Integer> floorFault = new ArrayList<Integer>();
        floorFault.add(3);
        elevator1.setFloorFaults(floorFault);
        elevator1.setCurrentFloor(5);
        Instruction instruction = new Instruction(false, 3,0);
        elevator1.getInstructionBox().add(instruction);
        elevator1.runElevator(3);
        timer.runTimer();

        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        if (elevator1.getFloorFaults().get(0) == 3) {
            System.out.println("Triggering a hard fault in elevator " + elevator1.getElevatorID());
            elevator1.setElevatorEnabled(false);
            elevator1.getMotor().setDirection(-1); //set direction to -1 to signify a disabled elevator
            elevator1.setCurrentFloor(-1);
            try { // CORN
                Thread.sleep(10000);

            } catch (InterruptedException e) {
            }
            elevator1.getFloorFaults().set(0, 0);
        }

        assertEquals(elevator1.getCurrentFloor(), -1);
    }



}
