/**
 * The Timer class allows classes to keep track of time.
 * @author Sam Wilson
 */
public class Timer implements Runnable {
    private int time;
    private boolean isActive;
    private Elevator elevator;


    /**
     * Constructor for timer class
     */
    public Timer(){
        this.time = 0;
        this.isActive = false;
    }
    /**
     * Set timer
     * @param amount
     */
    public void setTimer(int amount){
        this.time = amount;
        this.isActive = true;
    }
    /**
     * Kill the timer
     */
    public void killTimer(){
        this.time = 0;
        this.isActive = false;
    }
    @Override
    public void run(){
        while(true){

            try {
                Thread.sleep(50);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }

            if(isActive()){
                try {
                    Thread.sleep(999);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                if (time > 0){
                    this.time--;
                    // System.out.println("Time: " + time);
                } else if (time == 0) {
                    if(isActive()){
                        this.isActive = false;
                        System.out.println("Elevator timeout - Elevator "+ this.elevator.getElevatorID() + " Disabled.");
                        this.elevator.disableElevator();
                    }
                }
            }
        }
    }

    /**
     * Purely for testing purposes, uses the same logic and coding as the run method
     */
    public void runTimer(){
        while(time > 0){
            try {
                Thread.sleep(999);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            setTimer(time-1);
        }
    }
    /**
     * get the time
     * @return time
     */
    public int getTime(){
        return this.time;
    }
    /**
     * Check if the timer is active
     * @return true if so false otherwise
     */
    public boolean isActive(){
        return this.isActive;
    }

    /**
     * Used for testing, uses same logic
     * @return - true for active, false for inactvie
     */
    public boolean isActiveTest(){
        if(elevator.getCurrentFloor() == -1){
            return false;
        }
        return true;
    }
    /**
     * Set elevator
     * @param elevator
     */
    public void setElevator(Elevator elevator){
        this.elevator = elevator;
    }
    /**
     * Get elevator
     * @return elevator
     */
    public Elevator getElevator() { return elevator; }
}
