/**
 * The DirectionLamp class represents the lamps that
 * indicate the floor(s) which will be visited by the elevator
 */
public class DirectionLamp {

    private boolean lampOn;
    private int lampDirection; //1 for up, 0 for down

    private Elevator elevator;

    /**
     * Constructor for DirectionLamp class
     * @param elevator an elevator object
     */
    public DirectionLamp(Elevator elevator){
        this.lampOn = false;
        this.lampDirection = 0;
        this.elevator = elevator;
    }

    /**
     * turnOnLamp turns the lamp on
     * @param direction the direction which the elevator is going
     */
    public void turnOnLamp(int direction) {
        this.lampOn = true;
        this.lampDirection = direction;
        if (direction == 1) {
            System.out.println("Elevator " + elevator.getElevatorID() + " lamp is on and pointing up");
        }
        else{
            System.out.println("Elevator " + elevator.getElevatorID() + " lamp is on and pointing down");
        }
    }
    /**
     * turnOffLamp turns the lamp off
     *
     */

    public void turnOffLamp(){
        this.lampOn = false;
        System.out.println("Elevator " + elevator.getElevatorID() + " lamp is off");
    }
    /**
     * isLampOn checks if the lamp is on or off
     * @return returns true if on and false if off
     */

    public boolean isLampOn() {
        return lampOn;
    }
}
