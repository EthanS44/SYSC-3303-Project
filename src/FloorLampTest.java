import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class FloorLampTest {

    @Test
    public void testLampInitialization() {
        FloorLamp lamp = new FloorLamp();
        assertFalse(lamp.isLampOn());
    }


    @Test
    public void testTurnOnLamp() {
        FloorLamp lamp = new FloorLamp();
        lamp.turnOnLamp();
        assertTrue(lamp.isLampOn());
    }

    @Test
    public void testTurnOffLamp() {
        FloorLamp lamp = new FloorLamp();
        lamp.turnOnLamp();
        lamp.turnOffLamp();
        assertFalse(lamp.isLampOn());
    }

}

