public class Instruction {

    private boolean direction; // Up = true, down = false
    private int floorNumber; // Just the floorNumber to go to

    public Instruction(boolean direction, int floorNumber){
        this.direction = direction; // Up = true, down = false
        this.floorNumber = floorNumber; // Just the floorNumber to go to
    }

    public int getFloorNumber() {
        return floorNumber;
    }

    public boolean getDirection(){
        return direction;
    }
}
