public class DirectionLamp {

    private boolean lampOn;
    private int lampDirection; //1 for up, 0 for down

    private Elevator elevator;

    public DirectionLamp(Elevator elevator){
        this.lampOn = false;
        this.lampDirection = 0;
        this.elevator = elevator;
    }

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

    public void turnOffLamp(){
        this.lampOn = false;
        System.out.println("Elevator " + elevator.getElevatorID() + " lamp is off");
    }

    public int getLampDirection() {
        return lampDirection;
    }

    public boolean isLampOn() {
        return lampOn;
    }
}
