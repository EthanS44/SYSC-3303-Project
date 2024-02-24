import java.util.concurrent.BlockingQueue;
import java.time.*;

public class Floor implements Runnable {


    private int floorNumber;
    private boolean waiting;
    // private final BlockingQueue<Request> requestQueue; // Assuming a shared request queue for communication
    private ElevatorQueue requestQueue;
    private FloorButton upButton, downButton;
    private FloorLamp upLamp, downLamp;

    /**
     * Constructor for the floor class
     * @param floorNumber - specific floor
     * @param queue - Shared queue to feed requests to the scheduler
     */
    public Floor(int floorNumber, ElevatorQueue queue) { // BlockingQueue<Request> requestQueue) {
        System.out.println("Floor "+ floorNumber + " been created");

        this.floorNumber = floorNumber;
        this.waiting = false;
        this.requestQueue = queue;
        upButton = new FloorButton(1);
        downButton = new FloorButton(0);
        upLamp = new FloorLamp();
        downLamp = new FloorLamp();
    }

    /**
     * Getter for the isWaiting variable
     * @return - True = waiting, False = not waiting
     */
    public boolean isWaiting(){
        return waiting;
    }

    /**
     * Getter for the floor number
     * @return - Floor number
     *
    public int getFloorNumber() {
        return floorNumber;
    }


    /**
     * When a button is pushed this function puts a request into the request box
     * @param buttonDirection - What direction the user wants to go
     * @return - true if the request has went through
     */
    public boolean pushButton(boolean buttonDirection){
        // converts the buttonDirection to buttonID (0 = down and 1 = up)
        int buttonId = buttonDirection ? 1 : 0;
        switch (buttonId){
            case 0:
                downButton.pressButton();
                downLamp.turnOnLamp();
                System.out.println("Down button pressed on floor "+floorNumber);
            case 1:
                upButton.pressButton();
                upLamp.turnOnLamp();
                System.out.println("Up button pressed on floor "+floorNumber);
        }
        LocalDateTime currentTime = LocalDateTime.now();
        // Create a new request with the current time, floor number, and button direction
        Request newRequest = new Request(false, currentTime , floorNumber, buttonId, getFloorNumber());
        // Try to send this request to the Scheduler
        //requestQueue.put(newRequest);
        requestQueue.putInRequestBox(newRequest);
        //System.out.println("Floor " + floorNumber + ": Request for " + (buttonDirection ? "UP" : "DOWN") + " button pushed.");
        this.waiting = true; // The floor is now waiting for an elevator
        //Change this, it is temporary
        return true;
    }

    public void resetLamps(){
        upButton.resetButton();
        downButton.resetButton();
        upLamp.turnOffLamp();
        downLamp.turnOffLamp();
        System.out.println("Lights and lamps on floor " + floorNumber + " reset\n");
    }

    public void handleButtons(){

        if (!(requestQueue.isResponseBoxEmpty())){
            if ((requestQueue.responseBox().get(0).getFloorNumber() == floorNumber)) {
                requestQueue.getFromResponseBox();
                System.out.println("Handling buttons on floor " + floorNumber + "\n");
                resetLamps();
            }
        }
    }

    @Override
    public void run() {
        if (floorNumber == 2) {
            pushButton(true);
        }
        while(true){
            handleButtons();
        }
    }
}
