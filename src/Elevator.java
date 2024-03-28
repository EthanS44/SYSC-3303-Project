import java.io.IOException;
import java.net.*;
import java.time.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.TimeoutException;

import static java.lang.Thread.sleep;
class ElevatorWaiting implements ElevatorState {
    @Override
    public void handle(Elevator elevator) {

        if(elevator.getDirectionLamp().isLampOn()) {
            elevator.getDirectionLamp().turnOffLamp();
        }

        // receive instruction packet from scheduler
        for (int i = 0; i < 10; i++) {
            try {
                elevator.receiveInstructionFromScheduler();
            } catch (IOException | ClassNotFoundException e) {
                System.out.println("Elevator " + elevator.getElevatorID() + ": Error executing Instruction");
            }
        }


        // get instruction from box and set new floor
            if (!elevator.getInstructionBox().isEmpty()) { // we have an instruction
                    elevator.setNextFloor(elevator.calculateNextFloor());
                    System.out.println("Elevator " + elevator.getElevatorID() + ": Next floor set to " + elevator.calculateNextFloor());
                    //Turn on direction lamp
                    elevator.getDirectionLamp().turnOnLamp(elevator.getMotor().getCurrentDirection());
                    elevator.setCurrentState(new ElevatorMoving());
            }

        }
    }


class ElevatorMoving implements ElevatorState {
    @Override
    public void handle(Elevator elevator) {
        System.out.println("Elevator " + elevator.getElevatorID() + ": Changed to ElevatorMoving State");
        elevator.goToFloor(elevator);
    }
}
class ElevatorHandlingDoor implements ElevatorState {
    @Override
    public void handle(Elevator elevator) {
        System.out.println("Elevator " + elevator.getElevatorID() + ": Changed to HandlingDoor State");

        // Assuming the elevator is already stopped when this state is entered
        elevator.setDoorOpen(true); // Simulating door opening
        System.out.println("Elevator " + elevator.getElevatorID() + " opening doors.");

        // Check if the door opened successfully
        if (!elevator.checkDoorOpenedSuccessfully()) {
            // The door didn't open successfully, handle the stuck door
            elevator.handleStuckDoor();
        } else {
            // If the door opened successfully, proceed with normal operation
            try {
                Thread.sleep(3000); // Simulate the door being open for a period
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt(); // Handle thread interruption
                System.out.println("Elevator " + elevator.getElevatorID() + ": Interrupted while door open.");
            }

            elevator.setDoorOpen(false); // Simulating door closing
            System.out.println("Elevator " + elevator.getElevatorID() + " closing doors.");
        }

        // remove latest instruction from box
        elevator.getInstructionBox().removeIf(instruction -> instruction.getFloorNumber() == elevator.getCurrentFloor());


        // Transition back to waiting state or any other appropriate state
        elevator.setCurrentState(new ElevatorWaiting());
        System.out.println("Elevator " + elevator.getElevatorID() + ": Changed to Waiting State");
    }
}

public class Elevator implements Runnable {

    //Elevators variables which include the ID, the shared Queue, a stopped/running status, the current floor and
    //a status variable to see if the door is open/closed
    private final int elevatorID;
    private boolean stopped;
    private int currentFloor;
    private int nextFloor;
    private boolean doorOpen;
    private ElevatorState currentState;
    DatagramSocket sendSocket;
    DatagramSocket receiveSocket;
    DatagramSocket acknowledgementSocket;
    private ArrayList<ElevatorButton> buttonList;
    private ArrayList<ArrivalSensor> arrivalSensors;
    private ArrayList<Instruction> instructionBox;
    private ElevatorMotor motor;
    private DirectionLamp directionLamp;
    private boolean doorOperationSuccess = true; // Assume door operation is successful by default
    private int doorRetryCounter = 0; // Counter for door operation attempts


    /**
     * Simulates checking if the elevator door opened successfully.
     * @return true if the door operation was successful, false if the door is stuck.
     */
    public boolean checkDoorOpenedSuccessfully() {
        // Simulate a scenario where the door fails to open correctly after 3 attempts
        if (doorRetryCounter >= 3) {
            doorOperationSuccess = false;
        }
        return doorOperationSuccess;
    }


    /**
     * Handles scenarios where the elevator door is stuck open.
     */
    public void handleStuckDoor() {
        System.out.println("Elevator " + this.elevatorID + ": Door is stuck open. Attempting to resolve.");

        // Increment the retry counter for door operation
        doorRetryCounter++;

        // Attempt to resolve the door issue by simulating a retry operation
        if (doorRetryCounter < 3) {
            System.out.println("Elevator " + this.elevatorID + ": Attempting to close the door, attempt " + doorRetryCounter);
            // Simulate that the door operation might succeed on a retry
            doorOperationSuccess = true;
        } else {
            // If retries exceed a threshold, log the error and take the elevator out of service for maintenance
            System.out.println("Elevator " + this.elevatorID + ": Door remains stuck after multiple attempts. Elevator taken out of service for maintenance.");
            this.stopped = true; 
            
        }
    }

    /**
     * Constructor for Elevator
     * @param elevatorID - Takes the elevators ID (Cart #)
     * @param sendPort - Port number the elevator sends from
     * @param receivePort - Port the elevator receives from
     */
    public Elevator(int elevatorID, /*ElevatorQueue queue,*/ int sendPort, int receivePort, int acknowledgmentPort){
        this.elevatorID = elevatorID;
        //this.elevatorqueue = queue;
        this.currentFloor = 1;
        this.nextFloor = 1;
        this.doorOpen = false;
        this.stopped = true;
        this.currentState = new ElevatorWaiting();
        this.buttonList = new ArrayList<>();
        this.arrivalSensors = new ArrayList<ArrivalSensor>();
        this.instructionBox = new ArrayList<Instruction>();
        this.motor = new ElevatorMotor();
        this.directionLamp = new DirectionLamp(this);
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
        try {
            this.acknowledgementSocket = new DatagramSocket(acknowledgmentPort);
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

        // adds an arrival sensor for each floor
        for (int i = 1; i <= 7; i++){
            ArrivalSensor newSensor = new ArrivalSensor(i, this);
            arrivalSensors.add(newSensor);
        }
    }
    /**
     * Getter for the elevator ID
     * @return - Elevator ID (Cart #)
     */
    public int getElevatorID() {
        return elevatorID;
    }

    public ElevatorMotor getMotor(){
        return motor;
    }

    /**
     * Getter for lampDirection variable
     * @return
     */
    public DirectionLamp getDirectionLamp() {
        return directionLamp;
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

    public int calculateNextFloor() {
        int direction = motor.getCurrentDirection();
        int floorToGo = instructionBox.get(0).getFloorNumber();

        if (direction == 1){
            for (Instruction instruction: instructionBox) {
                if (instruction.getFloorNumber() > floorToGo) {
                    floorToGo = instruction.getFloorNumber();
                }
            }
        } else {
            for (Instruction instruction: instructionBox) {
                if (instruction.getFloorNumber() < floorToGo) {
                    floorToGo = instruction.getFloorNumber();
                }
            }
        }
        // if instruction box is only of size 1, correct the motor direction
        if (instructionBox.size() == 1 && floorToGo < currentFloor){
            motor.setDirection(0);
        }
        else{
            motor.setDirection(1);
        }
        return floorToGo;
    }

    public void goToFloor(Elevator elevator){
        // go to next floor
        elevator.getMotor().startMotor();

        while(elevator.getNextFloor() != elevator.getCurrentFloor()) {
            //If statement for the elevator to go up
            if(elevator.getNextFloor() > elevator.getCurrentFloor()) {
                elevator.getMotor().setDirection(1);
                System.out.println("Elevator " + elevator.getElevatorID() + " Going to floor " + elevator.getNextFloor() + " Current floor " + elevator.getCurrentFloor());

                // handle arrival sensor of current floor
                if(elevator.getArrivalSensors().get(elevator.getCurrentFloor() -1).handleArrivalSensor()){
                    //sleep for time it takes to go to next floor
                    try {
                        Thread.sleep(2602);
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt(); // Reset interrupt status
                        System.out.println("Failed to handle instruction");
                    }
                    //increment floor
                    elevator.setCurrentFloor(elevator.getCurrentFloor() + 1);
                    break;
                }

                //sleep for time it takes to go to next floor
                try {
                    Thread.sleep(2602);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt(); // Reset interrupt status
                    System.out.println("Failed to handle instruction");
                }

                //increment floor
                elevator.setCurrentFloor(elevator.getCurrentFloor() + 1);
                //send scheduler current floor and direction
                elevator.sendResponse(false);

            }
            //If statement for the elevator to go down
            else if (elevator.getNextFloor() < elevator.getCurrentFloor()){
                elevator.getMotor().setDirection(0);
                System.out.println("Elevator " + elevator.getElevatorID() + " Going to floor " + elevator.getNextFloor() + " Current floor " + elevator.getCurrentFloor());


                // handle arrival sensor of current floor
                if(elevator.getArrivalSensors().get(elevator.getCurrentFloor() - 1).handleArrivalSensor()){
                    //sleep for time it takes to go to next floor
                    try {
                        Thread.sleep(2602);
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt(); // Reset interrupt status
                        System.out.println("Failed to handle instruction");
                    }
                    //increment floor
                    elevator.setCurrentFloor(elevator.getCurrentFloor() - 1);
                    break;
                }

                //sleep for time it takes to go to next floor
                try {
                    Thread.sleep(2602);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt(); // Reset interrupt status
                    System.out.println("Failed to handle instruction");
                }

                //increment floor
                elevator.setCurrentFloor(elevator.getCurrentFloor() - 1);
                //send scheduler current floor and direction
                elevator.sendResponse(false);
            }
        }
        System.out.println("Elevator " + elevator.getElevatorID() + " Arrived at floor " + elevator.getCurrentFloor());
        Instruction instructionToRemove = elevator.isFloorInQueue(elevator.getCurrentFloor());
        elevator.getInstructionBox().remove(instructionToRemove);

        // stop elevator motor
        elevator.getMotor().stop();
        if(currentFloor == nextFloor) {
            elevator.getMotor().changeDirection();
        }
        elevator.sendResponse(true);

        // set state to door handling
        elevator.setCurrentState(new ElevatorHandlingDoor());
    }
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
    public ArrayList<ArrivalSensor> getArrivalSensors() { return this.arrivalSensors; }

    public Instruction isFloorInQueue(int floorNumber){
        for (Instruction instruction: this.instructionBox){
            if (instruction.getFloorNumber() == floorNumber){
                return instruction; //return the matching instruction
            }
        }
        return null;
    }

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
            System.out.println("Elevator " + this.elevatorID + " Sent Request from Button " + requestToSend.getButtonId());
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(1);
        }
        //receive acknowledgment from scheduler after sending request
        this.receiveAcknowledgment();
    }

    public void receiveInstructionFromScheduler() throws IOException, ClassNotFoundException {
        byte[] data = new byte[200];
        DatagramPacket packetToReceive = null;

        // packet to receive the data
        packetToReceive = new DatagramPacket(data, data.length);

        // receive data
        try {
            receiveSocket.setSoTimeout(100);
            receiveSocket.receive(packetToReceive);
            System.out.println("Elevator " + this.elevatorID + " Received Instruction from Scheduler");
        } catch (SocketTimeoutException e) {
            //System.out.println("Timeout Exception"); // Handles SocketTimeoutException if no packet is received within the specified timeout
            return;
        } catch (IOException e) {
            //System.out.println("IOException"); // Handles IOException if any occurs during receive operation
            return;
        }

        // Turn data into instruction
        Instruction instruction =  Instruction.toInstruction(packetToReceive.getData());

        // add instruction to instructionbox if floor number is not already in box
        if (isFloorInQueue(instruction.getFloorNumber()) == null){
            this.instructionBox.add(instruction);
        }
    }

    public void receiveAcknowledgment(){
        byte[] data = new byte[200];
        DatagramPacket acknowledgementPacket = null;

        // packet to receive the data
        acknowledgementPacket = new DatagramPacket(data, data.length);

        // receive data
        try {
            acknowledgementSocket.receive(acknowledgementPacket);
            System.out.println("Elevator " + this.elevatorID + " received acknowledgment from scheduler");
        } catch (IOException e) {
            System.out.println("IOException");
        }
    }

    /**
     * Sends a response to the Scheduler
     */
    public void sendResponse(boolean stoppingAtFloor){

        Response response = new Response(currentFloor, getMotor().getCurrentDirection(), elevatorID, stoppingAtFloor);
        DatagramPacket newPacket = null;
        try {

            // Serialize instruction object to bytes
            byte[] responseBytes = response.toByteArray(response);

            // Create UDP packet with instruction bytes, destination IP, and port

            newPacket = new DatagramPacket(responseBytes , responseBytes.length, InetAddress.getLocalHost(), 70);
        } catch (UnknownHostException e) {
            e.printStackTrace();
            System.exit(1);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        // Sending response packet
        try {
            sendSocket.send(newPacket);
            System.out.println("Elevator " + this.elevatorID + " Sent Response to Scheduler");
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(1);
        }
    }
    @Override
    public void run(){

        // Push button 7
        if (elevatorID == 1) {
            this.buttonList.get(6).pushButton();
            this.buttonList.get(3).pushButton();
        }
        if (elevatorID == 2) {
            this.buttonList.get(4).pushButton();
            this.buttonList.get(2).pushButton();
        }

        while(true){
            request();

        }
    }

}