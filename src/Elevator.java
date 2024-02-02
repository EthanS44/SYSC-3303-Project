import java.time.*;

import static java.lang.Thread.sleep;

public class Elevator implements Runnable {

    private final int elevatorID;
    private ElevatorQueue elevatorqueue;
    private boolean stopped;
    private int currentFloor;
    private boolean doorOpen;


    public Elevator(int elevatorID, ElevatorQueue queue){
        this.elevatorID = elevatorID;
        this.elevatorqueue = queue;
        currentFloor = 1;
        doorOpen = true;
        System.out.println("Elevator " + elevatorID + " created\n");
    }

    public int getElevatorID() {
        return elevatorID;
    }

    public boolean isStopped(){
        return stopped;
    }

    public void buttonPushed(int buttonID){
        System.out.println("Button "+buttonID+ " pushed!");
        LocalDateTime currentTime = LocalDateTime.now();
        //int buttonID = 5; // can be changed ofc
        Request request = new Request(true, currentTime, elevatorID, buttonID, currentFloor);
        elevatorqueue.putInRequestBox(request);
        System.out.println("Request sent!");
    }

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

    public void openDoor(){
        System.out.println("Elevator " + elevatorID + " opening doors\n");
        doorOpen = true;
    }

    public void closeDoor(){
        System.out.println("Elevator " + elevatorID + " closing doors\n");
        doorOpen = false;
    }

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


    @Override
    public void run(){
        buttonPushed(5);
        while(true){
            handleInstruction();
        }
    }

}
