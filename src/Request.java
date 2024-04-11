/**The Request class is essentially analogous to a Packet,
 *  this is the structure that the elevators and floors will send to the scheduler.
 *
 */


import java.io.*;
import java.time.LocalDateTime;
public class Request implements Serializable  {
    private final boolean isElevator; // true for elevator, false for floor
    private final LocalDateTime time; // time the button was pushed
    private final int indexNumber; // floor/elevator index number
    private final int buttonId; // if isElevator = true: buttonId = floor button #. If isElevator = false: 1 = up, 0 = down.
    private final int currentFloor;
    private final int elevatorFloorID;
    private final int triggerFault;

    /**
     * Constructor for the Request class
     * @param isElevator - True if an elevator is making the request, false if it's the floor
     * @param time - Current time
     * @param indexNumber - what floor/elevator is the request coming from
     * @param buttonId - if isElevator = true: buttonId = floor button #. If isElevator = false: 1 = up, 0 = down.
     * @param currentFloor - What floor the elevator is currently on/what floor the request is being made from
     * @param elevatorFloorID - What floor or elevator the request is coming from
     * @param triggerFault - 0 - no fault, 1 - transient fault, 2 - hard fault
     */
    public Request(boolean isElevator, LocalDateTime time, int indexNumber, int buttonId, int currentFloor, int elevatorFloorID, int triggerFault){
        this.isElevator = isElevator;
        this.time = time;
        this.indexNumber = indexNumber;
        this.buttonId = buttonId;
        this.currentFloor = currentFloor;
        this.elevatorFloorID = elevatorFloorID;
        this.triggerFault = triggerFault;
    }

    /**
     * Getter for isElevator
     * @return - True if it's coming from an elevator, false if it's coming from a floor
     */
    public boolean isElevator(){
        return this.isElevator;
    }

    public int getElevatorFloorID(){
        return this.elevatorFloorID;
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

    /**
     * Getter for current triggerFault
     * @return the type of trigger fault, whether hard, transient or none
     */
    public int getTriggerFault(){
        return triggerFault;
    }

    /**
     * Converts the request byte to be able to send as packet
     * @param request the request to be converted
     * @return byte that is sent as a packet
     * @throws IOException
     */
    public static byte[] toByteArray(Request request) throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
        objectOutputStream.writeObject(request);
        objectOutputStream.flush();
        return byteArrayOutputStream.toByteArray();
    }
    /**
     * This method converts the byte to a request
     * @param byteArray that is to be converted
     * @return Request
     * @throws IOException
     * @throws ClassNotFoundException
     */
    public static Request toRequest(byte[] byteArray) throws IOException, ClassNotFoundException {
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(byteArray);
        ObjectInputStream objectInputStream = new ObjectInputStream(byteArrayInputStream);
        return (Request) objectInputStream.readObject();
    }


}
