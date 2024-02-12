import java.time.*;
import java.util.ArrayList;

import static java.lang.Thread.sleep;
class ElevatorWaiting implements ElevatorState {
    @Override
    public void handle(Elevator elevator){
        while(elevator.getCurrentFloor() == elevator.getNextFloor()){
            // wait for the next floor to be different from the current one
        }
        elevator.setCurrentState(new ElevatorMoving());
    }
}
class ElevatorMoving implements ElevatorState {
    @Override
    public void handle(Elevator elevator){
        // need to put something here to get an instruction from the instruction box

        // store current floor
        int oldFloor = elevator.getCurrentFloor();

        // go to next floor
        while(elevator.getNextFloor() != elevator.getCurrentFloor()) {
            //If statement for the elevator to go up
            if(elevator.getNextFloor() > elevator.getCurrentFloor()) {
                System.out.println("Elevator " + elevator.getElevatorID() + " Going to floor " + elevator.getNextFloor() + " Current floor " + elevator.getCurrentFloor() + "\n");
                elevator.setCurrentFloor(elevator.getCurrentFloor() + 1);
            }
            //If statement for the elevator to go down
            else if (elevator.getNextFloor() < elevator.getCurrentFloor()){
                System.out.println("Elevator " + elevator.getElevatorID() + " Going to floor " + elevator.getNextFloor() + " Current floor " + elevator.getCurrentFloor() + "\n");
                elevator.setCurrentFloor(elevator.getCurrentFloor() - 1);
            }
        }
        System.out.println("Arrived at floor " + elevator.getCurrentFloor());

        // set state to door handling
        elevator.setCurrentState(new ElevatorHandlingDoor());
    }
}
class ElevatorHandlingDoor implements ElevatorState{
    @Override
    public void handle(Elevator elevator){
        // wait for elevator to be stopped
        while(!elevator.isStopped()){
        }

        // open door
        System.out.println("Elevator " + elevator.getElevatorID() + " opening doors\n");
        elevator.setDoorOpen(true);

        // sleep between open and close
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt(); // Reset interrupt status
            System.out.println("Failed to handle instruction" );
        }

        // close door
        System.out.println("Elevator " + elevator.getElevatorID() + " closing doors\n");
        elevator.setDoorOpen(false);

        // Sets state to waiting
        elevator.setCurrentState(new ElevatorWaiting());
    }
}

public class Elevator implements Runnable {
    private final int elevatorID;
    private ElevatorQueue elevatorqueue;
    private boolean stopped;
    private int currentFloor;
    private int nextFloor;
    private boolean doorOpen;
    private ElevatorState currentState;
    private ArrayList<ElevatorButton> buttonList;
    public Elevator(int elevatorID, ElevatorQueue queue){
        this.elevatorID = elevatorID;
        this.elevatorqueue = queue;
        this.buttonList = new ArrayList<ElevatorButton>();
        this.currentFloor = 1;
        this.doorOpen = false;
        this.stopped = true;
        this.currentState = new ElevatorWaiting();
        System.out.println("Elevator " + elevatorID + " created\n");

        // adds 7 buttons to the elevator's button list
        for (int i = 1; i <= 7; i++){
            ElevatorButton newButton = new ElevatorButton(i);
            addButton(newButton);
            newButton.setElevator(this);
        }
    }
    public int getElevatorID() {
        return elevatorID;
    }
    public boolean isStopped(){
        return stopped;
    }
    public int getCurrentFloor() { return currentFloor; }
    public void setCurrentFloor(int newFloor) { currentFloor = newFloor; }
    public int getNextFloor() { return nextFloor; }
    public void setNextFloor(int newFloor) {nextFloor = newFloor;}
    public boolean isDoorOpen() { return doorOpen; }
    public void setDoorOpen(boolean open) { doorOpen = open; }
    public ElevatorState getCurrentState() { return currentState; }
    public void setCurrentState(ElevatorState state) { this.currentState = state;}
    public void addButton(ElevatorButton button){ this.buttonList.add(button);}
    public void removeButton(ElevatorButton button){ this.buttonList.remove(button); }
    public ArrayList<ElevatorButton> getButtonList(){ return this.buttonList; }
    public ElevatorQueue getElevatorQueue() {return this.elevatorqueue; }

    public void request() { this.currentState.handle(this);}

    @Override
    public void run(){
        for (int i = 1; i <= 7; i++){
            this.buttonList.get(i).pushButton();
        }
        while(true){
            request();
        }
    }

}
