import java.util.concurrent.BlockingQueue;
import java.time.*;


// (ABODY)
public class Floor implements Runnable {

    private ElevatorQueue elevatorQueue;
    private int floorNumber;
    private boolean waiting;
    // private final BlockingQueue<Request> requestQueue; // Assuming a shared request queue for communication
    //private ElevatorQueue requestQueue;

    public Floor(int floorNumber, ElevatorQueue queue) { // BlockingQueue<Request> requestQueue) {
        System.out.println("Floor "+ floorNumber + " been created");
        this.floorNumber = floorNumber;
        this.waiting = false;
        this.elevatorQueue = queue;
        //this.requestQueue = requestQueue;

    }

    public boolean isWaiting(){
        return waiting;
    }

    public int getFloorNumber() {
        return floorNumber;
    }

    public void buttonPushed(boolean buttonDirection) {
        System.out.println("Button pressed on floor " + floorNumber);
        LocalDateTime currentTime = LocalDateTime.now();
        int buttonId = buttonDirection ? 1 : 0; // 1 for up, 0 for down
        Request newRequest = new Request(false, currentTime, floorNumber, buttonId, floorNumber);
        elevatorQueue.putInRequestBox(newRequest);
        System.out.println("Floor " + floorNumber + ": Request for " + (buttonDirection ? "UP" : "DOWN") + " button pushed.");
        this.waiting = true; // The floor is now waiting for an elevator
    }



    @Override
    public void run() {
        if (floorNumber == 2){
            buttonPushed(true);
        } else if (floorNumber == 6){
            buttonPushed(false);
        }
        /*try {
            while (!Thread.interrupted()) {
                if (floorNumber == 2 || floorNumber == 5) {
                    // For odd floors, simulate pressing the "up" button.
                    buttonPushed(true);
                } else {
                    // For even floors, simulate pressing the "down" button.
                    buttonPushed(false);
                }
                Thread.sleep(10000); // Wait for 10 seconds before the next button press
            }
        } catch (InterruptedException e) {
            System.out.println("Floor " + floorNumber + " interrupted.");
            Thread.currentThread().interrupt();
        }*/
    }

}
