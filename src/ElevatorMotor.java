public class ElevatorMotor {

    private boolean isRunning;
    private int currentDirection; //0 for down, 1 for up
    private int currentSpeed;
    private Elevator elevator;

    public ElevatorMotor(){
        this.isRunning = false;
        this.currentDirection = 1;
        currentSpeed = 0;
    }

    public boolean isMotorRunning(){
        return isRunning;
    }

    public int getCurrentDirection(){
        return currentDirection;
    }

    public void startMotor(){
        isRunning = true;
        currentSpeed = 8;
    }

    public void setDirection(int direction){
        this.currentDirection = direction;
    }

    public void stop(){
        isRunning = false;
        currentSpeed = 0;
    }

    public void changeDirection(){
        if (currentDirection == 1){
            currentDirection = 0;
        }
        else{
            currentDirection = 1;
        }
    }

    public int getCurrentSpeed() {
        return currentSpeed;
    }

    public void setCurrentSpeed(int currentSpeed) {
        this.currentSpeed = currentSpeed;
    }
}

