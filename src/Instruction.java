public class Instruction {
    private boolean direction; // Up = true, down = false
    private int floorNumber; // Just the floorNumber to go to

    /**
     * Constructor for the instruction class
     * @param direction - Direction for elevator to go to
     * @param floorNumber - Floor for the elevator to go to
     */
    public Instruction(boolean direction, int floorNumber){
        this.direction = direction; // Up = true, down = false
        this.floorNumber = floorNumber; // Just the floorNumber to go to
    }

    /**
     * Getter for floor number
     * @return - floor number to go to
     */
    public int getFloorNumber() {
        return floorNumber;
    }

    /**
     * Getter for the direction
     * @return - Direction the elevator will go to
     */
    public boolean getDirection(){
        return direction;
    }
}
