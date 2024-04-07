/**
 * The Timer class allows classes to keep track of time.
 * @author Sam Wilson
 */
public class Timer implements Runnable {
    private int time;
    private boolean isActive;
    private Elevator elevator;

    public Timer(){
        this.time = 0;
        this.isActive = false;
    }
    public void setTimer(int amount){
        this.time = amount;
        this.isActive = true;
    }
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
    public int getTime(){
        return this.time;
    }
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
    public void setActive(boolean active){
        this.isActive = active;
    }
    public void setElevator(Elevator elevator){
        this.elevator = elevator;
    }
    public Elevator getElevator() { return elevator; }
}
