// the Request class is essentially analogous to a Packet, this is the structure that the elevators and floors will send to the scheduler.

public class Request {
    private final boolean isElevator; // true for elevator, false for floor
    private final int time; // time the button was pushed
    private final int indexNumber; // floor/elevator index number
    private final int buttonId; // if isElevator = true: buttonId = floor button #. If isElevator = false: 1 = up, 0 = down.
    private boolean requestAcknowledged;

    public Request(boolean isElevator, int time, int indexNumber, int buttonId){
        this.isElevator = isElevator;
        this.time = time;
        this.indexNumber = indexNumber;
        this.buttonId = buttonId;
        requestAcknowledged = false;
    }
    public boolean isElevator(){
        return this.isElevator;
    }
    public int getTime(){
        return this.time;
    }
    public int getButtonId() {
        return buttonId;
    }
    public int getIndexNumber() {
        return indexNumber;
    }

    public void setRequestAcknowledged(boolean requestAcknowledged) {
        this.requestAcknowledged = requestAcknowledged;
    }

    public boolean getRequestAcknowledged(){
        return requestAcknowledged;
    }
}
