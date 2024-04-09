import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketTimeoutException;
import java.time.LocalDateTime;
import java.util.Arrays;

public class Floor implements Runnable {

    private int floorNumber;
    private int port;
    private boolean waiting;
    private DatagramSocket sendSocket;
    private DatagramSocket receiveSocket;
    private DatagramSocket requestSocket;
    private InetAddress schedulerAddress;
    private final int schedulerPort = 41; // Port where the scheduler is listening
    private FloorButton upButton, downButton;
    private FloorLamp upLamp, downLamp;
    private int requestPort;

    /**
     * Constructs a new Floor object with the specified floor number.
     *
     * @param floorNumber The floor number.
     */
    public Floor(int floorNumber) {
        this.floorNumber = floorNumber;
        this.requestPort = floorNumber + 100;
        this.waiting = false;
        this.port = floorNumber;
        try {
            this.sendSocket = new DatagramSocket();
            this.receiveSocket = new DatagramSocket(port);
            this.requestSocket = new DatagramSocket(requestPort);
            this.schedulerAddress = InetAddress.getLocalHost(); // Assuming scheduler is on the same machine
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Floor - Failed to create sockets");
            System.exit(1);
        }

        upButton = new FloorButton(1);
        downButton = new FloorButton(0);
        upLamp = new FloorLamp();
        downLamp = new FloorLamp();

        System.out.println("Floor " + floorNumber + " has been created");
    }

    /**
     * Testing Constructor for floor
     * @param floorNumber
     * @param isTest
     */
    public Floor(int floorNumber, boolean isTest){
        this.floorNumber = floorNumber;
        if(!isTest){
            System.exit(1);
        }
    }

    /**
     * This function checks if floor is waiting for request to be recieved or not
     * @return true if waiting, false otherwise
     */
    public boolean isWaiting() {
        return waiting;
    }

    /**
     * This function gets the current floor number
     * @return current floor number
     */
    public int getFloorNumber() {
        return floorNumber;
    }

    /**
     * Pushes the button for calling an elevator with the specified direction and trigger fault.
     *
     * @param buttonDirection The direction of the button (true for up, false for down).
     * @param triggerFault The trigger fault.
     * @return true if the request was successfully sent, false otherwise.
     */
    public boolean pushButton(boolean buttonDirection, int triggerFault) {
        int buttonId = buttonDirection ? 1 : 0;
        LocalDateTime currentTime = LocalDateTime.now();
        Request newRequest = new Request(false, currentTime, floorNumber, buttonId, floorNumber, floorNumber, triggerFault);

        try {
            byte[] requestData = Request.toByteArray(newRequest);

            DatagramPacket sendPacket = new DatagramPacket(requestData, requestData.length, schedulerAddress, schedulerPort);
            sendSocket.send(sendPacket);
            this.waiting = true;
            System.out.println("Floor " + floorNumber + ": Request for " + (buttonDirection ? "UP" : "DOWN") + " button pushed and sent.");

            // Update lamp status based on the button pressed
            if (buttonDirection) {
                upLamp.turnOnLamp();
                System.out.println("Up lamp turned on at floor " + floorNumber);
            } else {
                downLamp.turnOnLamp();
                System.out.println("Down lamp turned on at floor " + floorNumber);
            }

            return true;
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Floor failed to send Request to Scheduler");
            return false;
        }
    }

    /**
     * Receive request from the parser
     */
    public void receiveRequest(){
        byte[] data = new byte[2];
        DatagramPacket packetToReceive = new DatagramPacket(data, data.length);

        try {
            requestSocket.setSoTimeout(1);
            requestSocket.receive(packetToReceive);
        } catch (SocketTimeoutException e) {
            return;
        } catch (IOException e) {
            System.out.println("Floor failed to receive request - IOException");
            return;
        }

        System.out.println("Floor Receiving request");
        //collect the received packet and convert it to response

        boolean direction = packetToReceive.getData()[0] == 1;
        int triggerFault = packetToReceive.getData()[1];
        this.pushButton(direction, triggerFault);
    }

    /**
     * Purely for testing purposes has same logic
     * @param buttonDirection
     * @param isTest
     * @return
     */
    public void pushButton(boolean buttonDirection, boolean isTest){
        System.out.println("Floor " + floorNumber + ": Request for " + (buttonDirection ? "UP" : "DOWN") + " button pushed and sent.");

        upLamp = new FloorLamp();
        // Update lamp status based on the button pressed
        if (buttonDirection) {
            upLamp.turnOnLamp();
            System.out.println("Up lamp turned on at floor " + floorNumber);
        } else {
            downLamp.turnOnLamp();
            System.out.println("Down lamp turned on at floor " + floorNumber);
        }
    }
    /**
     * Resets all buttons and lamps on the floor.
     *
     * @param floor The floor to reset buttons and lamps.
     */
    public void resetButtons(Floor floor){// resets all buttons and lamps
        floor.upLamp.turnOffLamp();
        floor.downLamp.turnOffLamp();
        floor.upButton.resetButton();
        floor.downButton.resetButton();
        System.out.println("Buttons and lamps reset at floor " + floorNumber);
    }

    /**
     * Receive response from the scheduler
     */
    public void receiveResponse(){
        byte[] data = new byte[200];
        DatagramPacket packetToReceive = new DatagramPacket(data, data.length);

        try {
            receiveSocket.setSoTimeout(1);
            receiveSocket.receive(packetToReceive);
        } catch (SocketTimeoutException e) {
            return;
        } catch (IOException e) {
            System.out.println("Floor failed to receive response - IOException");
            return;
        }

        System.out.println("Floor Receiving response");
        //collect the received packet and convert it to response
        Response response;
        try {
            response = Response.toResponse(packetToReceive.getData());
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Floor: IOException");
            return;
        }
       resetButtons(this);
    }

    @Override
    public void run() {
        while(true){
            receiveRequest();
            receiveResponse();
        }
    }
}

