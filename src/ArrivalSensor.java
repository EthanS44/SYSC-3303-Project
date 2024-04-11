/**
 * Arrival sensor class is responsible for detecting when the elevator is approaching a
 floor and notifies the system.
 *
 */
public class ArrivalSensor {

    private int floorId;
    private Elevator elevator;

    /**
     * Construction for Arrival sensor class
     * @param floorId the floor number
     * @param elevator and elevator object
     */
    public ArrivalSensor(int floorId, Elevator elevator){
        this.floorId = floorId;
        this.elevator = elevator;
    }

    /**
     * handleArrivalSensor method handles the arrival sensor .
     * It checks the current direction of the elevator's motor and calculates the approaching floor
     * @return true if the elevator stopped and the required floor and false otherwise
     */
    public boolean handleArrivalSensor(){
        int direction = elevator.getMotor().getCurrentDirection();
        int approachingFloor;
        if (direction ==1) {
            approachingFloor = floorId + 1;
        }
        else {
            approachingFloor = floorId -1;
        }
        System.out.println("Elevator " + elevator.getElevatorID() + " is approaching floor " + approachingFloor + "\n");
        if(approachingFloor != elevator.getNextFloor()){
            Instruction instructionToRemove = elevator.isFloorInQueue(approachingFloor);
            if(instructionToRemove != null) {
                System.out.println("Elevator " + elevator.getElevatorID() + " stopping at floor " + approachingFloor + "\n");
                return true;
            }
        }
        return false;
    }
}
