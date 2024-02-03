import java.util.concurrent.BlockingQueue;
import java.time.*;

public class Floor implements Runnable {


    private ElevatorQueue elevatorqueue;
    private int floorNumber;
    private boolean waiting;
    // private final BlockingQueue<Request> requestQueue; // Assuming a shared request queue for communication
    private ElevatorQueue requestQueue;

    public Floor(int floorNumber, ElevatorQueue queue) { // BlockingQueue<Request> requestQueue) {
        System.out.println("Floor "+ floorNumber + " been created");
        this.floorNumber = floorNumber;
        this.waiting = false;
        this.requestQueue = queue;
        //this.requestQueue = requestQueue;

    }

    public boolean isWaiting(){
        return waiting;
    }

    public int getFloorNumber() {
        return floorNumber;
    }

    public boolean buttonPushed(boolean buttonDirection){
        System.out.println("Button pressed on floor "+floorNumber);
        LocalDateTime currentTime = LocalDateTime.now();
        // converts the buttonDirection to buttonID (0 = down and 1 = up)
        int buttonId = buttonDirection ? 1 : 0;
        // Create a new request with the current time, floor number, and button direction
        Request newRequest = new Request(false, currentTime , floorNumber, buttonId, getFloorNumber());
        // Try to send this request to the Scheduler
        //requestQueue.put(newRequest);
        requestQueue.putInRequestBox(newRequest);
        System.out.println("Floor " + floorNumber + ": Request for " + (buttonDirection ? "UP" : "DOWN") + " button pushed.");
        this.waiting = true; // The floor is now waiting for an elevator

        //Change this, it is temporary
        return true;
    }



    @Override
    public void run(){
      //  if (floorNumber % 2 == 1){
       //     buttonPushed(true);
      //  } else {
       //     buttonPushed(false);
       // }
    }

}
