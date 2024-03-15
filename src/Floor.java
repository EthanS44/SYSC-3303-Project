import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.time.LocalDateTime;
import java.util.Arrays;

public class Floor implements Runnable {

    private int floorNumber;
    private boolean waiting;
    private DatagramSocket sendSocket;
    private InetAddress schedulerAddress;
    private final int schedulerPort = 41; // Port where the scheduler is listening
    private FloorButton upButton, downButton;
    private FloorLamp upLamp, downLamp;

    public Floor(int floorNumber) {
        this.floorNumber = floorNumber;
        this.waiting = false;
        try {
            this.sendSocket = new DatagramSocket();
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
        Request newRequest = new Request(false, currentTime, floorNumber, buttonId, floorNumber);

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

    @Override
    public void run() {
        // Example: Automatically push the up button on floor 2 upon startup

        if (floorNumber == 5){
            pushButton(true);
        }
        /*
        if (floorNumber == 4){
            pushButton(true);
        }
        if (floorNumber == 1){
            pushButton(true);
        }
        if (floorNumber == 3){
            pushButton(true);
        }
        if (floorNumber == 2){
            pushButton(true);
        }

         */
    }
}
