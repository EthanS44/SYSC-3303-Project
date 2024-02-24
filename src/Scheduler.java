import java.lang.reflect.Array;
import java.util.ArrayList;

public class Scheduler implements Runnable {
    private static final int numberOfFloors = 7;
    private final ElevatorQueue elevatorqueue;
    private schedulerState currentState; //new
    private ArrayList<Floor> floors;

class SchedulerWaiting implements schedulerState {
    @Override
    public void handle(Scheduler scheduler){
        System.out.println("Is request box empty: " + scheduler.noPendingRequests());

        if (!scheduler.noPendingRequests()){
            scheduler.setCurrentState(new HandlingRequest());
            System.out.println("Scheduler state changed to HandlingRequest\n");
        }

        if (!scheduler.noPendingResponses()){
            Response response = scheduler.elevatorqueue.getFromResponseBox();
            System.out.println("Scheduler received response from floor " + response.getFloorNumber() + "\n");
        }
    }

} //new
class HandlingRequest implements schedulerState{
    @Override
    public void handle(Scheduler scheduler){
        scheduler.handleRequest();
        scheduler.setCurrentState(new SchedulerWaiting());
        System.out.println("Scheduler state changed to SchedulerWaiting\n");
    }
}

    public Scheduler(ElevatorQueue queue){
        this.elevatorqueue = queue;
        this.currentState = new SchedulerWaiting(); //new
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

    public void handleRequest(){
        Request requestToHandle = elevatorqueue.getFromRequestBox();
        System.out.println("Request received by scheduler, now handling\n");

        //This is for gathering information about what will be in the instruction
        boolean tempDirection;
        int tempFloorNumber;

        //If it's an elevator it has its own set of rules
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
        System.out.println("Scheduler: Request handled");
    }

    public boolean noPendingRequests() {
        return elevatorqueue.isRequestBoxEmpty();
    }

    public boolean noPendingResponses(){
        return elevatorqueue.isResponseBoxEmpty();
    }


    @Override
    public void run(){

        while(true){
            // handleRequest();
            request();

            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt(); // Reset interrupt status
                System.out.println("Failed to handle instruction" );
            }
        }
    }
}
