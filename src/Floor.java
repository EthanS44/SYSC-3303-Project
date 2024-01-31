import java.util.concurrent.BlockingQueue;

public class Floor implements Runnable {


    private int floorNumber;
    private boolean waiting;
    private final BlockingQueue<Request> requestQueue; // Assuming a shared request queue for communication

    public Floor(int floorNumber, BlockingQueue<Request> requestQueue) {
        this.floorNumber = floorNumber;
        this.waiting = false;
        this.requestQueue = requestQueue;
    }

    public boolean isWaiting(){
        return waiting;
    }

    public int getFloorNumber() {
        return floorNumber;
    }

    public boolean buttonPushed(boolean buttonDirection){
        // converts the buttonDirection to buttonID (0 = down and 1 = up)
        int buttonId = buttonDirection ? 1 : 0;
        // Create a new request with the current time, floor number, and button direction
        Request newRequest = new Request(tonId);
        // Try to send this request to the Scheduler
        try {
            requestQueue.put(newRequest);
            System.out.println("Floor " + floorNumber + ": Request for " + (buttonDirection ? "UP" : "DOWN") + " button pushed.");
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            System.out.println("Failed to send request from floor " + floorNumber);
        }
        this.waiting = true; // The floor is now waiting for an elevator
    }



    @Override
    public void run(){}

}
