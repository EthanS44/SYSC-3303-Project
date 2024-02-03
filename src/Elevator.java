import java.time.*;

import static java.lang.Thread.sleep;

public class Elevator implements Runnable {

    //Elevators varaibles which include the ID, the shared Queue, a stopped/running status, the current floor and
    //a status variable to see if the door is open/closed
    private final int elevatorID;
    private ElevatorQueue elevatorqueue;
    private boolean stopped;
    private int currentFloor;
    private boolean doorOpen;


    /**
     * Constructor for Elevator
     * @param elevatorID - Takes the elevators ID (Cart #)
     * @param queue - Takes a shared queue between schedular, elevator, and floor
     */
    public Elevator(int elevatorID, ElevatorQueue queue){
        this.elevatorID = elevatorID;
        this.elevatorqueue = queue;
        currentFloor = 1;
        doorOpen = true;
        System.out.println("Elevator " + elevatorID + " created\n");
    }
    /**
     * This method returns the current floor
     * @return int
     */
    public int getCurrentFloor(){
        return currentFloor;
    }

    /**
     * This method returns true if the door is opened and false otherwise
     * @return boolean
     */
    public boolean isDoorOpen(){
        return doorOpen;
    }

    /**
     * Getter for the elevator ID
     * @return - Elevator ID (Cart #)
     */
    public int getElevatorID() {
        return elevatorID;
    }

    /**
     * Getter for the status of the Cart to see if it's stopped or not
     * @return True = Stopped, False = Running
     */
    public boolean isStopped(){
        return stopped;
    }

    /**
     * When the button is pushed this function will put a new request into the requestBox in the shared queue
     * @param buttonID - Takes a buttonID as the parameter which signifes the cart being used
     */
    public void buttonPushed(int buttonID){
        System.out.println("Button "+buttonID+ " pushed!");
        LocalDateTime currentTime = LocalDateTime.now();
        //int buttonID = 5; // can be changed ofc
        Request request = new Request(true, currentTime, elevatorID, buttonID, currentFloor);
        elevatorqueue.putInRequestBox(request);
        System.out.println("Request sent!");
    }

    /**
     * Goes to the floor that is requested in the param
     * @param newFloor - This is the floor you want the Elevator to go to
     */
    public void goToFloor(int newFloor){
        //int newFloor = instruction.getFloorNumber();

        while(newFloor != currentFloor) {
            //If statement for the elevator to go up
            if(newFloor > currentFloor) {
                System.out.println("Elevator " + elevatorID + " Going to floor " + newFloor + " Current floor " + currentFloor + "\n");
                currentFloor += 1;
            }
            //If statement for the elevator to go down
            else if (newFloor < currentFloor){
                System.out.println("Elevator " + elevatorID + " Going to floor " + newFloor + " Current floor " + currentFloor + "\n");
                currentFloor -= 1;
            }
        }
        System.out.println("Arrived at floor " + currentFloor);
    }

    /**
     * This opens the door of the elevator
     */
    public void openDoor(){
        System.out.println("Elevator " + elevatorID + " opening doors\n");
        doorOpen = true;
    }

    /**
     * This closes the door of the elevator
     */
    public void closeDoor(){
        System.out.println("Elevator " + elevatorID + " closing doors\n");
        doorOpen = false;
    }

    /**
     * This takes the instruction and decides what floor to go to
     */
    public void handleInstruction(){
        Instruction instructionToHandle = elevatorqueue.getFromInstructionBox();

        goToFloor(instructionToHandle.getFloorNumber());

        openDoor();

        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt(); // Reset interrupt status
            System.out.println("Failed to handle instruction" );
        }

        closeDoor();
        System.out.println("INSTRUCTION HANDLED");
    }


    /**
     * Constant loop that continously handles any instructions fed to the elevator by the schedular
     */
    @Override
    public void run(){
        buttonPushed(5);
        while(true){
            handleInstruction();
        }
    }

}
