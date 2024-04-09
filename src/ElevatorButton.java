import java.time.LocalDateTime;

public class ElevatorButton {
    private final int buttonNo;
    private Elevator elevator;
    private boolean pressed;

    public ElevatorButton(int buttonNumber) {
        this.buttonNo = buttonNumber;
    }

    public void pushButton(int faultType) {
        System.out.println("Elevator button " + buttonNo + " pushed!");
        LocalDateTime currentTime = LocalDateTime.now();
        Request request = new Request(true, currentTime, this.elevator.getElevatorID(), buttonNo, this.elevator.getCurrentFloor(), this.elevator.getElevatorID(), faultType);
        elevator.sendRequest(request);
        pressed = true;
    }

    public void pushButtonTest(){
        System.out.println("Elevator button " + buttonNo + " pushed!");
        LocalDateTime currentTime = LocalDateTime.now();
        Request request = new Request(true, currentTime, this.elevator.getElevatorID(), buttonNo, this.elevator.getCurrentFloor(), this.elevator.getElevatorID(),0);

        //Assuming Request was actually sent
        System.out.println("Packet Successfully Sent!");
        pressed = true;
    }

    public int getButtonNo() {
        return buttonNo;
    }

    public void setElevator(Elevator elevator) {
        this.elevator = elevator;
    }

    public boolean isPressed() {
        return pressed;
    }
}
