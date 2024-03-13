public class ArrivalSensor {

    private int floorId;
    private Elevator elevator;

    public ArrivalSensor(int floorId, Elevator elevator){
        this.floorId = floorId;
        this.elevator = elevator;
    }

    public void handleArrivalSensor(){
        System.out.println("Elevator " + elevator.getElevatorID() + " is approaching floor " + floorId);
        if(floorId != elevator.getNextFloor()){
            Instruction instructionToRemove = elevator.isFloorInQueue(floorId);
            if(instructionToRemove != null) {
                System.out.println("Elevator " + elevator.getElevatorID() + " stopping at floor " + floorId);
                elevator.getInstructionBox().remove(instructionToRemove);
                elevator.getInstructionBox().add(0, instructionToRemove);
                elevator.setCurrentState(new ElevatorWaiting());
            }
        }
    }
}
