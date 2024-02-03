import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class InstructionTest {
    @Test
    void testGetFloorNumber() {
        // Test creating an instruction with direction UP and floor number 4
        Instruction instruction1 = new Instruction(true, 4);
        assertEquals(4, instruction1.getFloorNumber());

        // Test creating an instruction with direction DOWN and floor number 3
        Instruction instruction2 = new Instruction(false, 3);
        assertEquals(3, instruction2.getFloorNumber());

    }
    @Test
    void testGetDirection(){
        // Test creating an instruction with direction UP and floor number 2
        Instruction instruction1 = new Instruction(true, 2);
        assertTrue(instruction1.getDirection());

        // Test creating an instruction with direction DOWN and floor number 1
        Instruction instruction2 = new Instruction(false,1) ;
        assertFalse(instruction2.getDirection());
    }


}