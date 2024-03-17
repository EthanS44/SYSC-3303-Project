import java.io.*;

public class Response implements Serializable {

    private int floorNumber; //floor number that the elevator has arrived to
    private int currentDirection; // 1 for up and 0 for down
    private int elevatorID;
    private boolean stoppingAtFloor;

    public Response(int floorNumber, int currentDirection, int elevatorID, boolean stoppingAtFloor){
        this.floorNumber = floorNumber;
        this.currentDirection = currentDirection;
        this.elevatorID = elevatorID;
        this.stoppingAtFloor = stoppingAtFloor;
    }

    public int getFloorNumber() {
        return floorNumber;
    }

    public int getCurrentDirection(){
        return currentDirection;
    }

    public int getElevatorID(){
        return elevatorID;
    }

    public boolean isStoppingAtFloor(){
        return stoppingAtFloor;
    }

    public byte[] toByteArray(Response response) throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
        objectOutputStream.writeObject(this);
        objectOutputStream.flush();
        return byteArrayOutputStream.toByteArray();
    }
    public static Response toResponse(byte[] byteArray) throws IOException, ClassNotFoundException {
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(byteArray);
        ObjectInputStream objectInputStream = new ObjectInputStream(byteArrayInputStream);
        return (Response) objectInputStream.readObject();
    }
}