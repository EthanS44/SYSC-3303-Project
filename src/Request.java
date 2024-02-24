// the Request class is essentially analogous to a Packet, this is the structure that the elevators and floors will send to the scheduler.

import java.time.LocalDateTime;
public class Request {
    private final boolean isElevator; // true for elevator, false for floor
    private final LocalDateTime time; // time the button was pushed
    private final int indexNumber; // floor/elevator index number
    private final int buttonId; // if isElevator = true: buttonId = floor button #. If isElevator = false: 1 = up, 0 = down.
    private final int currentFloor;
    private boolean requestAcknowledged;

    /**
     * Constructor for the Request class
     * @param isElevator - True if an elevator is making the request, false if it's the floor
     * @param time - Current time
     * @param indexNumber - what floor/elevator is the request coming from
     * @param buttonId - if isElevator = true: buttonId = floor button #. If isElevator = false: 1 = up, 0 = down.
     * @param currentFloor - What floor the elevator is currently on/what floor the request is being made from
     */
    public Request(boolean isElevator, LocalDateTime time, int indexNumber, int buttonId, int currentFloor){
        this.isElevator = isElevator;
        this.time = time;
        this.indexNumber = indexNumber;
        this.buttonId = buttonId;
        this.currentFloor = currentFloor;
    }

    /**
     * Getter for isElevator
     * @return - True if it's coming from an elevator, false if it's coming from a floor
     */
    public boolean isElevator(){
        return this.isElevator;
    }

    /**
     * Getter for time
     * @return - LocalDateTime current time
     */
    public LocalDateTime getTime(){
        return this.time;
    }

    /**
     * Getter for buttonID
     * @return - if isElevator = true: buttonId = floor button #. If isElevator = false: 1 = up, 0 = down.
     */
    public int getButtonId() {
        return buttonId;
    }

    /**
     * Getter for index number
     * @return - what floor/elevator is the request coming from
     */
    public int getIndexNumber() {
        return indexNumber;
    }

    /**
     * Getter for current floor
     * @return - What floor the elevator is currently on/what floor the request is being made from
     */
    public int getCurrentFloor() {return currentFloor;}

}
