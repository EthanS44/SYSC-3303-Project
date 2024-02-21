import java.time.LocalDateTime;

public class ElevatorButton {
    private final int buttonNo;
    private Elevator elevator;

    public ElevatorButton(int buttonNumber) {
        this.buttonNo = buttonNumber;
    }

    public void pushButton() {
        System.out.println("Button " + buttonNo + " pushed!");
        LocalDateTime currentTime = LocalDateTime.now();
        Request request = new Request(true, currentTime, this.elevator.getElevatorID(), buttonNo, this.elevator.getCurrentFloor());
        this.elevator.getElevatorQueue().putInRequestBox(request);
        System.out.println("Request sent!");
    }

    public int getButtonNo() {
        return buttonNo;
    }

    public void setElevator(Elevator elevator) {
        this.elevator = elevator;
    }
}
