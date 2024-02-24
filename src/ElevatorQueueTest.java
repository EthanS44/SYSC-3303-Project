import org.junit.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

public class ElevatorQueueTest {
    @Test
    public void testPutAndGetRequestBox(){
        ElevatorQueue elevatorQueue2 = new ElevatorQueue();
        LocalDateTime currentTime = LocalDateTime.now();

        // Test putting a request into the request box
        Request testRequest = new Request(false, currentTime, 1, 1, 1);
        elevatorQueue2.putInRequestBox(testRequest);
        //assert that request box is not empty
        assertFalse(elevatorQueue2.isRequestBoxEmpty());

        // Test getting the request from the request box
        Request retrievedRequest = elevatorQueue2.getFromRequestBox();

        // Assert that the retrieved request is the same as the one put into the box
        assertEquals(testRequest, retrievedRequest);
        //assert request box is empty
        assertTrue(elevatorQueue2.isRequestBoxEmpty());
    }
    @Test
    public void testPutAndGetInstructionBox(){
        ElevatorQueue elevatorQueue2 = new ElevatorQueue();

        // Test putting an instruction into the instruction box
        Instruction testInstruction = new Instruction(true, 3);
        elevatorQueue2.putInInstructionBox(testInstruction);

        //assert that Instruction box is not empty
        assertFalse(elevatorQueue2.isInstructionBoxEmpty());
        Instruction testInstruction2 = new Instruction(true, 4);
        elevatorQueue2.putInInstructionBox(testInstruction2);

        //assert the size of the instruction box increased.
        assertEquals(2,elevatorQueue2.instructionBox().size());
        // Test getting the instruction from the instruction box
        Instruction retrievedInstruction = elevatorQueue2.getFromInstructionBox();
        Instruction retrievedInstruction2 = elevatorQueue2.getFromInstructionBox();

        // Assert that the retrieved instruction is the same as the one put into the box
        assertEquals(testInstruction, retrievedInstruction);
        assertEquals(testInstruction2, retrievedInstruction2);
        //Check if instruction box is empty
        assertTrue(elevatorQueue2.isInstructionBoxEmpty());
    }
}