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
    private InetAddress schedulerAddress;
    private final int schedulerPort = 41; // Port where the scheduler is listening
    private FloorButton upButton, downButton;
    private FloorLamp upLamp, downLamp;

    public Floor(int floorNumber) {
        this.floorNumber = floorNumber;
        this.waiting = false;
        this.port = floorNumber;
        try {
            this.sendSocket = new DatagramSocket();
            this.receiveSocket = new DatagramSocket(port);
            this.schedulerAddress = InetAddress.getLocalHost(); // Assuming scheduler is on the same machine
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(1);
        }

        upButton = new FloorButton(1);
        downButton = new FloorButton(0);
        upLamp = new FloorLamp();
        downLamp = new FloorLamp();

        System.out.println("Floor " + floorNumber + " has been created");
    }

    public boolean isWaiting() {
        return waiting;
    }

    public int getFloorNumber() {
        return floorNumber;
    }

    public boolean pushButton(boolean buttonDirection) {
        int buttonId = buttonDirection ? 1 : 0;
        LocalDateTime currentTime = LocalDateTime.now();
        Request newRequest = new Request(false, currentTime, floorNumber, buttonId, floorNumber, floorNumber);

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
            return false;
        }
    }

    public void resetButtons(Floor floor){// resets all buttons and lamps
        floor.upLamp.turnOffLamp();
        floor.downLamp.turnOffLamp();
        floor.upButton.resetButton();
        floor.downButton.resetButton();
        System.out.println("Buttons and lamps reset at floor " + floorNumber);
    }
    public void handleButtons(Response response){
        if(response.getFloorNumber()==this.floorNumber){
            resetButtons(this);
        }
    }

    public void receiveResponse(){
        byte[] data = new byte[200];
        DatagramPacket packetToReceive = new DatagramPacket(data, data.length);

        try {
            receiveSocket.setSoTimeout(1);
            receiveSocket.receive(packetToReceive);
        } catch (SocketTimeoutException e) {
            //System.out.println("Timeout Exception"); // Handles SocketTimeoutException if no packet is received within the specified timeout
            return;
        } catch (IOException e) {
            //System.out.println("IOException"); // Handles IOException if any occurs during receive operation
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
        // Push buttons
        if (floorNumber == 4){
            pushButton(true);
        }
        if (floorNumber == 7){
            pushButton(true);
        }
        if (floorNumber == 6){
            pushButton(true);
        }
        int i = 0;
        while(true){
            i+=1;
            if(i == 25000){
                if (floorNumber == 2){
                    pushButton(true);
                }
            }
            receiveResponse();
        }
    }
}
