import java.io.*;

public class Response implements Serializable {

    private int floorNumber; //floor number that the elevator has arrived to
    private int currentDirection; // 1 for up and 0 for down
    private int elevatorID;
    private boolean stoppingAtFloor;

    /**
     * Constructor for Elevator class
     * @param floorNumber
     * @param currentDirection
     * @param elevatorID
     * @param stoppingAtFloor
     */
    public Response(int floorNumber, int currentDirection, int elevatorID, boolean stoppingAtFloor){
        this.floorNumber = floorNumber;
        this.currentDirection = currentDirection;
        this.elevatorID = elevatorID;
        this.stoppingAtFloor = stoppingAtFloor;
    }

    /**
     * Getter for floor number
     * @return Floor number
     */
    public int getFloorNumber() {
        return floorNumber;
    }


    /**
     * Getter for current direction
     * @return current direction
     */
    public int getCurrentDirection(){
        return currentDirection;
    }

    /**
     * Getter for Elevator ID
     * @return Elevator ID
     */
    public int getElevatorID(){
        return elevatorID;
    }

    /**
     * Checks if elevator stopped at a particular floor
     * @return true if stopped at the floor and false otherwise
     */
    public boolean isStoppingAtFloor(){
        return stoppingAtFloor;
    }

    /**
     * Converts response to byte
     * @param response to be converted
     * @return byte
     * @throws IOException
     */
    public byte[] toByteArray(Response response) throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
        objectOutputStream.writeObject(this);
        objectOutputStream.flush();
        return byteArrayOutputStream.toByteArray();
    }
    /**
     * Converts byte to response
     * @param byteArray to be converted
     * @return response
     */
    public static Response toResponse(byte[] byteArray) throws IOException, ClassNotFoundException {
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(byteArray);
        ObjectInputStream objectInputStream = new ObjectInputStream(byteArrayInputStream);
        return (Response) objectInputStream.readObject();
    }
}