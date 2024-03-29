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
                    this.isActive = false;
                    System.out.println("Elevator timeout - Elevator "+ this.elevator.getElevatorID() + " Disabled.");
                    this.elevator.disableElevator();
                }
            }
        }
    }
    public int getTime(){
        return this.time;
    }
    public boolean isActive(){
        return this.isActive;
    }
    public void setActive(boolean active){
        this.isActive = active;
    }
    public void setElevator(Elevator elevator){
        this.elevator = elevator;
    }
}
