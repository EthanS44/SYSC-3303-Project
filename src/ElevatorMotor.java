public class ElevatorMotor {

    private boolean isRunning;
    private int currentDirection; //0 for down, 1 for up
    private Elevator elevator;

    public ElevatorMotor(){
        this.isRunning = false;
        this.currentDirection = 1;
    }

    public boolean isMotorRunning(){
        return isRunning;
    }

    public int getCurrentDirection(){
        return currentDirection;
    }

    public void startMotor(){
        isRunning = true;
    }

    public void setDirection(int direction){
        this.currentDirection = direction;
    }

    public void stop(){
        isRunning = false;
    }
}

