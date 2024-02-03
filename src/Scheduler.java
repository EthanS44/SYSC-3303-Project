import java.lang.reflect.Array;
import java.util.ArrayList;

public class Scheduler implements Runnable {
    private static final int numberOfFloors = 7;
    private final ElevatorQueue elevatorqueue;


    /**
     * Constructor for Scheduler class
     * @param queue - Shared queue
     */
    public Scheduler(ElevatorQueue queue){
        this.elevatorqueue = queue;
        System.out.println("Scheduler created\n");
    }


    /**
     * acknowledges request
     * @param request - request to be acknowledged
     */
    public void acknowledgeRequest(Request request){
        request.setRequestAcknowledged(true);
    }


    /**
     * Handles request by creating an instruction out of it
     */
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

    /**
     * Constantly handling any requests in the requestBox
     */
    @Override
    public void run(){
        while(true){
            handleRequest();
        }
    }
}
