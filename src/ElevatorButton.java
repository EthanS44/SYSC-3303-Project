import java.time.LocalDateTime;

public class ElevatorButton {
    private final int buttonNo;
    private Elevator elevator;

    public ElevatorButton(int buttonNumber) {
        this.buttonNo = buttonNumber;
    }

    public void pushButton() {
        System.out.println("Elevator button " + buttonNo + " pushed!");
        LocalDateTime currentTime = LocalDateTime.now();
        Request request = new Request(true, currentTime, this.elevator.getElevatorID(), buttonNo, this.elevator.getCurrentFloor());
        elevator.sendRequest(request);
    }

    public int getButtonNo() {
        return buttonNo;
    }

    public void setElevator(Elevator elevator) {
        this.elevator = elevator;
    }
}
