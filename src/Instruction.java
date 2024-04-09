import java.io.*;

public class Instruction implements Serializable {

    private boolean direction; // Up = true, down = false
    private int floorNumber; // Just the floorNumber to go to
    private int triggerFault;

    /**
     * Constructor for the instruction class
     * @param direction - Direction for elevator to go to
     * @param floorNumber - Floor for the elevator to go to
     */
    public Instruction(boolean direction, int floorNumber, int triggerFault){
        this.direction = direction; // Up = true, down = false
        this.floorNumber = floorNumber; // Just the floorNumber to go to
        this.triggerFault = triggerFault; // 0 = no fault, 1 = transient fault(door fault), 2 = hard fault(floor fault)
    }


    /**
     * Getter for floor number
     * @return - floor number to go to
     */
    public int getFloorNumber() {
        return floorNumber;
    }

    /**
     * Getter for trigger fault
     * @return - trigger fault int
     */
    public int getTriggerFault(){
        return triggerFault;
    }

    /**
     * Getter for the direction
     * @return - Direction the elevator will go to
     */
    public boolean getDirection(){
        return direction;
    }
    public byte[] toByteArray(Instruction instruction) throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
        objectOutputStream.writeObject(this);
        objectOutputStream.flush();
        return byteArrayOutputStream.toByteArray();
    }
    public static Instruction toInstruction(byte[] byteArray) throws IOException, ClassNotFoundException {
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(byteArray);
        ObjectInputStream objectInputStream = new ObjectInputStream(byteArrayInputStream);
        return (Instruction) objectInputStream.readObject();
    } // Instruction.toInstruction(byte[]) will return the instruction represented by the byte array
}
