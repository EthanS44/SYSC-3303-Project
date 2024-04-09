import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class
FloorButtonTest {

    @Test
    public void testButtonInitialization() {
        FloorButton button = new FloorButton(1);
        assertFalse(button.isButtonPressed());
    }

    @Test
    public void testPressButton() {
        FloorButton button = new FloorButton(1);
        button.pressButton();
        assertTrue(button.isButtonPressed());
    }

    @Test
    public void testResetButton() {
        FloorButton button = new FloorButton(1);
        button.pressButton();
        button.resetButton();
        assertFalse(button.isButtonPressed());
    }

}
