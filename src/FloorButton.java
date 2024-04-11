/**
 * FloorButton represents a button on a floor
 */
public class FloorButton {

    int direction; //(0 = down and 1 = up)
    boolean buttonPressed;

    /**
     * Constructor for FloorButton class
     * @param direction of the button pressed
     */
    public FloorButton(int direction){
        this.direction = direction;
        buttonPressed = false;
    }

    /**
     * Sets button pressed to true
     */
    public void pressButton(){
        buttonPressed = true;
    }

    /**
     * Sets button pressed to false
     */
    public void resetButton(){
        buttonPressed = false;
    }

    /**
     * Checks if button is pressed or not
     * @return true if pressed flase otherwise
     */
    public boolean isButtonPressed() {
        return buttonPressed;
    }
}
