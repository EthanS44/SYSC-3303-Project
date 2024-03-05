import java.io.*;

public class Response {

    private int floorNumber; //floor number that the elevator has arrived to

    public Response(int floorNumber){
        this.floorNumber = floorNumber;
    }

    public int getFloorNumber() {
        return floorNumber;
    }

    public byte[] toByteArray(Response response) throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
        objectOutputStream.writeObject(this);
        objectOutputStream.flush();
        return byteArrayOutputStream.toByteArray();
    }
    public Response toResponse(byte[] byteArray) throws IOException, ClassNotFoundException {
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(byteArray);
        ObjectInputStream objectInputStream = new ObjectInputStream(byteArrayInputStream);
        return (Response) objectInputStream.readObject();
    }
}
