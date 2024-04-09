public class FloorButton {

    int direction; //(0 = down and 1 = up)
    boolean buttonPressed;

    public FloorButton(int direction){
        this.direction = direction;
        buttonPressed = false;
    }


    public void pressButton(){
        buttonPressed = true;
    }

    public void resetButton(){
        buttonPressed = false;
    }

    public boolean isButtonPressed() {
        return buttonPressed;
    }
}
