import java.io.IOException;
import java.net.*;
import java.time.*;
import java.util.ArrayList;
import static java.lang.Thread.sleep;
class ElevatorWaiting implements ElevatorState {
    @Override
    public void handle(Elevator elevator) {
        System.out.println("Elevator: Changed to Waiting State");
        // get instruction from box and set new floor
        // Instruction instruction = elevator.getElevatorQueue().getFromInstructionBox();
        try {
            elevator.receiveInstructionFromScheduler();
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Elevator " + elevator.getElevatorID() + ": Error executing Instruction");
        }
        Instruction instruction = elevator.getInstructionBox().get(0);
        elevator.setNextFloor(instruction.getFloorNumber());

        elevator.setCurrentState(new ElevatorMoving());
    }
}
class ElevatorMoving implements ElevatorState {
    @Override
    public void handle(Elevator elevator){
        System.out.println("Elevator: Changed to ElevatorMoving State");
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
        System.out.println("Elevator: Changed to HandlingDoor State");

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

        // Send response packet to Scheduler
        //elevator.getElevatorQueue().putInResponseBox(new Response(elevator.getCurrentFloor()));
        elevator.sendResponse(new Response(elevator.getCurrentFloor()));

        // Sets state to waiting
        elevator.setCurrentState(new ElevatorWaiting());
    }
}

public class Elevator implements Runnable {

    //Elevators variables which include the ID, the shared Queue, a stopped/running status, the current floor and
    //a status variable to see if the door is open/closed
    private final int elevatorID;
    //private ElevatorQueue elevatorqueue;
    private boolean stopped;
    private int currentFloor;
    private int nextFloor;
    private boolean doorOpen;
    private ElevatorState currentState;
    DatagramSocket sendSocket;
    DatagramSocket receiveSocket;
    private ArrayList<ElevatorButton> buttonList;
    private ArrayList<Instruction> instructionBox;

    /**
     * Constructor for Elevator
     * @param elevatorID - Takes the elevators ID (Cart #)
     * @param sendPort - Port number the elevator sends from
     * @param receivePort - Port the elevator receives from
     */
    public Elevator(int elevatorID, /*ElevatorQueue queue,*/ int sendPort, int receivePort){
        this.elevatorID = elevatorID;
        //this.elevatorqueue = queue;
        this.currentFloor = 1;
        this.nextFloor = 1;
        this.doorOpen = false;
        this.stopped = true;
        this.currentState = new ElevatorWaiting();
        this.buttonList = new ArrayList<>();
        // Set up sockets
        try {
            this.sendSocket = new DatagramSocket(sendPort);
        } catch (SocketException se) {
            se.printStackTrace();
            System.exit(1);
        }
        try {
            this.receiveSocket = new DatagramSocket(receivePort);
        } catch (SocketException se) {
            se.printStackTrace();
            System.exit(1);
        }
        System.out.println("Elevator " + elevatorID + " created\n");

        // adds 7 buttons to the elevator's button list
        for (int i = 1; i <= 7; i++){
            ElevatorButton newButton = new ElevatorButton(i);
            addButton(newButton);
            newButton.setElevator(this);
        }
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
     * This method returns the current floor
     * @return int
     */
    public int getCurrentFloor(){
        return currentFloor;
    }

    /**
     * This method sets the current floor to the specified int
     * @param newFloor represents the next floor to be visited
     */
    public void setCurrentFloor(int newFloor) { currentFloor = newFloor; }

    /**
     * Returns the next floor to be visited
     * @return int
     */
    public int getNextFloor() { return nextFloor; }

    /**
     * Sets the next floor to be visited to the specified floor
     * @param newFloor represents the floor to be visited
     */
    public void setNextFloor(int newFloor) { nextFloor = newFloor;}
    /**
     * This method returns true if the door is opened and false otherwise
     * @return boolean
     */
    public boolean isDoorOpen(){ return doorOpen; }

    /**
     * this method sets the doorOpen field to the specified boolean value
     * @param open represents the value to set the field to
     */
    public void setDoorOpen(boolean open) { doorOpen = open; }

    /**
     * This method returns the current state of the elevator
     * @return ElevatorState
     */
    public ElevatorState getCurrentState() { return currentState; }

    /**
     * Sets current state to the specified ElevatorState
     * @param state represents the state to change the current state to
     */
    public void setCurrentState(ElevatorState state) { this.currentState = state;}

    /**
     * Adds ElevatorButton to the inside of the elevator
     * @param button specifies the button to be added
     */
    public void addButton(ElevatorButton button){ this.buttonList.add(button);}

    /**
     * Removes ElevatorButton from the elevator
     * @param button specifies the button to be removed
     */
    public void removeButton(ElevatorButton button){ this.buttonList.remove(button); }

    /**
     * Returns the list of buttons inside the elevator
     * @return ArrayList<ElevatorButtons>
     */
    public ArrayList<ElevatorButton> getButtonList(){ return this.buttonList; }

    /**
     * Returns the queue that the Elevator takes instructions from
     * @return ElevatorQueue
     */
    // public ElevatorQueue getElevatorQueue() {return this.elevatorqueue; }
    public ArrayList<Instruction> getInstructionBox() { return this.instructionBox; }

    /**
     * Executes code section in the current state
     */
    public void request() { this.currentState.handle(this); }
    /**
     * Constant loop that continuously handles any instructions fed to the elevator by the scheduler
     */
    public void sendRequest(Request requestToSend){
        try {
            // data is the byte array that represents the Request
            byte[] data = requestToSend.toByteArray(requestToSend);

            // Send packet to Scheduler receive socket
            DatagramPacket packetToSend = new DatagramPacket(data, data.length, InetAddress.getLocalHost(), 41);
            sendSocket.send(packetToSend);
            System.out.println("Elevator " + this.elevatorID + " Sent Request from Button " + requestToSend.getIndexNumber());
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(1);
        }
    }
    public void sendResponse(Response responseToSend){
        try {
            // data is the byte array that represents the Response
            byte[] data = responseToSend.toByteArray(responseToSend);

            // Send packet to Scheduler response receive socket
            DatagramPacket packetToSend = new DatagramPacket(data, data.length, InetAddress.getLocalHost(), 42);
            sendSocket.send(packetToSend);
            System.out.println("Elevator " + this.elevatorID + " Sent Response to Scheduler");
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(1);
        }
    }
    public void receiveInstructionFromScheduler() throws IOException, ClassNotFoundException {
        byte[] data = new byte[100];
        DatagramPacket packetToReceive = null;

        // packet to receive the data
        packetToReceive = new DatagramPacket(data, data.length);

        // receive data
        try {
            receiveSocket.receive(packetToReceive);
            System.out.println("Elevator " + this.elevatorID + " Received Instruction from Scheduler");
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(1);
        }

        // Turn data into instruction
        Instruction instruction =  Instruction.toInstruction(packetToReceive.getData());
        this.instructionBox.add(instruction);
    }
    @Override
    public void run(){

        // Push buttons 4, 7 and 2, then handle the requests
        this.buttonList.get(6).pushButton();

        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt(); // Reset interrupt status
            System.out.println("Failed to handle instruction" );
        }

        this.buttonList.get(2).pushButton();

        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt(); // Reset interrupt status
            System.out.println("Failed to handle instruction" );
        }

        this.buttonList.get(1).pushButton();

        while(true){
            request();
        }
    }

}