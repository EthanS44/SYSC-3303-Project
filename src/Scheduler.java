import java.io.IOException;
import java.lang.reflect.Array;
import java.net.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

//thought process since we delete the elevator queue the methods that depended on it how will they be handled
//Also would the scheduler send response back to the floor
//receive request will be called to collect packet then change to request add to request box. it will be
// handles converted to instruction from instruction to bytes to be sent over to Elevator. Response will be
// received not sure if it should be sent to the floor or anything that requested something

public class Scheduler implements Runnable {
    private static final int numberOfFloors = 7;
    //private final ElevatorQueue elevatorqueue;
    private schedulerState currentState; //new
    private ArrayList<Request> requestBox;
    private ArrayList<Response> responseBox;
    private int elevatorSendPort;
    private int elevator1Position, elevator2Position, elevator3Position, elevator4Position;
    private int elevator1Direction, elevator2Direction, elevator3Direction, elevator4Direction; // 1 is up 0 is down
    private int elevator1Enabled, elevator2Enabled, elevator3Enabled, elevator4Enabled;


    DatagramSocket receiveSocket;
    DatagramSocket sendReceiveSocket;
    DatagramSocket responseSocket;


    public Scheduler(){
        this.requestBox = new ArrayList<Request>();
        this.responseBox = new ArrayList<Response>();
        //this.elevatorqueue = queue;
        this.currentState = new SchedulerWaiting(); //new
        System.out.println("Scheduler created\n");

        //set elevator starting positions to floor 1
        this.elevator1Position = 1;
        this.elevator2Position = 1;
        this.elevator3Position = 1;
        this.elevator4Position = 1;

        //set elevator starting directions to up
        this.elevator1Direction = 1;
        this.elevator2Direction = 1;
        this.elevator3Direction = 1;
        this.elevator4Direction = 1;

        // creates 2 sockets
        try {
            receiveSocket = new DatagramSocket(41);
        } catch (SocketException se) {
            se.printStackTrace();
            System.exit(1);
        }
        try {
            sendReceiveSocket = new DatagramSocket(40);
        } catch (SocketException se) {
            se.printStackTrace();
            System.exit(1);
        }
        try {
            responseSocket = new DatagramSocket(70);
        } catch (SocketException se) {
            se.printStackTrace();
            System.exit(1);
        }
        this.elevatorSendPort = 52;
    }

    public Scheduler(int socketNum1, int socketNum2, int socketNum3){
        this.requestBox = new ArrayList<Request>();
        this.responseBox = new ArrayList<Response>();
        //this.elevatorqueue = queue;
        this.currentState = new SchedulerWaiting(); //new
        System.out.println("Scheduler created\n");

        //set elevator starting positions to floor 1
        this.elevator1Position = 1;
        this.elevator2Position = 1;
        this.elevator3Position = 1;
        this.elevator4Position = 1;

        //set elevator starting directions to up
        this.elevator1Direction = 1;
        this.elevator2Direction = 1;
        this.elevator3Direction = 1;
        this.elevator4Direction = 1;

        // creates 2 sockets
        try {
            receiveSocket = new DatagramSocket(socketNum1);
        } catch (SocketException se) {
            se.printStackTrace();
            System.exit(1);
        }
        try {
            sendReceiveSocket = new DatagramSocket(socketNum2);
        } catch (SocketException se) {
            se.printStackTrace();
            System.exit(1);
        }
        try {
            responseSocket = new DatagramSocket(socketNum3);
        } catch (SocketException se) {
            se.printStackTrace();
            System.exit(1);
        }
        this.elevatorSendPort = 52;
    }

    class SchedulerWaiting implements schedulerState {
        @Override
        public void handle(Scheduler scheduler){

            scheduler.receiveResponse();
            receiveRequest();


            if (!scheduler.noPendingRequests()){
                scheduler.setCurrentState(new HandlingRequest());
                System.out.println("Scheduler state changed to HandlingRequest\n");
            }
        }

    } //new
    class HandlingRequest implements schedulerState{
        @Override
        public void handle(Scheduler scheduler){
            //update elevator positions
            scheduler.receiveResponse();
            System.out.println("calling handle request");
            scheduler.handleRequest();
            scheduler.setCurrentState(new SchedulerWaiting());
            System.out.println("Scheduler state changed to SchedulerWaiting\n");
        }
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
        //Request requestToHandle = elevatorqueue.getFromRequestBox();
        // System.out.println("Request received by scheduler, now handling\n");
        // Check if there are requests in the requestBox
        receiveResponse();

        if (requestBox.isEmpty()) {
            System.out.println("No requests to handle.");
            return;
        }

        // not sure if it should Iterate over the ArrayList of requests
        // for (Request requestToHandle : requestBox) {
        Request requestToHandle = requestBox.get(0);
        System.out.println("Scheduler handling request from request box\n");

        //This is for gathering information about what will be in the instruction
        boolean tempDirection;
        int tempFloorNumber;

        //If it's an elevator it has its own set of rules
        if (requestToHandle.isElevator()) {
            if (requestToHandle.getCurrentFloor() > requestToHandle.getButtonId()) {
                tempDirection = false; // Down = false
            } else {
                tempDirection = true; // Up = True
            }
            tempFloorNumber = requestToHandle.getButtonId(); // Copies the floor number to the temp variable

        } else {
            if (requestToHandle.getCurrentFloor() > requestToHandle.getIndexNumber()) {
                tempDirection = false; // Down = False
            } else {
                tempDirection = true; // Up = True
            }
            tempFloorNumber = requestToHandle.getIndexNumber();
        }
        receiveResponse();
        //Convert to packet and send instruction
        sendInstructionToElevator(new Instruction(tempDirection, tempFloorNumber));
        System.out.println("Scheduler: Request handled");
        // remove request from requestBox
        requestBox.remove(requestToHandle);

        receiveResponse();
    }

    public boolean noPendingRequests() {
        return requestBox.isEmpty();
    }

    public boolean noPendingResponses(){
        return responseBox.isEmpty();
    }

    /**This method Converts instruction to packet to send to elevator
     *
     * @param instruction
     */
    public void sendInstructionToElevator(Instruction instruction){
        receiveResponse();
        //decide which elevator should get the instruction
        int closestElevator = getClosestElevator(instruction.getFloorNumber());
        switch(closestElevator){
            case(1):
                elevatorSendPort = 50;
                break;
            case(2):
                elevatorSendPort = 51;
                break;
            case(3):
                elevatorSendPort = 52;
                break;
            case(4):
                elevatorSendPort = 53;
                break;
        }

        // send packet to Elevator
        try {
            // Serialize instruction object to bytes
            byte[] instructionBytes = instruction.toByteArray(instruction);
            // Create UDP packet with instruction bytes, destination IP, and port
            DatagramPacket packet = new DatagramPacket(instructionBytes, instructionBytes.length, InetAddress.getLocalHost(), elevatorSendPort); //its just sending to elevator 1 for now

            sendReceiveSocket.send(packet);
            System.out.println(packet.getData());

        } catch (NoRouteToHostException no){
            System.out.println("Failed to send Instruction to Elevator, sending again");
            sendInstructionToElevator(instruction); //send instruction again
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(1);
        }
        System.out.println("Scheduler: Instruction sent to Elevator " + closestElevator + ".\n");
    }

   public void receiveResponse(){ //updates elevator positions and directions and send to floor to turn off lamps and buttons

       // Receive response packet from Elevator
           byte[] data = new byte[200];
           DatagramPacket packetToReceive = new DatagramPacket(data, data.length);

           try {
               responseSocket.setSoTimeout(1);
               responseSocket.receive(packetToReceive);
           } catch (SocketTimeoutException e) {
               //System.out.println("Timeout Exception"); // Handles SocketTimeoutException if no packet is received within the specified timeout
               return;
           } catch (IOException e) {
               //System.out.println("IOException"); // Handles IOException if any occurs during receive operation
               return;
           }

           System.out.println("Scheduler Receiving response");
           //collect the received packet and convert it to response
           Response response;
           try {
               response = Response.toResponse(packetToReceive.getData());
           } catch (IOException | ClassNotFoundException e) {
               System.out.println("Scheduler: IOException");
               e.printStackTrace();
               return;
           }

           //update elevator directions and positions
           switch (response.getElevatorID()) {
               case (1):
                   elevator1Position = response.getFloorNumber();
                   elevator1Direction = response.getCurrentDirection();
                   break;
               case (2):
                   elevator2Position = response.getFloorNumber();
                   elevator2Direction = response.getCurrentDirection();
                   break;
               case (3):
                   elevator3Position = response.getFloorNumber();
                   elevator3Direction = response.getCurrentDirection();
                   break;
               case (4):
                   elevator4Position = response.getFloorNumber();
                   elevator4Direction = response.getCurrentDirection();
                   break;
           }
           System.out.println("Elevator " + response.getElevatorID() + " position updated to floor " + getElevatorPosition(response.getElevatorID()) + " and direction to " + getElevatorDirection(response.getElevatorID()));

           //relay response to floor if elevator is stopping there, this is to turn off floor buttons and lamps
           if (response.isStoppingAtFloor()){
               sendResponse(response);
           }
   }

   public void sendResponse(Response response){
       DatagramPacket newPacket = null;
       try {

           // Serialize instruction object to bytes
           byte[] responseBytes = response.toByteArray(response);

           // Create UDP packet with instruction bytes, destination IP, and port

           newPacket = new DatagramPacket(responseBytes , responseBytes.length, InetAddress.getLocalHost(), response.getFloorNumber());
       } catch (UnknownHostException e) {
           e.printStackTrace();
           System.exit(1);
       } catch (IOException e) {
           throw new RuntimeException(e);
       }

       // Sending response packet
       try {
           sendReceiveSocket.send(newPacket);
           System.out.println("Scheduler sent response to floor");
       } catch (IOException e) {
           e.printStackTrace();
           System.exit(1);
       }
   }


    /**
     * This method receives packet and Converts the packet to a request
     * The request will be added to the request box
     */
    public void receiveRequest (){

        // Create new packet to hold received data

        byte[] data = new byte[200];
        DatagramPacket packetToReceive = null;

        packetToReceive = new DatagramPacket(data, data.length);

        // Receive request packet
        try {
            receiveSocket.setSoTimeout(1);
            receiveSocket.receive(packetToReceive);
            System.out.println("\nScheduler: Request received");
        } catch (SocketTimeoutException e) {
            //System.out.println("Timeout Exception"); // Handles SocketTimeoutException if no packet is received within the specified timeout
            return;
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("IOException");
            return;
        }

        //convert the received packet to bytes.
        Request request;
        try {
            request = Request.toRequest(packetToReceive.getData());
        } catch (IOException io) {
            System.out.println("Scheduler: Empty or Invalid Request, IO Exception");
            io.printStackTrace();
            return;

        } catch (ClassNotFoundException e) {
            System.out.println("Scheduler: Empty or Invalid Request, Class not found");
            return;
        }

        //add request to request box
        requestBox.add(request);
        System.out.println("Scheduler: Request added to request box. Waiting to be handled.");

        //Send acknowledgment for the received request
        if (request.isElevator()) {
            int elevatorID = request.getElevatorFloorID();
            int sendPort = 0;
            switch(elevatorID){
                case(1):
                    sendPort = 60;
                    break;
                case(2):
                    sendPort = 61;
                    break;
                case(3):
                    sendPort = 62;
                    break;
                case(4):
                    sendPort = 63;
                    break;
            }
            try {
                byte[] data2 = new byte[200];
                DatagramPacket acknowledgementPacketToSend;
                acknowledgementPacketToSend = new DatagramPacket(data2, data2.length, InetAddress.getLocalHost(), sendPort);

                sendReceiveSocket.send(acknowledgementPacketToSend);
                System.out.println("Scheduler: Acknowledgement sent to elevator " + request.getElevatorFloorID());

            } catch (IOException e) {
                e.printStackTrace();
                System.exit(1);
            }
        }
    }
    // get the position of a specific elevator
    private int getElevatorPosition(int elevatorNumber) {
        switch (elevatorNumber) {
            case 1:
                return elevator1Position;
            case 2:
                return elevator2Position;
            case 3:
                return elevator3Position;
            case 4:
                return elevator4Position;
            default:
                return -1;
        }
    }

    // get the direction of a specific elevator
    private int getElevatorDirection(int elevatorNumber) {
        switch (elevatorNumber) {
            case 1:
                return elevator1Direction;
            case 2:
                return elevator2Direction;
            case 3:
                return elevator3Direction;
            case 4:
                return elevator4Direction;
            default:
                return -1;
        }
    }
    public int getClosestElevator(int floorNumber) {
        int closestElevator = -1;
        int minDistance = Integer.MAX_VALUE; // Initialize minDistance to maximum possible value

        for (int i = 1; i <= 3; i++) { //change 3 to 4 for 4th elevator
            int distance = Math.abs(getElevatorPosition(i) - floorNumber); // calculate distance to floor

            // Check if the elevator is already at the requested floor
            if (getElevatorPosition(i) == floorNumber) {
                closestElevator = i;
                break; // No need to continue if an elevator is already at the floor
            }

            // Check if the elevator is pointing in the right direction
            if ((getElevatorDirection(i) == 1 && floorNumber > getElevatorPosition(i)) ||
                    (getElevatorDirection(i) == 0 && floorNumber < getElevatorPosition(i))) {
                // check if the distance is smaller than the current minimum distance
                if (distance < minDistance) {
                    minDistance = distance;
                    closestElevator = i;
                }
            }
        }

        // If no elevator is already at the requested floor, print the closest elevator
        if (closestElevator != -1) {
            System.out.println("Elevator " + closestElevator + " is the closest to floor " + floorNumber);
        }

        return closestElevator;
    }

    @Override
    public void run(){


        while(true){

            request();

        }
    }
}
