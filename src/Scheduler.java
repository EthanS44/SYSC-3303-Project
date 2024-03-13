import java.io.IOException;
import java.lang.reflect.Array;
import java.net.*;
import java.util.ArrayList;

//thought process since we delete the elevator queue the methods that depended on it how will they be handled
//Also would the scheduler send response back to the floor
//receive request will be called to collect packet then change to request add to request box. it will be
// handles converted to instruction from instruction to bytes to be sent over to Elevator. Response will be
// received not sure if it should be sent to the floor or anything that requested something



public class Scheduler implements Runnable {
    private static final int numberOfFloors = 7;
    //private final ElevatorQueue elevatorqueue;
    private schedulerState currentState; //new
    private ArrayList<Floor> floors;
    private ArrayList<Request> requestBox;
    private ArrayList<Response> responseBox;


    DatagramSocket receiveSocket;
    DatagramSocket sendReceiveSocket;

    class SchedulerWaiting implements schedulerState {
        @Override
        public void handle(Scheduler scheduler){
            System.out.println("Is request box empty: " + scheduler.noPendingRequests());

            if (!scheduler.noPendingRequests()){
                scheduler.setCurrentState(new HandlingRequest());
                System.out.println("Scheduler state changed to HandlingRequest\n");
            }

            if (!scheduler.noPendingResponses()){
                Response response = scheduler.responseBox.get(0);
                responseBox.remove(response);
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

    public Scheduler(){
        this.requestBox = new ArrayList<Request>();
        this.responseBox = new ArrayList<Response>();
        //this.elevatorqueue = queue;
        this.currentState = new SchedulerWaiting(); //new
        System.out.println("Scheduler created\n");

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
        if (requestBox.isEmpty()) {
            System.out.println("No requests to handle.");
            return;
        }

        // not sure if it should Iterate over the ArrayList of requests
        // for (Request requestToHandle : requestBox) {
            Request requestToHandle = requestBox.get(0);
            System.out.println("Request received by scheduler, now handling\n");

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
            //Convert to packet and send instruction
            sendInstructionToElevator(new Instruction(tempDirection, tempFloorNumber));
            //elevatorqueue.putInInstructionBox(new Instruction(tempDirection,tempFloorNumber));
            System.out.println("Scheduler: Request handled");
            // remove request from requestBox
            requestBox.remove(requestToHandle);

    }

    public boolean noPendingRequests() {
        return requestBox.isEmpty();
    }

    public boolean noPendingResponses(){
        return requestBox.isEmpty();
    }

    /**This method Converts instruction to packet to send to elevator
     *
     * @param instruction
     */
    public void sendInstructionToElevator(Instruction instruction){
        // send packet to Elevator
        try {
            // Serialize instruction object to bytes
            byte[] instructionBytes = instruction.toByteArray();
            // Create UDP packet with instruction bytes, destination IP, and port
            DatagramPacket packet = new DatagramPacket(instructionBytes, instructionBytes.length, InetAddress.getLocalHost(), 31);

            sendReceiveSocket.send(packet);
            System.out.println("Scheduler: Instruction sent to Elevator.\n");
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(1);
        }

        // Receive response packet from Elevator
        byte[] data = new byte[100];
        DatagramPacket packetToReceive = new DatagramPacket(data, data.length);

        try {
            sendReceiveSocket.receive(packetToReceive);
        } catch(IOException e) {
            e.printStackTrace();
            System.exit(1);
        }

        //collect the received packet and convert it to response
        Response response;
        try {
            response = Response.toResponse(packetToReceive.getData());
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            return;
        }
        responseBox.add(response);



    }

    /**
     * This method receives packet and Converts the packet to a request
     * The request will be added to the request box
     */

    public void receiveRequest (){
        // Create new packet to hold received data
        byte[] data = new byte[100];
        DatagramPacket packetToReceive = null;

        packetToReceive = new DatagramPacket(data, data.length);

        // Receive request packet
        try {
            receiveSocket.receive(packetToReceive);
            System.out.println("\nScheduler: Request received");
        } catch(IOException e) {
            e.printStackTrace();
            System.exit(1);
        }

        //convert the received packet to bytes.
        Request request;
        try {
            request = Request.toRequest(packetToReceive.getData());
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            return;
        }

        requestBox.add(request);

        // Creating response packet
        Response response = responseBox.get(0);

        // remove request from responseBox
        responseBox.remove(response);

        DatagramPacket newPacket = null;
        try {

            // Serialize instruction object to bytes
            byte[] responseBytes = response.toByteArray();

            // Create UDP packet with instruction bytes, destination IP, and port

            newPacket = new DatagramPacket(responseBytes , responseBytes .length, InetAddress.getLocalHost(), packetToReceive.getPort());
        } catch (UnknownHostException e) {
            e.printStackTrace();
            System.exit(1);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        // Sending response packet
        try {
            receiveSocket.send(newPacket);
            System.out.println("Scheduler: Response packet sent");
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(1);
        }



    }





    @Override
    public void run(){

        while(true){
            // handleRequest();
            receiveRequest();
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