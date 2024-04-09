/**
 * The ElevatorMotor class represents the motor of an elevator, controlling its movement.
 */
public class ElevatorMotor {

    private boolean isRunning; // Flag indicating if the motor is running
    private int currentDirection; // 0 for down, 1 for up, -1 for a disabled elevator
    private int currentSpeed; // Current speed of the elevator motor

    /**
     * Constructs a new ElevatorMotor object with default settings.
     */
    public ElevatorMotor(){
        this.isRunning = false;
        this.currentDirection = 1; // Default direction: up
        this.currentSpeed = 0; // Default speed: 0
    }

    /**
     * Gets the current direction of the elevator motor.
     * @return The current direction (0 for down, 1 for up, -1 for disabled).
     */
    public int getCurrentDirection(){
        return currentDirection;
    }

    /**
     * Starts the elevator motor.
     */
    public void startMotor(){
        isRunning = true;
        currentSpeed = 8; // Assuming a default speed of 8 units
    }

    /**
     * Sets the direction of the elevator motor.
     * @param direction The direction to set (0 for down, 1 for up, -1 for disabled).
     */
    public void setDirection(int direction){
        this.currentDirection = direction;
    }

    /**
     * Stops the elevator motor.
     */
    public void stop(){
        isRunning = false;
        currentSpeed = 0;
    }

    /**
     * Gets the current speed of the elevator motor.
     * @return The current speed.
     */
    public int getCurrentSpeed() {
        return currentSpeed;
    }

    /**
     * Sets the current speed of the elevator motor.
     * @param currentSpeed The speed to set.
     */
    public void setCurrentSpeed(int currentSpeed) {
        this.currentSpeed = currentSpeed;
    }
}
