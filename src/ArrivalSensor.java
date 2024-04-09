public class ArrivalSensor {

    private int floorId;

    private Elevator elevator;

    public ArrivalSensor(int floorId, Elevator elevator){
        this.floorId = floorId;
        this.elevator = elevator;
    }

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
