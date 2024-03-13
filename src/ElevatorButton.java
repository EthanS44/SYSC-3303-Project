import java.time.LocalDateTime;

public class ElevatorButton {
    private final int buttonNo;
    private Elevator elevator;

    public ElevatorButton(int buttonNumber) {
        this.buttonNo = buttonNumber;
    }

    public Request pushButton() {
        System.out.println("Elevator button " + buttonNo + " pushed!");
        LocalDateTime currentTime = LocalDateTime.now();
        Request request = new Request(true, currentTime, this.elevator.getElevatorID(), buttonNo, this.elevator.getCurrentFloor());
        System.out.println("Request sent by ElevatorButton "+ buttonNo);
        return request;
    }

    public int getButtonNo() {
        return buttonNo;
    }

    public void setElevator(Elevator elevator) {
        this.elevator = elevator;
    }
}
