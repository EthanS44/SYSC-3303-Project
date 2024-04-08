import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.DatagramPacket;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;

import org.junit.Test;
import static org.junit.jupiter.api.Assertions.*;

public class ParserTest {

    @Test
    public void testParseTextFile(){
        DatagramPacket data1;
        DatagramPacket data2;
        DatagramPacket data3;
        Parser parse = new Parser();
        File inputTestFile = new File("inputFile.txt");
        ArrayList<Instruction> list = parse.testParseTextFile(inputTestFile);

        assertEquals(list.get(0).getTriggerFault(), 0);
        assertEquals(list.get(0).getDirection(), false);
        assertEquals(list.get(0).getFloorNumber(),1);


        assertEquals(list.get(1).getTriggerFault(), 0);
        assertEquals(list.get(1).getDirection(), false);
        assertEquals(list.get(1).getFloorNumber(),1);

        /**
        assertEquals(list.get(2).getTriggerFault(), 2);
        assertEquals(list.get(2).getDirection(), false);
        assertEquals(list.get(2).getFloorNumber(),4);
         **/
    }

}
