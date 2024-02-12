import java.lang.reflect.Array;
import java.util.ArrayList;

//new
class ElevatorWaiting implements schedulerState {
    @Override
    public void handle(Scheduler scheduler){


        if (scheduler.hasPendingRequests()) {
            scheduler.setCurrentState(new HandlingRequest());
        } else {
            System.out.println("No pending requests. Waiting...");
        }
    }

} //new
class HandlingRequest implements schedulerState{
    @Override
    public void handle(Scheduler scheduler){
        scheduler.handleRequest();
        scheduler.setCurrentState(new ElevatorWaiting());
    }
}
public class Scheduler implements Runnable {
    private static final int numberOfFloors = 7;
    private final ElevatorQueue elevatorqueue;
    private schedulerState currentState; //new


    public Scheduler(ElevatorQueue queue){
        this.elevatorqueue = queue;
        this.currentState = new ElevatorWaiting(); //new
        System.out.println("Scheduler created\n");
    }

    //new
    public schedulerState getCurrentState() { return currentState; }
    //new
    public void setCurrentState(schedulerState state){
        this.currentState = state;
    }
    //new
    public void request() {
        this.currentState.handle(this);
    }

    public void acknowledgeRequest(Request request){
        request.setRequestAcknowledged(true);
    }

    public void handleRequest(){
        Request requestToHandle = elevatorqueue.getFromRequestBox();
        System.out.println("Request received by scheduler, now handling\n");

        //This is for gathering information about what will be in the instruction
        boolean tempDirection;
        int tempFloorNumber;

        //If it's an elevator it has it's own set of rules
        if(requestToHandle.isElevator()){
            if(requestToHandle.getCurrentFloor() > requestToHandle.getButtonId()){
                tempDirection = false; // Down = false
            }
            else{
                tempDirection = true; // Up = True
            }
            tempFloorNumber = requestToHandle.getButtonId(); // Copies the floor number to the temp variable

        }
        else {
            if(requestToHandle.getCurrentFloor() > requestToHandle.getIndexNumber()){
                tempDirection = false; // Down = False
            } else {
                tempDirection = true; // Up = True
            }
            tempFloorNumber = requestToHandle.getIndexNumber();
        }

        elevatorqueue.putInInstructionBox(new Instruction(tempDirection,tempFloorNumber));
        acknowledgeRequest(requestToHandle);
        System.out.println("Request handled");
    }
    public boolean hasPendingRequests() {
        return elevatorqueue.isRequestBoxEmpty();
    }


    @Override
    public void run(){

        while(true){
            // handleRequest();
            request();
        }
    }

}
