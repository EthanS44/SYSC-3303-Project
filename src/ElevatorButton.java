import java.time.LocalDateTime;

/**
 * The ElevatorButton class represents a button inside an elevator cabin.
 */
public class ElevatorButton {
    private final int buttonNo;
    private Elevator elevator;
    private boolean pressed;

    /**
     * Constructs a new ElevatorButton object with the specified button number.
     * @param buttonNumber The number of the button.
     */
    public ElevatorButton(int buttonNumber) {
        this.buttonNo = buttonNumber;
    }

    /**
     * Pushes the elevator button, sending a request to the elevator controller.
     * @param faultType The type of fault to be triggered.
     */
    public void pushButton(int faultType) {
        System.out.println("Elevator button " + buttonNo + " pushed!");
        LocalDateTime currentTime = LocalDateTime.now();
        Request request = new Request(true, currentTime, this.elevator.getElevatorID(), buttonNo, this.elevator.getCurrentFloor(), this.elevator.getElevatorID(), faultType);
        elevator.sendRequest(request);
        pressed = true;
    }

    /**
     * Simulates pushing the elevator button for testing purposes.
     */
    public void pushButtonTest(){
        System.out.println("Elevator button " + buttonNo + " pushed!");
        LocalDateTime currentTime = LocalDateTime.now();
        Request request = new Request(true, currentTime, this.elevator.getElevatorID(), buttonNo, this.elevator.getCurrentFloor(), this.elevator.getElevatorID(),0);

        //Assuming Request was actually sent
        System.out.println("Packet Successfully Sent!");
        pressed = true;
    }
    /**
     * Sets the elevator associated with this button.
     * @param elevator The elevator object to be associated.
     */
    public void setElevator(Elevator elevator) {
        this.elevator = elevator;
    }

    /**
     * Checks if the button has been pressed.
     * @return true if the button has been pressed, false otherwise.
     */
    public boolean isPressed() {
        return pressed;
    }
}
